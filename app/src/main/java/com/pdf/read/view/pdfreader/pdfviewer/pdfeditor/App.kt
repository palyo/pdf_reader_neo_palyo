package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor

import android.app.*
import android.content.*
import android.os.*
import androidx.lifecycle.*
import androidx.multidex.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import coder.apps.aftercall.AfterCallConfig
import coder.apps.aftercall.AfterCallTool
import coder.apps.aftercall.ui.PostCallActivity
import com.tom_roush.pdfbox.android.*
import kotlinx.coroutines.*
import java.io.File

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
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
                AfterCallPermissionActivity::class.java,
                PostCallActivity::class.java,
            )
        )

        configureAfterCallScreen()

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

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Refresh the after-call tool grid with the latest recent PDFs every time
        // PostCallActivity is created. Recents change frequently — we can't rely
        // on the static list set in onCreate() to stay accurate hours later.
        if (activity is PostCallActivity) {
            applicationScope.launch { refreshAfterCallTools() }
        }

        // Force AdMob's AdActivity (interstitial / rewarded / rewarded-interstitial
        // / app-open host) to honour system-bar insets on Android 16.
        //
        // Why this is needed: on Android 16 the OS enforces edge-to-edge for every
        // activity and *ignores* `android:windowOptOutEdgeToEdgeEnforcement`. Our
        // `Theme.AdActivity.OptOutEdgeToEdge` covers Android 15, but on API 36 the
        // SDK's translucent content draws under the status bar — that's why labels
        // like "Test Ad" and the close (X) glyph end up inside the status-bar strip.
        //
        // The fix is to install a WindowInsetsListener on the activity's content
        // view that converts the system-bar insets into top/bottom padding. The
        // SDK draws its creative inside `android.R.id.content`, so once that view
        // is padded, every overlay AdMob renders — including the "Test Ad" label
        // and the close button — sits in the safe area below the status bar.
//        if (activity.javaClass.name == "com.google.android.gms.ads.AdActivity") {
//            applySystemBarInsetsTo(activity)
//        }
    }

    /**
     * Pads the activity's root content view (`android.R.id.content`) by the
     * system-bar insets so its children — including any third-party overlays
     * AdMob renders on top of the ad creative — stay clear of the status bar
     * at the top and the gesture/nav bar at the bottom.
     *
     * The view's own background is also painted black. Without that, the
     * padded strip above the AdMob chrome would render whatever the window
     * background happens to be — and `Theme.Translucent.NoTitleBar` makes
     * that transparent, so the OS composites a white default underneath.
     * Setting a black background on the content view itself means the
     * status-bar-height padding strip renders black, matching the AdMob
     * chrome below it and producing a single black band above the ad.
     */
    private fun applySystemBarInsetsTo(activity: Activity) {
        val content = activity.findViewById<android.view.View>(android.R.id.content) ?: return
        content.setBackgroundColor(android.graphics.Color.BLACK)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
            val bars = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        // Trigger a re-dispatch if insets were already delivered before the
        // listener was attached (common when AdActivity is created very quickly).
        androidx.core.view.ViewCompat.requestApplyInsets(content)
    }

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

    // ── After-call wiring ────────────────────────────────────────────────────

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * One-time wiring done at process start: route "Open App" to HomeActivity
     * and seed the default tool grid with the main PDF features. The grid gets
     * re-populated with recent PDFs in [refreshAfterCallTools] when
     * [PostCallActivity] is actually shown, so the latest user activity always
     * appears front-and-centre.
     */
    private fun configureAfterCallScreen() {
        AfterCallConfig.setHostLauncher(HomeActivity::class.java)
        AfterCallConfig.setPrimaryAction(
            AfterCallTool(
                iconRes = R.drawable.ic_icon_file_pdf,
                label = getString(R.string.app_name)
            ) { ctx ->
                // Land the user on the Recent tab — they were just shown a list
                // of recents, opening the same tab makes the transition obvious.
                Intent(ctx, HomeActivity::class.java).putExtra(CURRENT_TAB, 1)
            }
        )
        AfterCallConfig.setTools(defaultFeatureTools())

        // Honour the user's Caller-ID master switch from Settings. When false,
        // the receivers/services short-circuit and the post-call screen never
        // launches — same effect as the user revoking permissions, except it
        // survives a full grant.
        AfterCallConfig.disabled = TinyDB(this).getBoolean(ENABLE_POST_CALL_SCREEN, true).not()
    }

    /**
     * Replaces the after-call tool grid with up to 6 of the user's most recent
     * PDFs. If the user has no recents yet, falls back to the static feature
     * shortcuts so the screen never renders empty.
     */
    private suspend fun refreshAfterCallTools() {
        val recents = runCatching {
            PDFReaderDatabase.getDatabase(this@App)
                .recentDao()
                .fetchRecent(limit = 6)
                .map { File(it.filePath) }
                .filter { it.exists() }
        }.getOrDefault(emptyList())

        if (recents.isEmpty()) {
            AfterCallConfig.setTools(defaultFeatureTools())
            return
        }

        AfterCallConfig.setTools(
            recents.map { file ->
                AfterCallTool(
                    iconRes = R.drawable.ic_file_icon_pdf,
                    label = file.nameWithoutExtension
                ) { ctx ->
                    Intent(ctx, PDFReaderActivity::class.java).apply {
                        putExtra(FILE_PATH, file.absolutePath)
                        putExtra(FILE_NAME, file.name)
                    }
                }
            }
        )
    }

    /** Static feature tiles shown when there are no recents to display. */
    private fun defaultFeatureTools(): List<AfterCallTool> = listOf(
        AfterCallTool(
            // Padded variant; the shared ic_tool_scanner fills its 24×24 viewport
            // edge-to-edge, which makes it look ~50% larger than the other
            // ic_action_* icons in the after-call grid. See the comment in
            // res/drawable/ic_after_call_tool_scanner.xml.
            iconRes = R.drawable.ic_after_call_tool_scanner,
            label = getString(R.string.title_document_scanner)
        ) { ctx -> Intent(ctx, ListDocScannedActivity::class.java) },
        AfterCallTool(
            iconRes = R.drawable.ic_action_merge,
            label = getString(R.string.action_merge_pdf).replace("\n", " ")
        ) { ctx -> Intent(ctx, MergeActivity::class.java) },
        AfterCallTool(
            iconRes = R.drawable.ic_action_split,
            label = getString(R.string.action_split_pdf).replace("\n", " ")
        ) { ctx -> Intent(ctx, SplitActivity::class.java) },
        AfterCallTool(
            iconRes = R.drawable.ic_action_compress,
            label = getString(R.string.action_compress_pdf).replace("\n", " ")
        ) { ctx -> Intent(ctx, CompressActivity::class.java) },
        AfterCallTool(
            iconRes = R.drawable.ic_action_word_to_pdf,
            label = getString(R.string.title_word_to_pdf)
        ) { ctx -> Intent(ctx, WordToPdfActivity::class.java) },
        AfterCallTool(
            iconRes = R.drawable.ic_action_text_to_pdf,
            label = getString(R.string.title_text_to_pdf)
        ) { ctx -> Intent(ctx, TextToPdfActivity::class.java) },
    )
}