package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class  Coordinates(
    var startX: Double,
    var startY: Double,
    var endX: Double,
    var endY: Double,
) : Parcelable