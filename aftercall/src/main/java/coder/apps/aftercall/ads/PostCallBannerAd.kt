package coder.apps.aftercall.ads

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import coder.apps.aftercall.ads.PostCallAdState.Companion.adBannerLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdFailed
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdImpression
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdLoaded
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdViewWidget
import coder.apps.aftercall.ads.PostCallAdState.Companion.getBannerFailedListener
import coder.apps.aftercall.ads.PostCallAdState.Companion.getBannerLoadedListener
import coder.apps.aftercall.ads.PostCallAdState.Companion.isBannerAdLoading
import coder.apps.aftercall.ads.PostCallAdState.Companion.isReLoadBannerAdsValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.setBannerListener
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.BANNER
import coder.apps.aftercall.extensions.firebaseASOEvent
import coder.apps.aftercall.extensions.isNetworkOn
import coder.apps.aftercall.BuildConfig
import kotlin.math.roundToInt

class PostCallBannerAd {

    private var idBannerAds1: String = ""
    private var idBannerAds2: String = ""

    fun initBannerListener(onLoaded: (() -> Unit)?, onFailed: (() -> Unit)?) {
        setBannerListener(onLoaded, onFailed)
    }

    fun loadBanner(context: Context, frameLayout: FrameLayout?, view: View?) {
        val testBanner = "ca-app-pub-3940256099942544/6300978111"
        if (BuildConfig.DEBUG) {
            idBannerAds1 = testBanner
            idBannerAds2 = testBanner
        } else {
            idBannerAds1 = coder.apps.aftercall.AfterCallConfig.bannerAdUnitId1 ?: testBanner
            idBannerAds2 = coder.apps.aftercall.AfterCallConfig.bannerAdUnitId2 ?: idBannerAds1
        }
        AfterCallLog.d(BANNER, "loadBanner: requested (primary=$idBannerAds1, fallback=$idBannerAds2)")
        if (!context.isNetworkOn()) {
            context.firebaseASOEvent("post_b_nw_off")
            AfterCallLog.w(BANNER, "loadBanner: no network — hiding banner slot")
            if (frameLayout != null && view != null) hideBannerView(frameLayout, view)
            return
        }
        if (frameLayout != null && view != null) {
            frameLayout.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
        }
        if (isBannerAdLoading) return
        loadingBannerAds(context, frameLayout, view)
    }

