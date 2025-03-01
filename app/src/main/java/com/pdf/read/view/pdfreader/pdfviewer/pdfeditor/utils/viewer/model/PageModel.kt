package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

data class PageModel(
    val width: Float,
    val height: Float,
    val coordinates: ArrayList<PdfLine>,
) {
    var relativeSizeCalculated = false
}
