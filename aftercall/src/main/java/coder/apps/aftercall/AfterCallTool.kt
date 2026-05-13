package coder.apps.aftercall

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * A single entry rendered in the after-call screen's tool grid (or as the
 * primary "record" tile). The host app provides the icon, label, and the
 * intent that should fire when the user taps it.
 *
 * Pass either [labelRes] or [label]; if both are set [label] wins.
 */
data class AfterCallTool(
    @DrawableRes val iconRes: Int = 0,
    @StringRes val labelRes: Int = 0,
    val label: CharSequence? = null,
    val intentFactory: (Context) -> Intent,
)