    private fun loadingBannerAds(context: Context, frameLayout: FrameLayout?, view: View?) {
        if (bannerAdViewWidget != null && bannerAdLoaded) {
            if (frameLayout != null) {
                val bannerAdView = bannerAdViewWidget!!
                (bannerAdView.parent as? ViewGroup)?.removeView(bannerAdView)
                frameLayout.removeAllViews()
                frameLayout.addView(bannerAdView)
                frameLayout.visibility = View.VISIBLE
                view?.visibility = View.VISIBLE
            }
            AfterCallLog.d(BANNER, "reusing cached banner view")
        } else if (idBannerAds1.isNotEmpty()) {
            val inlineAdSize = getInlineAdSize(context)
            AfterCallLog.d(BANNER, "primary load start (unit=$idBannerAds1)")
            val adView = AdView(context)
            adView.adUnitId = idBannerAds1
            adView.setAdSize(inlineAdSize)
            bannerAdImpression = false
            bannerAdLoaded = false
            bannerAdFailed = false
            isBannerAdLoading = true
            bannerAdViewWidget = adView
            adView.loadAd(AdRequest.Builder().build())
            if (frameLayout != null && view != null) {
                frameLayout.visibility = View.VISIBLE
                view.visibility = View.VISIBLE
            }
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    AfterCallLog.i(BANNER, "primary loaded — attached=${frameLayout != null}")
                    adBannerLoadTimeValue = System.currentTimeMillis()
                    bannerAdImpression = false
                    isBannerAdLoading = false
                    bannerAdLoaded = true
                    if (frameLayout != null) {
                        frameLayout.removeAllViews()
                        frameLayout.addView(bannerAdViewWidget)
                    } else {
                        getBannerLoadedListener()?.invoke()
                    }
                }

                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdOpened() {}

                override fun onAdFailedToLoad(e: LoadAdError) {
                    context.firebaseASOEvent("post_b1_fail_${e.code}")
                    AfterCallLog.w(BANNER, "primary failed (code=${e.code}, msg=${e.message})")
                    bannerAdViewWidget = null
                    bannerAdLoaded = false
                    if (idBannerAds2.isNotEmpty()) {
                        AfterCallLog.d(BANNER, "falling back to secondary unit")
                        loadingBannerFailedAds(context, frameLayout, view)
                    } else {
                        isBannerAdLoading = false
                        bannerAdFailed = true
                        if (frameLayout != null && view != null) hideBannerView(frameLayout, view)
                        else getBannerFailedListener()?.invoke()
                        if (e.code == 0 || e.code == 2) isReLoadBannerAdsValue = true
                    }
                }

                override fun onAdImpression() {
                    context.firebaseASOEvent("post_b1_imp")
                    bannerAdImpression = true
                }
            }
        } else if (idBannerAds2.isNotEmpty()) {
            loadingBannerFailedAds(context, frameLayout, view)
        } else {
            bannerAdViewWidget = null
            isBannerAdLoading = false
            bannerAdFailed = false
            bannerAdLoaded = false
            if (frameLayout != null && view != null) hideBannerView(frameLayout, view)
            else getBannerFailedListener()?.invoke()
        }
    }

    fun loadingBannerFailedAds(context: Context, frameLayout: FrameLayout?, view: View?) {
        if (idBannerAds2.isNotEmpty()) {
            isBannerAdLoading = true
            val inlineAdSize = getInlineAdSize(context)
            AfterCallLog.d(BANNER, "secondary load start (unit=$idBannerAds2)")
            val adView = AdView(context)
            adView.setAdUnitId(idBannerAds2)
            adView.setAdSize(inlineAdSize)
            adView.loadAd(AdRequest.Builder().build())
            bannerAdViewWidget = adView
            if (frameLayout != null && view != null) {
                frameLayout.visibility = View.VISIBLE
                view.visibility = View.VISIBLE
            }
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adBannerLoadTimeValue = System.currentTimeMillis()
                    bannerAdImpression = false
                    isBannerAdLoading = false
                    bannerAdLoaded = true
                    if (frameLayout != null) {
                        frameLayout.removeAllViews()
                        frameLayout.addView(bannerAdViewWidget)
                    } else {
                        getBannerLoadedListener()?.invoke()
                    }
                }

                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdOpened() {}

                override fun onAdFailedToLoad(e: LoadAdError) {
                    context.firebaseASOEvent("post_b2_fail_${e.code}")
                    AfterCallLog.w(BANNER, "secondary failed (code=${e.code}, msg=${e.message})")
                    bannerAdViewWidget = null
                    bannerAdLoaded = false
                    isBannerAdLoading = false
                    bannerAdFailed = true
                    getBannerFailedListener()?.invoke()
                    if (e.code == 0 || e.code == 2) isReLoadBannerAdsValue = true
                }

                override fun onAdImpression() {
                    context.firebaseASOEvent("post_b2_imp")
                    bannerAdImpression = true
                }
            }
            return
        }
        if (frameLayout != null && view != null) hideBannerView(frameLayout, view)
        else getBannerFailedListener()?.invoke()
        bannerAdViewWidget = null
        isBannerAdLoading = false
        bannerAdFailed = false
        bannerAdLoaded = false
    }

    private fun getInlineAdSize(context: Context): AdSize {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val widthDp = (displayMetrics.widthPixels / displayMetrics.density).roundToInt()
        return AdSize.getPortraitInlineAdaptiveBannerAdSize(context, widthDp)
    }

    fun hideBannerView(frameLayout: FrameLayout, view: View) {
        frameLayout.removeAllViews()
        frameLayout.visibility = View.GONE
        view.visibility = View.GONE
    }

    fun onAdExpired(context: Context) {
        bannerAdViewWidget?.destroy()
        bannerAdViewWidget = null
        bannerAdImpression = false
        bannerAdLoaded = false
        bannerAdFailed = false
        adBannerLoadTimeValue = 0L
        loadBanner(context, null, null)
    }

    fun onDestroyAd() {
        if (bannerAdViewWidget == null || !bannerAdImpression) return
        bannerAdViewWidget?.destroy()
        bannerAdViewWidget = null
        bannerAdImpression = false
        bannerAdLoaded = false
        bannerAdFailed = false
        adBannerLoadTimeValue = 0L
    }
}
