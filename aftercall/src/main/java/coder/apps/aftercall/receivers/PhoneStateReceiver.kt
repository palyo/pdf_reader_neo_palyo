package coder.apps.aftercall.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.PHONE
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coder.apps.aftercall.R
import coder.apps.aftercall.scheduler.PostCallJobScheduler
import coder.apps.aftercall.extensions.CALL_COUNTER
import coder.apps.aftercall.extensions.CALL_TIME
import coder.apps.aftercall.extensions.CALL_TYPE
import coder.apps.aftercall.extensions.END_TIME
import coder.apps.aftercall.extensions.EXTRA_MOBILE_NUMBER
import coder.apps.aftercall.extensions.IS_OPEN_FROM_NOTIFICATION
import coder.apps.aftercall.extensions.START_TIME
import coder.apps.aftercall.extensions.callCounter
import coder.apps.aftercall.extensions.prefs_call_incoming
import coder.apps.aftercall.extensions.prefs_call_outgoing
import coder.apps.aftercall.extensions.prefs_call_state
import coder.apps.aftercall.extensions.prefs_start_call_timer
import coder.apps.aftercall.services.PostCallForegroundService
import coder.apps.aftercall.ui.PostCallActivity
import java.util.Date

class PhoneStateReceiver : PhoneCallStateReceiver() {
    override fun onIncomingCallReceived(context: Context, str: String?, date: Date?) {
        AfterCallLog.i(PHONE, "incoming call received")
    }

    override fun onIncomingCallAnswered(context: Context, str: String?, date: Date?) {
        AfterCallLog.i(PHONE, "incoming call answered (at=$date)")
    }

    override fun onIncomingCallEnded(context: Context, str: String?, date: Date?, date2: Date?) {
        AfterCallLog.i(PHONE, "incoming call ended")
        openNewActivity(context, str, date, date2, context.getString(R.string.label_incoming_call))
    }

    override fun onOutgoingCallStarted(ctx: Context, str: String?, date: Date?) {
        AfterCallLog.i(PHONE, "outgoing call started (at=$date)")
    }

    override fun onOutgoingCallEnded(context: Context, str: String?, date: Date?, date2: Date?) {
        AfterCallLog.i(PHONE, "outgoing call ended")
        openNewActivity(context, str, date, date2, context.getString(R.string.label_outgoing_call))
    }

    override fun onMissedCall(context: Context, str: String?, date: Date?) {
        AfterCallLog.i(PHONE, "missed call")
        openNewActivity(context, str, date, Date(), context.getString(R.string.label_missed_call))
    }

    private fun openNewActivity(context: Context, str: String?, date: Date?, date2: Date?, callType: String) {
        val phoneNumber = if (str.isNullOrEmpty()) "" else str
        val checkPermission = checkPermission()
        val isEnablePostCallScreen = isEnablePostCallScreen()
        val isShowPostCallScreen = isShowPostCallScreen()
        AfterCallLog.d(
            PHONE,
            "openPostCallActivity: hasPermission=$checkPermission enabled=$isEnablePostCallScreen showFlag=$isShowPostCallScreen number=$phoneNumber start=${date != null} end=${date2 != null} type=$callType"
        )

        PostCallActivity.getPostCallActivity()?.finishActivity()
        setCallEnded(true)
        context.let { stopService(it) }

        if (isEnablePostCallScreen() && isShowPostCallScreen()) {
            val callCounter = context.callCounter
            val intent = Intent(context, PostCallActivity::class.java).apply {
                putExtra(EXTRA_MOBILE_NUMBER, phoneNumber)
                putExtra(CALL_TIME, date)
                putExtra(START_TIME, date!!.time)
                putExtra(END_TIME, date2!!.time)
                putExtra(CALL_TYPE, callType)
                putExtra(CALL_COUNTER, callCounter)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.prefs_call_state = callCounter + 1
            context.prefs_start_call_timer = 0L
            context.prefs_call_state = -1
            context.prefs_call_incoming = false
            context.prefs_call_outgoing = false

            if (checkPermission()) {
                intent.putExtra(IS_OPEN_FROM_NOTIFICATION, false)
                if (PostCallForegroundService.myServiceIsRunning) {
                    context.startActivity(intent)
                } else {
                    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    val dummyView = View(context).apply { setBackgroundColor(0) }
                    val layoutType = if (Build.VERSION.SDK_INT >= 26) 2038 else 2002
                    windowManager.addView(dummyView, WindowManager.LayoutParams(1, 1, layoutType, 24, -3))
                    Handler(Looper.getMainLooper()).postDelayed({
                        context.startActivity(intent)
                        windowManager.removeView(dummyView)
                    }, 200L)
                }
            } else {
                intent.putExtra(IS_OPEN_FROM_NOTIFICATION, true)
                showPostCallNotification(context, intent)
                setIncomingCall(false)
                setOutgoingCall(false)
                setLastState(-1)
                PostCallJobScheduler.INSTANCE.startPostCall(context)
                return
            }
        }
        setIncomingCall(false)
        setOutgoingCall(false)
        setLastState(-1)
    }

    private fun showPostCallNotification(context: Context, intent: Intent) {
        AfterCallLog.d(PHONE, "showPostCallNotification")
        try {
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == 0) {
                NotificationManagerCompat.from(context).cancelAll()
            }
        } catch (ignored: Exception) {}

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 67108866)
        } else {
            PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 134217728)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        val builder = NotificationCompat.Builder(context, "PostCall")
            .setContentTitle("See call information")
            .setSmallIcon(R.drawable.post_ic_notification_phone_call)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(2)
            .setWhen(System.currentTimeMillis())
            .setDefaults(1)
            .setAutoCancel(true)
            .setVibrate(LongArray(0))

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("PostCall")
            val channel = NotificationChannel("PostCall", "PostCall", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifications."
                setLockscreenVisibility(1)
                enableVibration(true)
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != 0) return

        if (isFullScreenGranted(context)) {
            builder.setFullScreenIntent(pendingIntent, true)
        } else {
            builder.setContentIntent(pendingIntent)
        }
        AfterCallLog.d(PHONE, "fullScreenIntent granted=${isFullScreenGranted(context)}")
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun isFullScreenGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= 34) {
            return (context.getSystemService(NotificationManager::class.java)).canUseFullScreenIntent()
        }
        return true
    }

    companion object {
        private const val CHANNEL_ID_POST_CALL = "PostCall"
    }
}
