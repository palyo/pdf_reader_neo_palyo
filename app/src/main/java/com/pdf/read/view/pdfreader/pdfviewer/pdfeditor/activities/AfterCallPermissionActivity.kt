package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coder.apps.space.library.base.BaseActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ActivityAfterCallPermissionBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ItemAfterCallPermissionCardBinding

/**
 * Permission funnel for the post-call screen.
 *
 * The after-call experience needs three things from the user:
 *
 *  1. **READ_PHONE_STATE** — so the [coder.apps.aftercall.receivers.PhoneStateReceiver]
 *     can detect that a call ended.
 *  2. **SYSTEM_ALERT_WINDOW** — so [coder.apps.aftercall.ui.PostCallActivity] can be
 *     launched from the background (Android 10+ otherwise blocks background activity
 *     starts).
 *  3. **POST_NOTIFICATIONS** (Android 13+) — fallback path when the overlay
 *     permission isn't granted; we surface a notification the user can tap.
 *
 * Each card is a self-contained row that can be tapped individually, *or* the user
 * can hit "Allow & Continue" at the bottom to run all three in sequence. Once every
 * required permission is granted we route to [HomeActivity] (or back to the caller
 * if it provided a result registration via `startActivityForResult`).
 *
 * Extends [BaseActivity] like the other host-app activities so the codespace
 * library handles the binding inflation, `setContentView`, status-bar / nav-bar
 * theming and `tinyDB` plumbing in one place. The three abstract callbacks
 * ([initExtra] / [initView] / [initListeners]) receive the binding as a
 * receiver, so child views are accessible directly without a `binding.` prefix.
 */
