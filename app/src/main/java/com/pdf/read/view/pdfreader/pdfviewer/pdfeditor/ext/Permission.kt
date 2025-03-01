package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.*
import android.app.*
import android.content.pm.*
import android.os.*
import android.provider.*
import androidx.core.app.*

val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
else arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

fun Activity.hasPermissions(permissions: Array<String>): Boolean = permissions.all { ActivityCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED }

fun Activity.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}