package coder.apps.aftercall.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import coder.apps.aftercall.scheduler.PostCallJobScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            PostCallJobScheduler.INSTANCE.startPostCall(context)
        }
    }
}
