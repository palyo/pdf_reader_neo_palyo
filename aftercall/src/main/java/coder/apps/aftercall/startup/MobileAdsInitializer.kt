package coder.apps.aftercall.startup

import android.content.Context
import android.os.Handler
import android.os.Looper
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.STARTUP
import androidx.startup.Initializer
import com.google.android.gms.ads.MobileAds

class MobileAdsInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Handler(Looper.getMainLooper()).post {
            MobileAds.initialize(context)
            AfterCallLog.i(STARTUP, "MobileAds initialized")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WebViewInitializer::class.java)
    }
}