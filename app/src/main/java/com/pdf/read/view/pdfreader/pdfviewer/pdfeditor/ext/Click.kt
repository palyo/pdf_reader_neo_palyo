package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.os.*
import android.view.*

class OnSingleClickListener(private val block: () -> Unit) : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        block()
    }
}

fun View.singleClick(block: () -> Unit) {
    setOnClickListener(OnSingleClickListener(block))
}