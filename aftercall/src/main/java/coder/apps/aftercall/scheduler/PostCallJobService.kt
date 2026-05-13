package coder.apps.aftercall.scheduler

import android.app.job.JobParameters
import android.app.job.JobService

class PostCallJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        try {
            PostCallJobScheduler.INSTANCE.startPostCall(applicationContext)
        } catch (_: Exception) {
        }
        return false
    }
}
