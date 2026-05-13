package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import android.app.*
import android.content.*
import android.net.*
import android.os.*
import android.util.*
import androidx.activity.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.limurse.iap.*
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
        isPremium = false
        //val iapConnector = IapConnector(
        //    context = this@MainActivity,
        //    nonConsumableKeys = listOf("subscription_lifetime"),
        //    consumableKeys = arrayListOf(),
        //    subscriptionKeys = arrayListOf(),
        //    key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoU2V+jOBlz47MMp2UIxn6ji0kgRJw0AGdGX5K7nBcWh/869RufxLnqo+RpuLFLgMHcVlcRW2d8vLDq6zzlt3iTvrqbThswBI1IlkZ22ot+UTV2RP4no4jJF7eqcJSPwoKIM6L8t1aLa2rvlwI8K58G5x03tvpNQHp4sD8SLWFKrhXFuET9CCMIiSr4FKoo0YWtK1yprMOu3s+ddKjp/iWYNv3iX5SU7FhJ1uXuxIeZShX7Ulgy7Oji+ACJfoQgYU6xpODoCA7ymEtih4SI3Tojzwg2nMfMvGH3KzUC2Uizd9UAg19PN9H4WHQVjKlRlzGRBR791a3ufDil9vYMl8UwIDAQAB",
        //    enableLogging = true
        //)
        //iapConnector.addPurchaseListener(object : PurchaseServiceListener {
        //    override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {}
        //    override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {}
        //
        //    override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
        //        Log.e("TAG", "onSubscriptionRestored: $purchaseInfo")
        //        if (purchaseInfo.sku == "subscription_lifetime") {
        //            isPremium = true
        //        }
        //    }
        //
        //    override fun onPurchaseFailed(purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?) {}
        //})
        init { requestConsentForm() }
    }

    private fun requestConsentForm() {
        if (!isPremium && isNetworkAvailable()) {
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

    /**
     * Launch sequence: Splash → Language → Permission → Dashboard.
     *
     * The previous chain included an Onboarding step between Permission and
     * Dashboard. [AppBoardingActivity] still exists in the codebase and the
     * `IS_ONBOARDING_ENABLED` TinyDB flag is still defined — they're just no
     * longer wired into the cold-start chain. If you want to surface the
     * onboarding deck from Settings or a "What's new" banner later, you can
     * launch [AppBoardingActivity] directly without re-introducing it here.
     *
     * Language uses its "seen" flag in TinyDB (one-shot). The Permission step
     * is gated on the *live* OS permission state via
     * [hasAllAfterCallPermissions], so whenever any of the three after-call
     * permissions is missing (e.g. user revoked from Settings or skipped
     * them with "Maybe later"), the screen reappears every cold start. As
     * soon as all three are granted, the funnel is silently skipped.
     *
     * `isStartFlowRepeat()` keeps the existing "re-prompt language picker
     * periodically" behaviour.
     */
    private fun gotoDashboard() {
        val showLanguage = (isStartFlowRepeat() && !isPremium) ||
            tinyDB?.getBoolean(IS_LANGUAGE_ENABLED, true) == true
        val showPermission = !hasAllAfterCallPermissions() && !isPremium

        when {
            showLanguage -> go(AppLanguageActivity::class.java, finish = true)
            showPermission -> go(AfterCallPermissionActivity::class.java, finish = true)
            else -> go(HomeActivity::class.java, finish = true)
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