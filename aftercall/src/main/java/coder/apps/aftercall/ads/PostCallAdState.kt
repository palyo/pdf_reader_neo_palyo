package coder.apps.aftercall.ads

import android.content.Context
import androidx.multidex.MultiDexApplication
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.NATIVE
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd

open class PostCallAdState : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var appContext: Context? = null
        var adBannerLoadTimeValue: Long = 0L
        var adNativeLoadTimeValue: Long = 0L
        var bannerAdFailed = false
        var bannerAdImpression = false
        var bannerAdLoaded = false
        var isBannerAdLoading = false
        var bannerAdViewWidget: AdView? = null
        private var bannerLoadedListener: (() -> Unit)? = null
        private var bannerFailedListener: (() -> Unit)? = null
        var isCallingStart = false
        var isNativeFailedGooglePostCallValue = false
        var isNativeGoogleAdImpressionPostCall = false
        var isNativeGooglePostCallValue = false
        var isNativePostCallLoadingValue = false
        var isReLoadBannerAdsValue = false
        var isStartedService = false
        var nativeAdsPostCallAd: NativeAd? = null
        private var nativeLoadListener: (() -> Unit)? = null
        private var nativeFailedListener: (() -> Unit)? = null
        var adNativeExpirationTime: Long = 3600000L
        var adBannerExpirationTime: Long = 3300000L

        fun destroyUnusedNative() {
            val nativeAd = nativeAdsPostCallAd
            if (nativeAd != null && !isNativeGoogleAdImpressionPostCall) {
                nativeAd.destroy()
                AfterCallLog.d(NATIVE, "destroyUnusedNative: dropped preloaded ad with no impression")
            }
            nativeAdsPostCallAd = null
            isNativeGooglePostCallValue = false
            isNativeGoogleAdImpressionPostCall = false
            adNativeLoadTimeValue = 0L
            isNativePostCallLoadingValue = false
        }

        fun setOpenAdHide(z: Boolean) {
        }

        fun getNativeLoadListener(): (() -> Unit)? {
            return nativeLoadListener
        }

        fun getNativeFailedListener(): (() -> Unit)? {
            return nativeFailedListener
        }

        fun setNativeListener(onLoad: (() -> Unit)?, onFailed: (() -> Unit)?) {
            nativeLoadListener = onLoad
            nativeFailedListener = onFailed
        }

        fun setNativeFailedGooglePostCall(z: Boolean) {
            isNativeFailedGooglePostCallValue = z
        }

        fun getBannerAdView(): AdView? {
            return bannerAdViewWidget
        }

        fun getBannerLoadedListener(): (() -> Unit)? {
            return bannerLoadedListener
        }

        fun getBannerFailedListener(): (() -> Unit)? {
            return bannerFailedListener
        }

        fun setBannerListener(onLoaded: (() -> Unit)?, onFailed: (() -> Unit)?) {
            bannerLoadedListener = onLoaded
            bannerFailedListener = onFailed
        }

        fun setReLoadBannerAds(z: Boolean) {
            isReLoadBannerAdsValue = z
        }

        fun isCheckNotNull(): Boolean {
            return getNativeLoadListener() != null || getNativeFailedListener() != null
        }
    }
}
