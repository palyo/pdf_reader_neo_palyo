package coder.apps.aftercall.scheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.SCHEDULER
import androidx.core.content.ContextCompat
import android.Manifest

class PostCallJobScheduler private constructor() {
    fun startPostCall(context: Context) {
        val checkCallPhonePermission = hasPermission(context)
        AfterCallLog.d(SCHEDULER, "startPostCall: hasPermission=$checkCallPhonePermission")
        if (hasPermission(context)) {
            val systemService = context.getSystemService(Context.JOB_SCHEDULER_SERVICE)

            val jobScheduler = systemService as JobScheduler
            val persistableBundle = PersistableBundle()
            persistableBundle.putInt("job_scheduler_source", 1)
            val builder =
                JobInfo.Builder(666, ComponentName(context, PostCallJobService::class.java))
                    .setExtras(persistableBundle)
                    .setMinimumLatency(0L)
            if (Build.VERSION.SDK_INT >= 26) {
                builder.setRequiresBatteryNotLow(true)
            }
            if (jobScheduler.allPendingJobs.size > 50) {
                val it = jobScheduler.allPendingJobs.iterator()
                while (it.hasNext()) {
                    AfterCallLog.d(SCHEDULER, "pending job: ${it.next()}")
                }
                jobScheduler.cancelAll()
            }
            if (Build.VERSION.SDK_INT >= 24) {
                if (jobScheduler.getPendingJob(666) != null) {
                    jobScheduler.cancel(666)
                }
            }
            try {
                jobScheduler.schedule(builder.build())
            } catch (e: IllegalArgumentException) {
                AfterCallLog.e(SCHEDULER, "job schedule failed", e)
            }
            AfterCallLog.d(SCHEDULER, "job scheduled")
        }
    }

    fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == 0
    }

    companion object {
        @JvmField
        val INSTANCE: PostCallJobScheduler = PostCallJobScheduler()
    }
}
