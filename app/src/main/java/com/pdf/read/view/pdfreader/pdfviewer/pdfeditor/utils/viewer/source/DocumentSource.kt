package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.source

import android.content.Context
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.IOException

interface DocumentSource {
    @Throws(IOException::class)
    fun createDocument(context: Context?, core: PdfiumCore?, password: String?): PdfDocument?

    fun getBytes(): ByteArray

    fun getFile(): File?

    fun getStartPageIndex(): Int

    fun defaultPageIndexToLoad(): Int
}
