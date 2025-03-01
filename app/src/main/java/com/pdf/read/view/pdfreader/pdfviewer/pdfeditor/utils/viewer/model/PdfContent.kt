package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.graphics.*
import android.os.*
import kotlinx.parcelize.*

class PdfLine(
    val id: Int,
    val text: String,
    val position: PointF,
    val size: Size,
) {
    var relatedPosition = PointF()
    var relatedSize = Size(0f, 0f)
    val words: ArrayList<PdfWord> = arrayListOf()
    val rect: RectF = RectF()
}

class PdfWord(
    val id: Int,
    val text: String,
    val position: PointF,
    val size: Size,
) {
    var relatedPosition = PointF()
    var relatedSize = Size(0f, 0f)
    val characters: ArrayList<PdfChar> = arrayListOf()
    val rect: RectF = RectF()
}

@Parcelize
data class PdfChar(
    val id: Int,
    val lineId: Int,
    val wordId: Int,
    val text: String,
    val topPosition: PointF,
    val bottomPosition: PointF,
    val size: Size,
    val pageNumber: Int,
) : Parcelable {
    var relatedPosition = PointF()
    var relatedSize = Size(0f, 0f)
    val rect: RectF = RectF()
}

@Parcelize
class Size(var width: Float = 0f, var height: Float = 0f) : Parcelable {
    fun set(width: Float, height: Float) {
        this.width = width
        this.height = height
    }
}
