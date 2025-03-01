package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.content.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.*

object PreloadNewNative {

    private const val TAG = "PreloadNewNative"
    val nativeHashMap: MutableMap<String, MutableList<NativeAd>> = mutableMapOf()

    fun Context.loadNativeAd(key: String, ids: MutableList<String>, preloadCount: Int? = 1) {
        TinyDB(this).putInt("preload_$key", 0)
        val natives = nativeHashMap[key] ?: mutableListOf()
        if (natives.size >= (preloadCount ?: 1)) {
            return
        } else if (natives.size > 0) {
            loadPreloadNativeObject(key = key, ids = ids)
            return
        }
        TinyDB(this).putInt("preload_$key", TinyDB(this).getInt("preload_$key", 0) + (preloadCount ?: 1))
        fetchPreloadNativeObject(key = key, ids = ids, listener = { nativeAd: NativeAd?, b: Boolean ->
            if (!b)
                nativeAd?.apply {
                    natives.add(this)
                    nativeHashMap[key] = natives
                }
        }, preloadCount = preloadCount ?: 1)
    }

    fun Context.loadPreloadNativeObject(key: String, ids: MutableList<String>, preloadCount: Int? = 1) {
        val natives = nativeHashMap[key] ?: mutableListOf()
        TinyDB(this).putInt("preload_$key", TinyDB(this).getInt("preload_$key", 0) + 1)
        fetchPreloadNativeObject(key = key, ids = ids, listener = { nativeAd: NativeAd?, b: Boolean ->
            if (b) {
                if (TinyDB(this@loadPreloadNativeObject).getInt("preload_$key", 0) > 0) {
                    TinyDB(this@loadPreloadNativeObject).putInt("preload_$key", TinyDB(this@loadPreloadNativeObject).getInt("preload_$key", 0) - 1)
                }
            } else {
                nativeAd?.apply {
                    natives.add(this)
                    nativeHashMap[key] = natives
                }
            }
        }, preloadCount = preloadCount ?: 1)
    }

    fun Context.fetchPreloadNativeObject(key: String, index: Int? = 0, ids: MutableList<String>, listener: ((NativeAd?, Boolean) -> Unit)? = null, preloadCount: Int) {
        if ((ids.size - 1) < (index ?: 0)) {
            TAG.log("$key: onAdFailed()")
            listener?.invoke(null, true)
            return
        }
        val natives = nativeHashMap[key] ?: mutableListOf()
        val adLoader = AdLoader.Builder(this, ids[index ?: 0])
                .forNativeAd { ad: NativeAd ->
                    listener?.invoke(ad, false)
                    TAG.log("$key: onAdLoaded() ${ids[index ?: 0]}")
                    if (preloadCount > natives.size && TinyDB(this@fetchPreloadNativeObject).getInt("preload_$key", 0) < preloadCount) {
                        fetchPreloadNativeObject(key = key, index = (index ?: 0) + 1, ids = ids, listener = listener, preloadCount)
                    }
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        TAG.log("$key: onAdFailedToLoad() ${ids[index ?: 0]} ${adError.code}")
                        fetchPreloadNativeObject(key = key, index = (index ?: 0) + 1, ids = ids, listener = listener, preloadCount)
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().setMediaAspectRatio(MediaAspectRatio.LANDSCAPE).setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build()).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun Context.isNativePreloadAvailable(key: String, ids: MutableList<String>, preloadCount: Int? = 1): Boolean {
        val natives = nativeHashMap[key] ?: mutableListOf()
        return if (natives.size > 0) {
            true
        } else {
            if ((preloadCount ?: 0) > 0 && TinyDB(this).getInt("preload_$key", 0) < (preloadCount ?: 0)) {
                loadPreloadNativeObject(key, ids)
            }
            false
        }
    }

    fun Context.fetchNativePreloadAd(key: String, ids: MutableList<String>, preloadCount: Int? = 1): NativeAd? {
        val natives = nativeHashMap[key] ?: mutableListOf()
        return if (natives.size > 0) {
            val native = natives[0]
            natives.removeAt(0)
            if (TinyDB(this@fetchNativePreloadAd).getInt("preload_$key", 0) > 0) {
                TinyDB(this@fetchNativePreloadAd).putInt("preload_$key", TinyDB(this@fetchNativePreloadAd).getInt("preload_$key", 0) - 1)
            }
            if (natives.size < (preloadCount ?: 0) && TinyDB(this@fetchNativePreloadAd).getInt("preload_$key", 0) < (preloadCount ?: 0)) {
                loadPreloadNativeObject(key, ids)
            }
            native
        } else {
            null
        }
    }
}