class AfterCallPermissionActivity :
    BaseActivity<ActivityAfterCallPermissionBinding>(ActivityAfterCallPermissionBinding::inflate) {

    private var phoneCard: ItemAfterCallPermissionCardBinding? = null
    private var overlayCard: ItemAfterCallPermissionCardBinding? = null
    private var notifyCard: ItemAfterCallPermissionCardBinding? = null

    /**
     * When the user taps "Allow & Continue" we walk through every missing permission
     * one by one. Individual card taps set this to false so we don't chain.
     */
    private var sequentialFlow = false

    private val phonePermLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            refreshStatus()
            if (sequentialFlow) requestOverlay()
        }

    private val notifyPermLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            refreshStatus()
            if (sequentialFlow && allGranted()) goNext()
        }

    private val overlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshStatus()
            if (sequentialFlow) requestNotifications()
        }

    override fun ActivityAfterCallPermissionBinding.initExtra() {
        phoneCard = ItemAfterCallPermissionCardBinding.bind(cardPhone.root)
        overlayCard = ItemAfterCallPermissionCardBinding.bind(cardOverlay.root)
        notifyCard = ItemAfterCallPermissionCardBinding.bind(cardNotify.root)
        refreshStatus()
    }

    override fun ActivityAfterCallPermissionBinding.initView() {
        onBackPressedDispatcher.addCallback(
            this@AfterCallPermissionActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Back from the permission screen during first-run flow falls
                    // through to Home — never strands the user with no UI.
                    finishToHome(resultOk = false)
                }
            }
        )
    }

    override fun ActivityAfterCallPermissionBinding.initListeners() {
        phoneCard?.apply {
            imagePermIcon.setImageResource(R.drawable.ic_after_call_phone)
            textPermLabel.setText(R.string.after_call_perm_phone_label)
            textPermDesc.setText(R.string.after_call_perm_phone_desc)
            permCardRoot.setOnClickListener {
                sequentialFlow = false
                requestPhoneState()
            }
        }
        overlayCard?.apply {
            imagePermIcon.setImageResource(R.drawable.ic_after_call_overlay)
            textPermLabel.setText(R.string.after_call_perm_overlay_label)
            textPermDesc.setText(R.string.after_call_perm_overlay_desc)
            permCardRoot.setOnClickListener {
                sequentialFlow = false
                requestOverlay()
            }
        }
        notifyCard?.apply {
            imagePermIcon.setImageResource(R.drawable.ic_after_call_notify)
            textPermLabel.setText(R.string.after_call_perm_notify_label)
            textPermDesc.setText(R.string.after_call_perm_notify_desc)
            permCardRoot.setOnClickListener {
                sequentialFlow = false
                requestNotifications()
            }
        }

        buttonContinue.setOnClickListener {
            sequentialFlow = true
            startSequentialFlow()
        }
        buttonSkip.setOnClickListener {
            finishToHome(resultOk = false)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    override fun onDestroy() {
        phoneCard = null
        overlayCard = null
        notifyCard = null
        super.onDestroy()
    }

    private fun startSequentialFlow() {
        when {
            !hasPhoneState() -> requestPhoneState()
            !hasOverlay() -> requestOverlay()
            !hasNotifications() -> requestNotifications()
            else -> goNext()
        }
    }

    private fun requestPhoneState() {
        if (hasPhoneState()) {
            if (sequentialFlow) requestOverlay()
            return
        }
        phonePermLauncher.launch(Manifest.permission.READ_PHONE_STATE)
    }

    private fun requestOverlay() {
        if (hasOverlay()) {
            if (sequentialFlow) requestNotifications()
            return
        }
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:$packageName".toUri()
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }
        overlayLauncher.launch(intent)
    }

    private fun requestNotifications() {
        if (hasNotifications()) {
            if (sequentialFlow && allGranted()) goNext()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifyPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Pre-Android-13: nothing to request, treat as granted and move on.
            if (sequentialFlow && allGranted()) goNext()
        }
    }

    private fun refreshStatus() {
        phoneCard?.let { applyStatus(it, hasPhoneState()) }
        overlayCard?.let { applyStatus(it, hasOverlay()) }
        notifyCard?.let { applyStatus(it, hasNotifications()) }
    }

    private fun applyStatus(card: ItemAfterCallPermissionCardBinding, granted: Boolean) {
        card.textPermStatus.apply {
            if (granted) {
                setBackgroundResource(R.drawable.bg_after_call_pill_granted)
                setText(R.string.after_call_perm_status_granted)
                setTextColor(
                    ContextCompat.getColor(this@AfterCallPermissionActivity, R.color.colorGreen)
                )
            } else {
                setBackgroundResource(R.drawable.bg_after_call_pill_required)
                setText(R.string.after_call_perm_status_required)
                setTextColor(
                    ContextCompat.getColor(this@AfterCallPermissionActivity, R.color.colorAccent)
                )
            }
        }
    }

    private fun hasPhoneState() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasOverlay() = Settings.canDrawOverlays(this)

    private fun hasNotifications(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun allGranted() = hasPhoneState() && hasOverlay() && hasNotifications()

    private fun goNext() {
        finishToHome(resultOk = true)
    }

    /**
     * Single exit point for the screen.
     *
     * Routes the user to the next step of the first-run flow:
     *   Splash → Language → **Permission (here)** → Dashboard.
     *
     * The Onboarding step that used to sit between Permission and Dashboard
     * was removed from the cold-start chain. [AppBoardingActivity] still
     * exists for other entry points (e.g. settings menu), but we no longer
     * chain into it from here.
     *
     * Crucially, this method does **not** persist any "seen" flag. The
     * launch-flow check
     * ([com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.hasAllAfterCallPermissions])
     * reads the live OS permission state instead — so if the user taps
     * "Maybe later" with permissions still missing, the screen re-appears
     * on the next cold start. Once every required permission is granted,
     * the screen is silently skipped.
     *
     * If we were launched with `startActivityForResult`, also propagate the
     * outcome so the caller can react (e.g. a settings entry-point can
     * update a toggle's checked state).
     */
    private fun finishToHome(resultOk: Boolean) {
        setResult(if (resultOk) Activity.RESULT_OK else Activity.RESULT_CANCELED)

        Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
        }
        finish()
    }
}
