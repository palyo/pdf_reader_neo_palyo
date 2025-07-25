package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import com.itextpdf.text.pdf.PdfReader
import java.io.IOException

fun String.isProtected(): Boolean {
    val isEncrypt: Boolean = try {
        val reader = PdfReader(this)
        val pageCount = reader.numberOfPages
        false
    } catch (e: IOException) {
        e.printStackTrace()
        true
    }
    return isEncrypt
}