package coder.apps.aftercall.ads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import coder.apps.aftercall.BuildConfig
import coder.apps.aftercall.R
import coder.apps.aftercall.ads.PostCallAdState.Companion.adNativeLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.getNativeFailedListener
import coder.apps.aftercall.ads.PostCallAdState.Companion.getNativeLoadListener
import coder.apps.aftercall.ads.PostCallAdState.Companion.isCheckNotNull
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeFailedGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeGoogleAdImpressionPostCall
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativePostCallLoadingValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.nativeAdsPostCallAd
import coder.apps.aftercall.ads.PostCallAdState.Companion.setNativeListener
import coder.apps.aftercall.ads.PostCallAdState.Companion.setOpenAdHide
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.NATIVE
import coder.apps.aftercall.extensions.firebaseASOEvent
import coder.apps.aftercall.extensions.isNetworkOn

class PostCallNativeAd(var context: Context) {

    private val idNativeAds1: String = if (BuildConfig.DEBUG)
        "ca-app-pub-3940256099942544/2247696110"
    else
        coder.apps.aftercall.AfterCallConfig.nativeAdUnitId
            ?: "ca-app-pub-3940256099942544/2247696110"

    fun initNativeListener(onLoad: (() -> Unit)?, onFailed: (() -> Unit)?) {
        setNativeListener(onLoad, onFailed)
    }

    fun loadPostNative() {
        AfterCallLog.d(NATIVE, "loadPostNative: requested")
        loadNativeAds()
    }

    fun loadNativeAds() {
        isNativePostCallLoadingValue = true
        isNativeFailedGooglePostCallValue = false
        isNativeGoogleAdImpressionPostCall = false
        if (!context.isNetworkOn()) {
            context.firebaseASOEvent("post_n_nw_off")
            AfterCallLog.w(NATIVE, "load skipped: no network")
            isNativePostCallLoadingValue = false
            isNativeFailedGooglePostCallValue = true
            getNativeFailedListener()?.invoke()
            return
        }
        if (idNativeAds1.isNotEmpty()) {
            val nativeAdOptions = NativeAdOptions.Builder()
                .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_ANY)
                .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                .build()

            val adLoader = AdLoader.Builder(context, idNativeAds1)
                .forNativeAd { nativeAd ->
                    AfterCallLog.i(NATIVE, "loaded (unit=$idNativeAds1)")
                    nativeAdsPostCallAd = nativeAd
                    isNativePostCallLoadingValue = false
                    if (isCheckNotNull()) {
                        getNativeLoadListener()?.invoke()
                    }
                    adNativeLoadTimeValue = System.currentTimeMillis()
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        context.firebaseASOEvent("post_n_fail_${adError.code}")
                        AfterCallLog.w(NATIVE, "load failed (code=${adError.code}, msg=${adError.message})")
                        adNativeLoadTimeValue = 0L
                        nativeAdsPostCallAd = null
                        isNativeFailedGooglePostCallValue = true
                        isNativePostCallLoadingValue = false
                        getNativeFailedListener()?.invoke()
                    }

                    override fun onAdClicked() {
                        setOpenAdHide(true)
                    }

                    override fun onAdImpression() {
                        context.firebaseASOEvent("post_n_imp")
                        isNativeGoogleAdImpressionPostCall = true
                        nativeAdsPostCallAd = null
                        AfterCallLog.i(NATIVE, "impression recorded")
                    }

                    override fun onAdClosed() {}
                })
                .withNativeAdOptions(nativeAdOptions)
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            nativeAdsPostCallAd = null
            isNativePostCallLoadingValue = false
        }
    }

    fun populateUnifiedNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView,
        hideMedia: Boolean
    ): NativeAdView {
        val headline = adView.findViewById<TextView>(R.id.ad_headline)
        headline.text = nativeAd.headline
        adView.headlineView = headline

        val body = adView.findViewById<TextView>(R.id.ad_body)
        body.text = nativeAd.body
        adView.bodyView = body

        val icon = adView.findViewById<ImageView>(R.id.ad_app_icon)
        icon.setImageDrawable(nativeAd.icon?.drawable)
        adView.iconView = icon
        icon.visibility = if (nativeAd.icon == null) View.GONE else View.VISIBLE

        val cta = adView.findViewById<TextView>(R.id.ad_call_to_action)
        cta.text = nativeAd.callToAction
        adView.callToActionView = cta

        val relativeLayout = adView.findViewById<RelativeLayout>(R.id.iv_relative)
        if (hideMedia) {
            relativeLayout.visibility = View.GONE
        } else {
            adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
            relativeLayout.visibility = View.VISIBLE
        }
        AfterCallLog.d(NATIVE, "populated (hasIcon=${nativeAd.icon?.drawable != null})")
        adView.setNativeAd(nativeAd)
        return adView
    }

    fun showLoadingLayoutNative(frameLayout: FrameLayout) {
        val inflate = LayoutInflater.from(context)
            .inflate(R.layout.post_call_ad_native_large_loading, null) as FrameLayout
        (inflate.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)).startLayoutAnimation()
        frameLayout.visibility = View.VISIBLE
        frameLayout.removeAllViews()
        frameLayout.addView(inflate)
    }

    fun showNative(frameLayout: FrameLayout, nativeAd: NativeAd?, hideMedia: Boolean) {
        AfterCallLog.d(NATIVE, "showNative: ad=${if (nativeAd != null) "present" else "null"}")
        if (nativeAd == null) {
            frameLayout.visibility = View.GONE
            frameLayout.removeAllViews()
            return
        }
        val nativeAdView = LayoutInflater.from(context)
            .inflate(R.layout.post_call_ad_native_large, null) as NativeAdView
        try {
            populateUnifiedNativeAdView(nativeAd, nativeAdView, hideMedia)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        frameLayout.removeAllViews()
        frameLayout.addView(nativeAdView)
        frameLayout.visibility = View.VISIBLE
    }

    fun onDestroyAd() {
        val isImpression = isNativeGoogleAdImpressionPostCall
        AfterCallLog.d(NATIVE, "destroy: hasImpression=$isImpression")
        if (isImpression) {
            nativeAdsPostCallAd = null
            isNativeGooglePostCallValue = false
            isNativeGoogleAdImpressionPostCall = false
        }
    }

    fun onDestroyAdView() {
        nativeAdsPostCallAd = null
        isNativeGooglePostCallValue = false
    }
}
