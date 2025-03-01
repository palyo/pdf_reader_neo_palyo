package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import android.app.*
import android.content.*
import android.net.*
import android.os.*
import androidx.activity.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.App.Companion.appOpenManager
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.PreloadNewNative.loadNativeAd
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*
import java.util.concurrent.atomic.*

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private var consentManager: ConsentManager? = null
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    override fun ActivityMainBinding.initExtra() {
        if (isPermissionAllowed()) {
            DocumentViewModel.getInstance(application).loadPreload()
        }
        init { requestConsentForm() }
    }

    private fun requestConsentForm() {
        if (isNetworkAvailable()) {
            consentManager = ConsentManager.getInstance(this)
            consentManager?.gatherConsent(this) { consentError ->
                if (consentManager?.canRequestAds == true) {
                    try {
                        initializeMobileAdsSdk()
                    } catch (_: Exception) {
                    }
                }
            }
        } else {
            delayed(3000L) {
                gotoDashboard()
            }
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        loadInterAd()
        loadNativeAd("NATIVE_ALL", mutableListOf(NATIVE_ID))
        appOpenManager = AppOpenManager()
        delayed(3000L) {
            viewInterAdForce {
                gotoDashboard()
            }
        }
    }

    private fun gotoDashboard() {
        if (isStartFlowRepeat() || tinyDB?.getBoolean(IS_LANGUAGE_ENABLED, true) == true) {
            go(AppLanguageActivity::class.java, finish = true)
        } else if (tinyDB?.getBoolean(IS_ONBOARDING_ENABLED, true) == true) {
            go(AppBoardingActivity::class.java, finish = true)
        } else {
            go(HomeActivity::class.java, finish = true)
        }
    }

    private fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun Activity.isPermissionAllowed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true
            }
        } else {
            if (hasPermissions(STORAGE_PERMISSION)) {
                return true
            }
        }
        return false
    }

    override fun ActivityMainBinding.initListeners() {

    }

    override fun ActivityMainBinding.initView() {
        onBackPressedDispatcher.addCallback { finish() }
    }
}