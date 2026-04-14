package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.app.Activity
import android.content.Context
import android.os.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*
import com.google.android.gms.ads.rewarded.RewardItem

private var rewardedAd: RewardedAd? = null
private var isLoadingRewarded = false

fun Context.loadRewardAd(listener: ((isFailed: Boolean) -> Unit)? = null) {
    if (isLoadingRewarded || isPremium) return

    isLoadingRewarded = true
    val adRequest = AdRequest.Builder().build()

    RewardedAd.load(this, REWARD_AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            isLoadingRewarded = false
            rewardedAd = null
            listener?.invoke(true)
        }

        override fun onAdLoaded(ad: RewardedAd) {
            isLoadingRewarded = false
            rewardedAd = ad
            listener?.invoke(false)
        }
    })
}

fun Activity.viewRewardAd(
    onClosed: (Boolean) -> Unit = {},
    onFailed: () -> Unit = {},
) {
    if (isPremium) {
        onClosed(true)
        return
    }

    if (rewardedAd != null) {
        showRewardAd(onClosed, onFailed)
    } else {
        val dialog = showProgressDialog(this)
        loadRewardAd()

        object : CountDownTimer(5000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                if (rewardedAd != null) {
                    cancel()
                    onFinish()
                }
            }

            override fun onFinish() {
                try {
                    if (!isFinishing && !isDestroyed && dialog.isShowing) {
                        dialog.dismiss()
                    }
                } catch (_: Exception) {
                }

                if (rewardedAd != null) {
                    showRewardAd(onClosed, onFailed)
                } else {
                    onFailed()
                }
            }
        }.start()
    }
}

private fun Activity.showRewardAd(
    onClosed: (Boolean) -> Unit,
    onFailed: () -> Unit,
) {

    var isAdCompleted = false
    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            onClosed(isAdCompleted)
            rewardedAd = null
            loadRewardAd()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            onFailed()
            rewardedAd = null
            loadRewardAd()
        }

        override fun onAdShowedFullScreenContent() {}
    }

    rewardedAd?.show(this) { rewardItem ->
        isAdCompleted = true
    }
}

