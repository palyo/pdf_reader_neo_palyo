package coder.apps.aftercall.receivers

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.NotificationManagerCompat
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.CALL_STATE
import androidx.core.content.ContextCompat
import coder.apps.aftercall.ads.PostCallAdState.Companion.adBannerExpirationTime
import coder.apps.aftercall.ads.PostCallAdState.Companion.adBannerLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.adNativeExpirationTime
import coder.apps.aftercall.ads.PostCallAdState.Companion.adNativeLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdViewWidget
import coder.apps.aftercall.ads.PostCallAdState.Companion.isBannerAdLoading
import coder.apps.aftercall.ads.PostCallAdState.Companion.isCallingStart
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeFailedGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativePostCallLoadingValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isStartedService
import coder.apps.aftercall.ads.PostCallAdState.Companion.nativeAdsPostCallAd
import coder.apps.aftercall.ads.PostCallAdState.Companion.setOpenAdHide
import coder.apps.aftercall.ads.PostCallBannerAd
import coder.apps.aftercall.ads.PostCallNativeAd
import coder.apps.aftercall.extensions.callCounter
import coder.apps.aftercall.extensions.enablePostCallScreen
import coder.apps.aftercall.extensions.isBannerLoad
import coder.apps.aftercall.extensions.isNativeLoad
import coder.apps.aftercall.extensions.prefs_call_incoming
import coder.apps.aftercall.extensions.prefs_call_outgoing
import coder.apps.aftercall.extensions.prefs_call_state
import coder.apps.aftercall.extensions.prefs_start_call_timer
import coder.apps.aftercall.services.PostCallForegroundService
import coder.apps.aftercall.ui.PostCallActivity
import coder.apps.aftercall.ui.widget.CallerWidgetWindow
import java.util.Date

abstract class PhoneCallStateReceiver : BroadcastReceiver() {

    private var isEnablePostCallScreen: Boolean = false
    private var isOverlyPermision: Boolean = false
    private var isShowPostCallScreen: Boolean = false

    companion object {
        private var callStartTime: Date? = null
        var isCallEnded: Boolean = false
            private set
        private var isIncomingCall: Boolean = false
        private var isOutgoingCall: Boolean = false
        var isVoiceOptionEnabled: Boolean = false
        private var savedNumber: String? = null
        private var windowView: CallerWidgetWindow? = null
        private var lastState: Int = -1

        fun setLastState(i: Int) {
            lastState = i
        }

        fun setIncomingCall(z: Boolean) {
            isIncomingCall = z
        }

        fun setOutgoingCall(z: Boolean) {
            isOutgoingCall = z
        }

        fun setCallEnded(z: Boolean) {
            isCallEnded = z
        }
    }

    fun isEnablePostCallScreen(): Boolean = isEnablePostCallScreen
    fun isShowPostCallScreen(): Boolean = isShowPostCallScreen

    protected open fun onIncomingCallAnswered(context: Context, str: String?, date: Date?) {}
    protected open fun onIncomingCallEnded(
        context: Context,
        str: String?,
        date: Date?,
        date2: Date?
    ) {
    }

    protected open fun onIncomingCallReceived(context: Context, str: String?, date: Date?) {}
    protected open fun onIncomingCallStarted(ctx: Context, str: String?, date: Date?) {}
    protected open fun onMissedCall(context: Context, str: String?, date: Date?) {}
    protected open fun onOutgoingCallEnded(
        context: Context,
        str: String?,
        date: Date?,
        date2: Date?
    ) {
    }

    protected open fun onOutgoingCallStarted(ctx: Context, str: String?, date: Date?) {}

