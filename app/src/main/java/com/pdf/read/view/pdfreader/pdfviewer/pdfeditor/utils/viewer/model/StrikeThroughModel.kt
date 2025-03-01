package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class StrikeThroughModel(
    val id: Long,
    val snippet: String,
    val page: Int,
    var updatedAt: Long,
    val coordinates: Coordinates?,
    val segments: List<CharDrawSegments>,
) : PdfAnnotationModel(Type.StrikeThrough, page - 1), Parcelable {
    @IgnoredOnParcel
    var isSelected = false

    fun updateAnnotationData(): StrikeThroughModel {
        super.paginationPageIndex = page - 1
        super.type = Type.StrikeThrough
        return this
    }
}