package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.app.*
import android.content.*
import android.content.pm.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*

// ─────────────────────────────────────────────────────────────────────────────
// Ad unit configuration
//
// Previously these values were fetched at runtime from a JSON file hosted on
// S3 via Retrofit. The JSON has been inlined here as compile-time constants —
// removes the network round-trip, the parser, the dependency on Retrofit /
// OkHttp / RxJava, and the brittle Activity.init(callback) async flow.
//
// Debug builds (BuildConfig.DEBUG == true) use Google's official test ad unit
// IDs so we don't accidentally serve production ads from emulators / dev
// devices and risk policy violations. Release builds use the real production
// IDs that were previously served from the remote ad_manager.json.
// ─────────────────────────────────────────────────────────────────────────────

// ── Production ad unit IDs (matches the previously remote ad_manager.json) ──
private const val PROD_APP_ID = "ca-app-pub-4852962457779682~7914510420"
private const val PROD_OPEN_ID = "ca-app-pub-4852962457779682/1300762933"
private const val PROD_INTER_ID = "ca-app-pub-4852962457779682/7251093613"
private const val PROD_NATIVE_ID = "ca-app-pub-4852962457779682/8305154982"
private const val PROD_BANNER_ID = "ca-app-pub-4852962457779682/3926926272"
private const val PROD_REWARD_ID = "ca-app-pub-4852962457779682/2738490118"

// ── Test ad unit IDs (Google's official samples, safe for dev / CI builds) ──
private const val DEBUG_APP_ID = "ca-app-pub-3940256099942544~3347511713"
private const val DEBUG_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
private const val DEBUG_INTER_ID = "ca-app-pub-3940256099942544/1033173712"
private const val DEBUG_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
private const val DEBUG_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
private const val DEBUG_REWARD_ID = "ca-app-pub-3940256099942544/5224354917"

// Kept as `var` (not `val`) only to preserve the original public surface —
// no caller writes to these anymore.
var OPEN_ID: String = if (BuildConfig.DEBUG) DEBUG_OPEN_ID else PROD_OPEN_ID
var INTER_ID: String = if (BuildConfig.DEBUG) DEBUG_INTER_ID else PROD_INTER_ID
var NATIVE_ID: String = if (BuildConfig.DEBUG) DEBUG_NATIVE_ID else PROD_NATIVE_ID
var BANNER_ID: String = if (BuildConfig.DEBUG) DEBUG_BANNER_ID else PROD_BANNER_ID
var REWARD_AD_UNIT_ID: String = if (BuildConfig.DEBUG) DEBUG_REWARD_ID else PROD_REWARD_ID

// ─────────────────────────────────────────────────────────────────────────────
// Other config values previously served by ad_manager.json
// ─────────────────────────────────────────────────────────────────────────────

/** Privacy policy URL shown in the consent flow + onboarding terms link. */
private const val POLICY_URL = "https://sites.google.com/view/pdfreader-byaanibrothersinfo/home"

/** When true, the language picker re-fires periodically on cold-start. */
private const val START_FLOW_REPEAT = false

fun getPolicyLink(): String = POLICY_URL

fun Context.isStartFlowRepeat(): Boolean = START_FLOW_REPEAT

// ─────────────────────────────────────────────────────────────────────────────
// Activity bootstrap
//
// Original implementation: hit S3, parse JSON, write to TinyDB, then invoke
// the callback. Now everything is local — we just wire the runtime AdMob app
// ID and continue. The signature `init { … }` is preserved so the existing
// MainActivity call site (`init { requestConsentForm() }`) compiles unchanged.
// ─────────────────────────────────────────────────────────────────────────────

fun Activity.init(callback: () -> Unit) {
    registerAppId(if (BuildConfig.DEBUG) DEBUG_APP_ID else PROD_APP_ID)
    callback.invoke()
}

/**
 * Replaces the AdMob `APPLICATION_ID` meta-data at runtime. The manifest ships
 * the debug app-id by default; release builds overwrite it with the production
 * id so live revenue gets credited to the right account.
 */
fun Activity.registerAppId(appId: String) {
    try {
        val ai: ApplicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", appId)
    } catch (_: PackageManager.NameNotFoundException) {
    } catch (_: NullPointerException) {
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Misc per-user state (kept as-is)
// ─────────────────────────────────────────────────────────────────────────────

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

var Context.isPremium: Boolean
    get() = TinyDB(this).getBoolean("isPremium", false)
    set(value) {
        TinyDB(this).putBoolean("isPremium", value)
    }
