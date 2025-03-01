package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.graphics.*
import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class SignatureModel(
    val id: Long,
    val bitmap: Bitmap,
    val page: Int,
    var updatedAt: Long,
    val coordinates: Coordinates?,
) : PdfAnnotationModel(Type.Signature, page - 1), Parcelable {
    fun updateAnnotationData(): SignatureModel {
        super.paginationPageIndex = page - 1
        super.type = Type.Signature
        return this
    }
}