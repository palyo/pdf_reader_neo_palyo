package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor

import android.app.*
import android.content.*
import android.os.*
import androidx.lifecycle.*
import androidx.multidex.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.tom_roush.pdfbox.android.*

class App : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    companion object {
        private var instance: App? = null
        private var appContext: Context? = null
        var appOpenManager: AppOpenManager? = null
        var currentActivity: Activity? = null
        var classes: MutableList<Class<*>> = mutableListOf()
        var isOpenInter = false
        fun getInstance(): App = instance ?: throw IllegalStateException("Application is not created yet!")
        fun getAppContext(): Context = appContext ?: throw IllegalStateException("Application is not created yet!")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        languagePrefKey = "pdf_reader_key"
        themeToggleMode()
        PDFBoxResourceLoader.init(this.applicationContext)
        PDFReaderDatabase.getDatabase(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "pdf_reader_notify"
        val channelName = "General"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(this)
            }
        }
        setAvoidMultipleClass(
            mutableListOf(
                MainActivity::class.java,
                AppLanguageActivity::class.java,
            )
        )
        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                if (!isOpenInter && (applicationContext?.appOpenCount ?: 0) >= 2) {
                    if (isShowOpenAdsOnStart(currentActivity?.javaClass?.name ?: "")) {
                        viewAppOpen(listener = null, isWait = false)
                    }
                }

                if (currentActivity != null) {
                    if (isOpenInter) isOpenInter = false
                }
            }
        })
    }

    fun isShowOpenAdsOnStart(classname: String): Boolean {
        if (classname == "com.google.android.gms.ads.AdActivity" || AppOpenManager.isShowingAd) {
            return false
        }
        for (aClass in classes) {
            if (aClass.name.equals(classname, ignoreCase = true)) {
                return false
            }
        }

        return true
    }

    open fun setAvoidMultipleClass(aClass: MutableList<Class<*>>) {
        classes.addAll(aClass)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}