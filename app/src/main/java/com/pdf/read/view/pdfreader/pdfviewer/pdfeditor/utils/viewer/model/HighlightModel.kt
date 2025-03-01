package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class HighlightModel(
    val id: Long,
    val snippet: String,
    val color: String,
    val page: Int,
    val updatedAt: Long,
    val coordinates: Coordinates?,
    val segments: List<CharDrawSegments>,
) : PdfAnnotationModel(Type.Highlight, page - 1), Parcelable {
    @IgnoredOnParcel
    var isSelected = false

    fun updateAnnotationData(): HighlightModel {
        super.paginationPageIndex = page - 1
        super.type = Type.Highlight
        return this
    }
}