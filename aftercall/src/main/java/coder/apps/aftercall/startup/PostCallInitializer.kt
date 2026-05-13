package coder.apps.aftercall.startup

import android.content.Context
import androidx.startup.Initializer
import coder.apps.aftercall.scheduler.PostCallJobScheduler

class PostCallInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        PostCallJobScheduler.INSTANCE.startPostCall(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}