    override fun onReceive(context: Context, intent: Intent) {
        AfterCallLog.d(CALL_STATE, "onReceive")
        var callState = 1
        isEnablePostCallScreen = true
        isShowPostCallScreen = context.enablePostCallScreen
        isOverlyPermision = hasOverlayPermission(context)

        if ("android.intent.action.NEW_OUTGOING_CALL" == intent.action) {
            val number = intent.extras?.getString("android.intent.extra.PHONE_NUMBER")
            savedNumber = number
            AfterCallLog.d(CALL_STATE, "savedNumber=$number")
            return
        }

        AfterCallLog.d(CALL_STATE, "extras=${intent.extras != null}")
        val extras = intent.extras ?: return
        val state = extras.getString("state")
        val incomingNumber = extras.getString("incoming_number")

        if (!TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) callState = 2
            val ramInfo = getRamInfoInMB(context)
            val totalRam = ramInfo.first
            val freeRam = ramInfo.second
            if (freeRam < 1500 && totalRam > 2000) {
                AfterCallLog.d(CALL_STATE, "onCallStateChanged dispatched")
                onCallStateChanged(context, callState, incomingNumber)
            } else {
                onCallStateChanged(context, callState, incomingNumber)
                AfterCallLog.w(CALL_STATE, "service not started: device low on RAM")
            }
        } else {
            callState = 0
            val freeRam = getRamInfoInMB(context).second
            if (freeRam < 1500) {
                onCallStateManageWithoutService(context, callState, incomingNumber)
                AfterCallLog.w(CALL_STATE, "service not started: device low on RAM")
            }
        }
    }

    private fun getRamInfoInMB(context: Context): Pair<Long, Long> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return Pair(0L, 0L)
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val gb = 1024L * 1024L * 1024L
        val totalGB = memoryInfo.totalMem / gb
        val availGB = memoryInfo.availMem / gb
        AfterCallLog.d(CALL_STATE, "ram avail=${availGB}GB total=${totalGB}GB")
        return Pair(totalGB, availGB)
    }

    fun onCallStateManageWithoutService(context: Context, callState: Int, str: String?) {
        AfterCallLog.i(CALL_STATE, "manage: state=$callState")
        if (lastState == callState) {
            AfterCallLog.d(CALL_STATE, "manage: state unchanged — ignoring")
            return
        }
        when (callState) {
            0 -> {
                AfterCallLog.d(CALL_STATE, "manage: callEnded — startTimeNull=${callStartTime == null}")
                if (callStartTime == null) {
                    gettingTimeFromPref(context)
                    lastState = getPrefState(context)
                    isOutgoingCall = getOutgoingCall(context)
                    isIncomingCall = getIncomingCall(context)
                }
                if (lastState == 1) {
                    if (isIncomingCall) onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                    else onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                } else {
                    if (isIncomingCall) onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                    else onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
            }

            1 -> {
                AfterCallLog.d(CALL_STATE, "manage: RINGING outgoing=$isOutgoingCall incoming=$isIncomingCall")
                PostCallActivity.getPostCallActivity()?.finishActivity()
                disabledOpenAds()
                if (!isOutgoingCall) {
                    isIncomingCall = true
                    val date = Date()
                    callStartTime = date
                    saveStartTime(context, date)
                    saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                    savedNumber = str
                }
            }

            2 -> {
                AfterCallLog.d(
                    CALL_STATE,
                    "manage: OFFHOOK lastState=$lastState startTimeNull=${callStartTime == null} outgoing=$isOutgoingCall incoming=$isIncomingCall"
                )
                if (callStartTime == null) {
                    lastState = getPrefState(context)
                    isOutgoingCall = getOutgoingCall(context)
                    isIncomingCall = getIncomingCall(context)
                }
                if (lastState != 1) {
                    PostCallActivity.getPostCallActivity()?.finishActivity()
                    disabledOpenAds()
                    if (!isOutgoingCall && !isIncomingCall) {
                        isOutgoingCall = true
                        isIncomingCall = false
                        val date2 = Date()
                        callStartTime = date2
                        saveStartTime(context, date2)
                        saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                    }
                } else if (!isOutgoingCall) {
                    isIncomingCall = true
                    callStartTime = Date()
                    saveStartTime(context, callStartTime!!)
                    saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                    onIncomingCallAnswered(context, savedNumber, callStartTime)
                }
            }
        }
        lastState = callState
    }

    private fun gettingTimeFromPref(context: Context) {
        val prefStartTime = context.prefs_start_call_timer
        callStartTime = if (prefStartTime != 0L) Date(prefStartTime) else {
            val date = Date()
            saveStartTime(context, date)
            date
        }
    }

    private fun saveStartTime(context: Context, date: Date?) {
        date?.let { saveStartTimePref(context, it.time) }
    }

    fun saveStartTimePref(context: Context, j: Long) {
        context.prefs_start_call_timer = j
    }

    fun startService(context: Context) {
        AfterCallLog.d(CALL_STATE, "startService dispatched")
        if (isCallingStart) return
        isStartedService = false
        if (checkNotificationPermission(context)) {
            isCallingStart = true
            try {
                context.startService(Intent(context, PostCallForegroundService::class.java))
                isStartedService = true
            } catch (unused: Exception) {
                if (Build.VERSION.SDK_INT >= 26) {
                    try {
                        context.applicationContext.startForegroundService(
                            Intent(
                                context.applicationContext,
                                PostCallForegroundService::class.java
                            )
                        )
                        isStartedService = true
                    } catch (e: Exception) {
                        AfterCallLog.w(CALL_STATE, "startService failed: ${e.message}", e)
                        isStartedService = false
                    }
                }
            }
        } else {
            isStartedService = false
        }
        if (isStartedService || !hasOverlayPermission(context)) return
        isCallingStart = true
        showOverlay(context)
    }

    private fun saveCallState(context: Context, i: Int, z: Boolean, z2: Boolean) {
        context.prefs_call_state = i
        context.prefs_call_incoming = z
        context.prefs_call_outgoing = z2
    }

    private fun getPrefState(context: Context): Int = context.prefs_call_state
    private fun getIncomingCall(context: Context): Boolean = context.prefs_call_incoming
    private fun getOutgoingCall(context: Context): Boolean = context.prefs_call_outgoing

    private fun checkNotificationPermission(context: Context): Boolean {
        val z = try {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } catch (unused: Exception) {
            false
        }
        AfterCallLog.d(CALL_STATE, "notificationsEnabled=$z")
        if (Build.VERSION.SDK_INT < 26 || z) {
            return Build.VERSION.SDK_INT < 33 ||
                    ContextCompat.checkSelfPermission(
                        context,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == 0
        }
        return false
    }

    fun stopService(context: Context) {
        if (!isStartedService) removeOverlay()
        try {
            if (isCallingStart) {
                isCallingStart = false
                if (isStartedService) {
                    isStartedService = false
                    context.applicationContext.stopService(
                        Intent(
                            context.applicationContext,
                            PostCallForegroundService::class.java
                        )
                    )
                }
            }
        } catch (ignored: Exception) {
        }
    }

    fun onCallStateChanged(context: Context, callState: Int, str: String?) {
        AfterCallLog.i(CALL_STATE, "stateChanged: $callState")
        if (lastState == callState) {
            AfterCallLog.d(CALL_STATE, "state unchanged — ignoring")
            return
        }
        when (callState) {
            0 -> {
                if (isVoiceOptionEnabled) {
                    isVoiceOptionEnabled = false
                    context.applicationContext.sendBroadcast(Intent("isVoiceOptionEnabled"))
                }
                AfterCallLog.d(
                    CALL_STATE,
                    "stateChanged: IDLE lastState=$lastState incoming=$isIncomingCall outgoing=$isOutgoingCall"
                )
                if (callStartTime == null) {
                    gettingTimeFromPref(context)
                    lastState = getPrefState(context)
                    isOutgoingCall = getOutgoingCall(context)
                    isIncomingCall = getIncomingCall(context)
                }
                if (lastState == 1) {
                    if (isIncomingCall) onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                    else onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                    nativeLoadPostCall(context)
                } else {
                    if (isIncomingCall) onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                    else onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
            }

            1 -> {
                AfterCallLog.d(CALL_STATE, "stateChanged: RINGING outgoing=$isOutgoingCall incoming=$isIncomingCall")
                isVoiceOptionEnabled = true
                startService(context)
                PostCallActivity.getPostCallActivity()?.finishActivity()
                disabledOpenAds()
                if (!isOutgoingCall) {
                    isIncomingCall = true
                    val date = Date()
                    callStartTime = date
                    saveStartTime(context, date)
                    saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                    savedNumber = str
                    onIncomingCallStarted(context, str, callStartTime)
                    AfterCallLog.d(CALL_STATE, "stateChanged: launching post-call (path 1)")
                }
                nativeLoadPostCall(context)
            }

            2 -> {
                if (isVoiceOptionEnabled) {
                    isVoiceOptionEnabled = false
                    context.applicationContext.sendBroadcast(Intent("isVoiceOptionEnabled"))
                }
                startService(context)
                AfterCallLog.d(
                    CALL_STATE,
                    "stateChanged: OFFHOOK lastState=$lastState outgoing=$isOutgoingCall incoming=$isIncomingCall"
                )
                if (callStartTime == null) {
                    lastState = getPrefState(context)
                    isOutgoingCall = getOutgoingCall(context)
                    isIncomingCall = getIncomingCall(context)
                }
                if (lastState != 1) {
                    PostCallActivity.getPostCallActivity()?.finishActivity()
                    disabledOpenAds()
                    if (!isOutgoingCall && !isIncomingCall) {
                        isOutgoingCall = true
                        isIncomingCall = false
                        val date2 = Date()
                        callStartTime = date2
                        saveStartTime(context, date2)
                        saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                        onOutgoingCallStarted(context, savedNumber, callStartTime)
                        AfterCallLog.d(CALL_STATE, "stateChanged: launching post-call (path 2)")
                    }
                    nativeLoadPostCall(context)
                } else if (!isOutgoingCall) {
                    isIncomingCall = true
                    callStartTime = Date()
                    saveStartTime(context, callStartTime!!)
                    saveCallState(context, callState, isIncomingCall, isOutgoingCall)
                    onIncomingCallAnswered(context, savedNumber, callStartTime)
                    nativeLoadPostCall(context)
                }
            }
        }
        lastState = callState
    }

    private fun disabledOpenAds() {
        setOpenAdHide(true)
    }

    fun nativeLoadPostCall(context: Context) {
        if (checkPermission() && isShowPostCallScreen && isEnablePostCallScreen) {
            val callCounter = context.callCounter
            if (isBannerLoad(callCounter)) {
                AfterCallLog.d(CALL_STATE, "ad-prep: banner-mode for this call")
                loadGoogleBannerAd(context)
            } else if (isNativeLoad(callCounter)) {
                AfterCallLog.d(CALL_STATE, "ad-prep: native-mode for this call")
                if (isNativeFailedGooglePostCallValue) return
                AfterCallLog.d(CALL_STATE, "ad-prep: requesting native preload")
                googleNativeLoadPostCall(context)
            }
        }
    }

    private fun loadGoogleBannerAd(context: Context) {
        if (bannerAdViewWidget == null && !isBannerAdLoading) {
            PostCallBannerAd().loadBanner(context, null, null)
        } else if (bannerAdViewWidget == null || isBannerAdLoading ||
            System.currentTimeMillis() - adBannerLoadTimeValue < adBannerExpirationTime
        ) {
            // banner is already loading or fresh — do nothing
        } else {
            PostCallBannerAd().onAdExpired(context)
            AfterCallLog.d(CALL_STATE, "ad-prep: cached banner expired")
        }
    }

    fun googleNativeLoadPostCall(context: Context) {
        if (checkPermission() && isShowPostCallScreen && isEnablePostCallScreen) {
            if (nativeAdsPostCallAd == null && !isNativeGooglePostCallValue) {
                AfterCallLog.d(CALL_STATE, "ad-prep: native already loading")
                isNativeGooglePostCallValue = true
                PostCallNativeAd(context).loadPostNative()
                return
            }
            if (nativeAdsPostCallAd != null) {
                val currentTimeMillis = System.currentTimeMillis()
                AfterCallLog.d(
                    CALL_STATE,
                    "ad-prep: native cache age=${currentTimeMillis - adNativeLoadTimeValue}ms"
                )
                if (currentTimeMillis - adNativeLoadTimeValue >= adNativeExpirationTime) {
                    if (!isNativePostCallLoadingValue) {
                        adNativeLoadTimeValue = 0L
                        nativeAdsPostCallAd = null
                        PostCallNativeAd(context).loadPostNative()
                    }
                    AfterCallLog.d(CALL_STATE, "ad-prep: native cache expired — reloading")
                }
            }
            AfterCallLog.d(CALL_STATE, "ad-prep: no native available — skipping preload")
        }
    }

    fun checkPermission(): Boolean = isOverlyPermision
    fun hasOverlayPermission(context: Context): Boolean = Settings.canDrawOverlays(context)

    private fun showOverlay(context: Context) {
        windowView = CallerWidgetWindow(context)
        windowView?.show()
    }

    private fun removeOverlay() {
        AfterCallLog.d(CALL_STATE, "removeOverlay: present=${windowView != null}")
        try {
            windowView?.hide()
        } catch (ignored: Exception) {
        }
    }
}
