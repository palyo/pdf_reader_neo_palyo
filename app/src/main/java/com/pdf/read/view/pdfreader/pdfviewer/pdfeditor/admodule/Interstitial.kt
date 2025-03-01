package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.app.*
import android.content.*
import android.os.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.*

private const val TAG = "AdmobInterstitial"
private var admobInterstitialAd: InterstitialAd? = null
fun Context.loadInterAd(listener: ((result: Boolean) -> Unit)? = null) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(this, INTER_ID, adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                admobInterstitialAd = null
                listener?.invoke(true)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                admobInterstitialAd = interstitialAd
                listener?.invoke(false)
            }
        })
}

private fun Activity.displayInter(listener: ((result: Boolean) -> Unit)? = null) {
    if (!isFinishing && !isDestroyed) {
        admobInterstitialAd?.show(this)
        admobInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
            }

            override fun onAdDismissedFullScreenContent() {
                listener?.invoke(false)
                admobInterstitialAd = null
                loadInterAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                listener?.invoke(true)
                admobInterstitialAd = null
                loadInterAd()
            }

            override fun onAdImpression() {

            }

            override fun onAdShowedFullScreenContent() {

            }
        }
    }
}

fun Activity.viewInterAdForce(listener: ((result: Boolean) -> Unit)? = null) {
    if (admobInterstitialAd != null) {
        displayInter(listener)
    } else {
        loadInterAd()
        object : CountDownTimer((5 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (admobInterstitialAd != null) {
                    this.cancel()
                    this.onFinish()
                }
            }

            override fun onFinish() {
                if (admobInterstitialAd != null) {
                    displayInter(listener)
                } else {
                    listener?.invoke(true)
                    loadInterAd()
                }
            }
        }.start()
    }
}

fun Activity.viewInterAd(listener: ((result: Boolean) -> Unit)? = null) {
    if (admobInterstitialAd != null) {
        displayInter(listener)
    } else {
        loadInterAd()
    }
}

fun Activity.viewInterAdWait(listener: ((result: Boolean) -> Unit)? = null) {
    val dialog = showProgressDialog(this)
    if (admobInterstitialAd != null) {
        Handler(mainLooper).postDelayed({
            displayInter(listener)
            Handler(mainLooper).postDelayed({
                try {
                    if (!isFinishing && !isDestroyed && dialog.isShowing) {
                        dialog.dismiss()
                    }
                } catch (_: Exception) {
                }
            }, 1000)
        }, 2000)
    } else {
        loadInterAd()
        object : CountDownTimer((5 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (admobInterstitialAd != null) {
                    this.cancel()
                    this.onFinish()
                }
            }

            override fun onFinish() {
                try {
                    if (!isFinishing && !isDestroyed && dialog.isShowing) {
                        dialog.dismiss()
                    }
                } catch (_: Exception) {
                }
                if (admobInterstitialAd != null) {
                    displayInter(listener)
                } else {
                    listener?.invoke(true)
                    loadInterAd()
                }
            }
        }.start()
    }
}