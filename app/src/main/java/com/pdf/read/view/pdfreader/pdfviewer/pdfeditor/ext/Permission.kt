package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.*
import android.app.*
import android.content.pm.*
import android.os.*
import android.provider.*
import androidx.core.app.*

val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
else arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
fun Activity.hasPermissions(permissions: Array<String>): Boolean = permissions.all { ActivityCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED }

fun Activity.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

/**
 * The trio of permissions the after-call screen needs to function:
 *   - `READ_PHONE_STATE` so [coder.apps.aftercall.receivers.PhoneStateReceiver]
 *     can detect call-ended transitions.
 *   - `SYSTEM_ALERT_WINDOW` so the post-call screen can be launched from the
 *     background on Android 10+.
 *   - `POST_NOTIFICATIONS` (Android 13+) for the tap-to-open fallback when the
 *     overlay route is denied.
 *
 * Used by the launch sequence (Splash → Language → Permission → Onboarding →
 * Dashboard) to decide whether the permission screen still needs to be shown.
 * Reflects the *current* OS permission state — there's deliberately no "seen"
 * flag, so re-launching with a missing permission re-shows the funnel.
 */
fun android.content.Context.hasAllAfterCallPermissions(): Boolean {
    val phoneOk = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED
    val overlayOk = Settings.canDrawOverlays(this)
    val notifyOk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
    return phoneOk && overlayOk && notifyOk
}