package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util

import android.graphics.*
import kotlin.math.*

object CanvasUtils {
    fun isCircleCollided(cx1: Float, cy1: Float, r1: Float, cx2: Float, cy2: Float, r2: Float): Boolean {
        val dx = cx1 - cx2
        val dy = cy1 - cy2
        val distance = sqrt(dx * dx + dy * dy)
        return distance <= r1 + r2
    }
}

fun RectF.zoom(value: Float): RectF {
    return RectF(left * value, top * value, right * value, bottom * value)
}
