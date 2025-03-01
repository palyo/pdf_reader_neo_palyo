package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnderlineModel(
    val id: Long,
    val snippet: String,
    val page: Int,
    var updatedAt: Long,
    val coordinates: Coordinates?,
    val segments: List<CharDrawSegments>,
) : PdfAnnotationModel(Type.Underline, page - 1), Parcelable {
    @IgnoredOnParcel
    var isSelected = false

    fun updateAnnotationData(): UnderlineModel {
        super.paginationPageIndex = page - 1
        super.type = Type.Underline
        return this
    }
}




