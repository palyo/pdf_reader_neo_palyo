package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.app.*
import android.os.*
import android.util.*
import android.view.*
import com.google.android.gms.ads.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*

fun Activity.viewBanner(container: ViewGroup) {
    if (container.childCount > 0) container.removeAllViews()
    val adSize = getAdSize()
    val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (adSize.height + 2).toFloat(), resources.displayMetrics).toInt()
    val params: ViewGroup.LayoutParams = container.layoutParams
    container.setBackgroundResource(coder.apps.space.library.R.color.colorSecondaryBackground)
    val view = AdUnifiedBannerLoadingBinding.inflate(LayoutInflater.from(applicationContext), null, false)
    view.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    params.height = height
    container.layoutParams = params
    container.addView(view.root)
    val adView = AdView(this)
    adView.setAdSize(adSize)
    adView.adUnitId = BANNER_ID
    adView.adListener = object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            if (container.childCount > 0) container.removeAllViews()
            container.addView(adView)
        }
    }
    adView.loadAd(AdRequest.Builder().build())
}

fun Activity.viewCollapsibleBanner(container: ViewGroup) {
    if (container.childCount > 0) container.removeAllViews()
    val adSize = getAdSize()
    val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (adSize.height + 2).toFloat(), resources.displayMetrics).toInt()
    val params: ViewGroup.LayoutParams = container.layoutParams
    container.setBackgroundResource(coder.apps.space.library.R.color.colorSecondaryBackground)
    val view = AdUnifiedBannerLoadingBinding.inflate(LayoutInflater.from(applicationContext), null, false)
    view.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    params.height = height
    container.layoutParams = params
    container.addView(view.root)
    val adView = AdView(this)
    adView.setAdSize(adSize)
    adView.adUnitId = BANNER_ID
    adView.adListener = object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            if (container.childCount > 0) container.removeAllViews()
            container.addView(adView)
        }
    }
    val extras = Bundle()
    extras.putString("collapsible", "bottom")
    adView.loadAd(
        AdRequest.Builder().addNetworkExtrasBundle(
            com.google.ads.mediation.admob.AdMobAdapter::class.java,
            extras
        ).build()
    )
    container.addView(adView)
}

private fun Activity.getAdSize(): AdSize {
    val display = this.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val widthPixels = outMetrics.widthPixels.toFloat()
    val density = outMetrics.density
    val adWidth = (widthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}