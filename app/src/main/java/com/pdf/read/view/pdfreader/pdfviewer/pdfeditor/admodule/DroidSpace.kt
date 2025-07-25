package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.app.*
import android.content.*
import android.content.pm.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*
import retrofit2.*

var OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
var INTER_ID = "ca-app-pub-3940256099942544/1033173712"
var NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
var BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
var Context.appOpenCount: Int
    get() = TinyDB(this).getInt("appOpenCount", 0)
    set(value) {
        TinyDB(this).putInt("appOpenCount", value)
    }
var Context.currentAdLevel: Int
    get() = TinyDB(this).getInt("currentAdLevel", 0)
    set(value) {
        TinyDB(this).putInt("currentAdLevel", value)
    }

fun getAdsKey1(): String {
    return "Pdf%20Reader_Pdf%20Viewer_sai/ad_manager.json"
}

fun Activity.init(callback: () -> Unit) {
    val apiService = ApiClient(this).client?.create(ApiService::class.java)
    val google = getAdsKey1()
    val call = apiService?.getConfig(google)
    call?.enqueue(object : Callback<ConfigJson> {
        override fun onResponse(call: Call<ConfigJson>, response: Response<ConfigJson>) {
            if (response.isSuccessful) {
                response.body()?.let { habitJson ->
                    setAppJson(habitJson)
                    callback.invoke()
                }
            } else {
                val recorderJson = appJson()
                recorderJson?.let { initAds(it) }
                callback.invoke()
            }
        }

        override fun onFailure(call: Call<ConfigJson>, t: Throwable) {
            val recorderJson = appJson()
            recorderJson?.let { initAds(it) }
            callback.invoke()
        }
    })
}

fun Activity.setAppJson(appJson: ConfigJson) {
    TinyDB(this).putObject("appJson", appJson)
    initAds(appJson)
}

fun Context.appJson(): ConfigJson? {
    return TinyDB(this).getObject("appJson", ConfigJson::class.java)
}

fun Context.getPolicyLink(): String {
    return appJson()?.policyUrl ?: "https://privacy-and-policy-online.blogspot.com/2021/10/privacy-policy.html"
}

fun Context.isStartFlowRepeat(): Boolean {
    return appJson()?.isStartFlowRepeat == true
}

fun Activity.initAds(appJson: ConfigJson) {
    NATIVE_ID = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/2247696110" else appJson.nativeId ?: "ca-app-pub-3940256099942544/2247696110"
    INTER_ID = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else appJson.interId ?: "ca-app-pub-3940256099942544/1033173712"
    OPEN_ID = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else appJson.openId ?: "ca-app-pub-3940256099942544/9257395921"
    BANNER_ID = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else appJson.bannerId ?: "ca-app-pub-3940256099942544/9214589741"
    registerAppId(appJson.appId ?: "ca-app-pub-3940256099942544~3347511713")
}

fun Activity.registerAppId(appId: String) {
    try {
        val ai: ApplicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", appId)
    } catch (e: PackageManager.NameNotFoundException) {
    } catch (e: NullPointerException) {
    }
}