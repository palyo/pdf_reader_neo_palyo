package coder.apps.aftercall.startup

import android.content.Context
import android.os.Handler
import android.os.Looper
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.STARTUP
import android.webkit.WebView
import androidx.startup.Initializer

class WebViewInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        preloadWebView(context)
    }

    private fun preloadWebView(context: Context) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                WebView(context.applicationContext).destroy()
                AfterCallLog.d(STARTUP, "WebView preloaded")
            } catch (e: Exception) {
                AfterCallLog.w(STARTUP, "WebView preload failed", e)
            }
            return
        }

        Handler(Looper.getMainLooper()).post {
            try {
                WebView(context.applicationContext).destroy()
                AfterCallLog.d(STARTUP, "WebView preloaded (handler)")
            } catch (e: Exception) {
                AfterCallLog.w(STARTUP, "WebView preload failed", e)
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}