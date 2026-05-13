package coder.apps.aftercall.core

import android.util.Log
import coder.apps.aftercall.BuildConfig

/**
 * Centralized logger for the after-call module.
 *
 * Tags are namespaced as `AfterCall/<area>` so they can be filtered with
 * `adb logcat -s AfterCall/Activity` or with a regex `AfterCall/.*` in
 * Logcat. Debug/info/warn calls are no-ops in release builds. Errors are
 * always emitted so crash reporters can pick them up.
 */
internal object AfterCallLog {

    private const val PREFIX = "AfterCall"

    /** Verbose flow trace (e.g. each lifecycle step). Off in release. */
    fun d(area: String, msg: String) {
        if (BuildConfig.DEBUG) Log.d(tag(area), msg)
    }

    /** Notable state transition (e.g. "ad loaded", "call ended"). Off in release. */
    fun i(area: String, msg: String) {
        if (BuildConfig.DEBUG) Log.i(tag(area), msg)
    }

    /** Recoverable problem (e.g. ad load failed, fallback path engaged). */
    fun w(area: String, msg: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            if (throwable == null) Log.w(tag(area), msg) else Log.w(tag(area), msg, throwable)
        }
    }

    /** Error with optional throwable. Always emitted. */
    fun e(area: String, msg: String, throwable: Throwable? = null) {
        if (throwable == null) Log.e(tag(area), msg) else Log.e(tag(area), msg, throwable)
    }

    private fun tag(area: String) = "$PREFIX/$area"

    /** Standard log areas — kept here so the set is closed and consistent. */
    object Area {
        const val ACTIVITY   = "Activity"
        const val BANNER     = "BannerAd"
        const val NATIVE     = "NativeAd"
        const val CALL_STATE = "CallState"
        const val PHONE      = "PhoneReceiver"
        const val SERVICE    = "Service"
        const val SCHEDULER  = "Scheduler"
        const val STARTUP    = "Startup"
        const val WIDGET     = "Widget"
        const val WATCHER    = "HomeWatcher"
    }
}
