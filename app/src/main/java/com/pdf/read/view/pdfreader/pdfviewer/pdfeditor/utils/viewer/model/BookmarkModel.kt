package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class BookmarkModel(
    val id: Long,
    val page: Int,
    val updatedAt: Long,
) : Parcelable {
    @IgnoredOnParcel
    var isSelected = false
}