package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils

import android.graphics.*
import android.graphics.pdf.*
import android.os.*
import android.util.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.tom_roush.pdfbox.cos.*
import com.tom_roush.pdfbox.pdmodel.*
import com.tom_roush.pdfbox.pdmodel.common.*
import com.tom_roush.pdfbox.pdmodel.graphics.color.*
import com.tom_roush.pdfbox.pdmodel.graphics.image.*
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.*
import com.tom_roush.pdfbox.text.*
import java.io.*
import kotlin.math.*

class PDFHighlighter {

    fun extractTextFromPdf(pdfFile: File): String {
        PDDocument.load(pdfFile).use { document ->
            val stripper = PDFTextStripper()
            return stripper.getText(document)
        }
    }

    fun getPageNumberForQuery(query: String, pdfFile: File): Int {
        val pdfDocument = PDDocument.load(pdfFile)
        val stripper = PDFTextStripper()
        for (page in 1..pdfDocument.numberOfPages) {
            stripper.startPage = page
            stripper.endPage = page
            val pageText = stripper.getText(pdfDocument)
            if (pageText.contains(query, ignoreCase = true)) {
                return page-1
            }
        }
        return -1
    }

    fun List<CharDrawSegments>.mergeAllChars(): List<PdfChar> {
        val allChars = ArrayList<PdfChar>()
        for (segment in this) {
            allChars.addAll(segment.chars)
        }
        return allChars
    }

    fun processAnnotations(
        annotations: ArrayList<PdfAnnotationModel>,
        viewWidth: Float,
        viewHeight: Float,
        src: String,
        dest: String,
        listener: (isError: Boolean, message: String?) -> Unit
    ) {
        try {
            // Load the PDF document once.
            val pdfDocument = PDDocument.load(File(src))
            val highlighter = PDFHighlighter()
            // Process all highlight annotations.
            annotations.filter { it.asHighlight() != null }.forEach { ann ->
                ann.asHighlight()?.let { highlightAnn ->
                    val allChars: List<PdfChar> = highlightAnn.segments.mergeAllChars()
                    // Add the highlight annotation to the PDF (do not save/close here)
                    highlighter.highlightMultiLineSelectedWord(
                        pdfDocument = pdfDocument,
                        pageIndex = highlightAnn.page - 1,
                        selectedChars = allChars,
                        color = Color.parseColor(highlightAnn.color)
                    )
                }
            }
            // Process all underline annotations.
            annotations.filter { it.asUnderline() != null }.forEach { ann ->
                ann.asUnderline()?.let { underlineAnn ->
                    val allChars: List<PdfChar> = underlineAnn.segments.mergeAllChars()
                    highlighter.underlineMultiLineSelectedWord(
                        pdfDocument = pdfDocument,
                        pageIndex = underlineAnn.page - 1,
                        selectedChars = allChars
                    )
                }
            }
            // Process all strikethrough annotations.
            annotations.filter { it.asStrikeThrough() != null }.forEach { ann ->
                ann.asStrikeThrough()?.let { strikeAnn ->
                    val allChars: List<PdfChar> = strikeAnn.segments.mergeAllChars()
                    highlighter.strikeoutMultiLineSelectedWord(
                        pdfDocument = pdfDocument,
                        pageIndex = strikeAnn.page - 1,
                        selectedChars = allChars
                    )
                }
            }

            annotations.filter { it.asSignature() != null }.forEach { ann ->
                ann.asSignature()?.let { signatureAnn ->
                    addSignatureToPdf(pdfDocument, signatureAnn, viewWidth, viewHeight)
                }
            }

            pdfDocument.save(dest)
            pdfDocument.close()
            listener.invoke(false, "")
        } catch (e: Exception) {
            listener.invoke(true, e.message)
        }
    }

    /**
     * Highlights multi-line selected text by computing per-line bounding boxes and gap rectangles dynamically.
     *
     * @param pdfDocument An open PDDocument.
     * @param pageIndex Zero-based page index where the selection occurs.
     * @param selectedChars The list of selected PdfChar objects (spanning one or more lines).
     * @param color The Android color to use for the highlight.
     * @param outputPath Path to save the updated PDF.
     * @param coordinatesInViewSpace True if char.topPosition is in view coordinates (origin top‑left).
     */
    fun highlightMultiLineSelectedWord(
        pdfDocument: PDDocument,
        pageIndex: Int,
        selectedChars: List<PdfChar>,
        color: Int = Color.YELLOW,
        coordinatesInViewSpace: Boolean = true
    ) {
        try {
            val page = pdfDocument.getPage(pageIndex)
            val pdfPageHeight = page.mediaBox.height
            // Group selected characters by lineId.
            val groups: Map<Int, List<PdfChar>> = selectedChars.groupBy { it.lineId }
            val quadPointsList = mutableListOf<Float>()
            var overallMinX = Float.MAX_VALUE
            var overallMinY = Float.MAX_VALUE
            var overallMaxX = Float.MIN_VALUE
            var overallMaxY = Float.MIN_VALUE
            // Process each line group.
            groups.forEach { (lineId, charsInLine) ->
                var lineMinX = Float.MAX_VALUE
                var lineMaxX = Float.MIN_VALUE
                var lineMinY = Float.MAX_VALUE
                var lineMaxY = Float.MIN_VALUE

                charsInLine.forEach { char ->
                    // X bounds (assuming topPosition.x is correct)
                    val charLeft = char.topPosition.x
                    val charRight = char.topPosition.x + char.size.width
                    // Convert char.topPosition.y from view space to PDF space if needed.
                    val charTopPDF = if (coordinatesInViewSpace) pdfPageHeight - char.topPosition.y else char.topPosition.y
                    // Use char.bottomPosition.y directly (assumed in PDF space)
                    val charBottomPDF = char.bottomPosition.y

                    lineMinX = min(lineMinX, charLeft)
                    lineMaxX = max(lineMaxX, charRight)
                    lineMinY = min(lineMinY, charTopPDF)
                    lineMaxY = max(lineMaxY, charBottomPDF)
                }
                // Update overall bounding rectangle.
                overallMinX = min(overallMinX, lineMinX)
                overallMaxX = max(overallMaxX, lineMaxX)
                overallMinY = min(overallMinY, lineMinY)
                overallMaxY = max(overallMaxY, lineMaxY)
                // Create quad points for this line.
                // In PDF, "top" is the larger Y value.
                val lineQuadPoints = floatArrayOf(
                    lineMinX, lineMaxY,   // top-left
                    lineMaxX, lineMaxY,   // top-right
                    lineMinX, lineMinY,   // bottom-left
                    lineMaxX, lineMinY    // bottom-right
                )
                quadPointsList.addAll(lineQuadPoints.toList())
            }
            // Overall union bounding rectangle.
            val unionRect = PDRectangle(overallMinX, overallMinY, overallMaxX - overallMinX, overallMaxY - overallMinY)
            // Create the highlight annotation.
            val annotation = PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT)
            annotation.rectangle = unionRect
            annotation.quadPoints = quadPointsList.toFloatArray()
            // Convert Android color to PDF color.
            val colorArray = floatArrayOf(
                Color.red(color) / 255f,
                Color.green(color) / 255f,
                Color.blue(color) / 255f
            )
            annotation.color = PDColor(colorArray, PDDeviceRGB.INSTANCE)
            annotation.contents = "Highlighted text"
            // Set transparency (opacity) to 0.3.
            annotation.cosObject.setItem(COSName.CA, COSFloat(0.3f))

            page.annotations.add(annotation)
            Log.d("Highlight", "Multi-line highlight added on page $pageIndex with union rect: $unionRect")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Underlines multi-line selected text by grouping the selected PdfChar objects by line.
     *
     * @param pdfDocument An open PDDocument.
     * @param pageIndex Zero-based index of the page where the selection occurs.
     * @param selectedChars List of PdfChar objects spanning the selection.
     * @param color Android color to use for the underline.
     * @param outputPath File path to save the updated PDF.
     * @param coordinatesInViewSpace True if your PdfChar positions come from view coordinates (origin top‑left).
     */
    private fun underlineMultiLineSelectedWord(
        pdfDocument: PDDocument,
        pageIndex: Int,
        selectedChars: List<PdfChar>,
        color: Int = Color.RED,
        coordinatesInViewSpace: Boolean = true
    ) {
        try {
            val page = pdfDocument.getPage(pageIndex)
            val pdfPageHeight = page.mediaBox.height
            // Group selected characters by lineId.
            val groups: Map<Int, List<PdfChar>> = selectedChars.groupBy { it.lineId }
            val quadPointsList = mutableListOf<Float>()
            var overallMinX = Float.MAX_VALUE
            var overallMinY = Float.MAX_VALUE
            var overallMaxX = Float.MIN_VALUE
            var overallMaxY = Float.MIN_VALUE

            groups.forEach { (_, charsInLine) ->
                var lineMinX = Float.MAX_VALUE
                var lineMaxX = Float.MIN_VALUE
                var lineMinY = Float.MAX_VALUE  // lower Y value in PDF space
                var lineMaxY = Float.MIN_VALUE  // higher Y value in PDF space

                charsInLine.forEach { char ->
                    // X bounds.
                    val charLeft = char.topPosition.x
                    val charRight = char.topPosition.x + char.size.width
                    // Convert Y coordinate if needed.
                    val charTopPDF = if (coordinatesInViewSpace) pdfPageHeight - char.topPosition.y else char.topPosition.y
                    val charBottomPDF = char.bottomPosition.y  // assumed in PDF space.

                    lineMinX = min(lineMinX, charLeft)
                    lineMaxX = max(lineMaxX, charRight)
                    lineMinY = min(lineMinY, charTopPDF)
                    lineMaxY = max(lineMaxY, charBottomPDF)
                }

                overallMinX = min(overallMinX, lineMinX)
                overallMaxX = max(overallMaxX, lineMaxX)
                overallMinY = min(overallMinY, lineMinY)
                overallMaxY = max(overallMaxY, lineMaxY)
                // For underline, quad points are defined similarly to highlight:
                // [top-left.x, top-left.y, top-right.x, top-right.y, bottom-left.x, bottom-left.y, bottom-right.x, bottom-right.y]
                // In PDF space, "top" is the higher Y value.
                val lineQuadPoints = floatArrayOf(
                    lineMinX, lineMaxY,   // top-left
                    lineMaxX, lineMaxY,   // top-right
                    lineMinX, lineMinY,   // bottom-left
                    lineMaxX, lineMinY    // bottom-right
                )
                quadPointsList.addAll(lineQuadPoints.toList())
            }
            // Create overall union bounding rectangle.
            val unionRect = PDRectangle(overallMinX, overallMinY, overallMaxX - overallMinX, overallMaxY - overallMinY)
            // Create the underline annotation using PDAnnotationTextMarkup with subtype UNDERLINE.
            val annotation = PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE)
            annotation.rectangle = unionRect
            annotation.quadPoints = quadPointsList.toFloatArray()
            // Convert Android color to PDF color.
            val colorArray = floatArrayOf(
                Color.red(color) / 255f,
                Color.green(color) / 255f,
                Color.blue(color) / 255f
            )
            annotation.color = PDColor(colorArray, PDDeviceRGB.INSTANCE)
            annotation.contents = "Underlined text"

            page.annotations.add(annotation)
            Log.d("Underline", "Multi-line underline added on page $pageIndex with union rect: $unionRect")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Strikes through multi-line selected text by grouping the selected PdfChar objects by line.
     *
     * For each line, it computes a horizontal band at the vertical center of the line.
     *
     * @param pdfDocument An open PDDocument.
     * @param pageIndex Zero-based index of the page where the selection occurs.
     * @param selectedChars List of PdfChar objects spanning the selection.
     * @param color Android color to use for the strikeout.
     * @param outputPath File path to save the updated PDF.
     * @param coordinatesInViewSpace True if your PdfChar positions come from view coordinates (origin top‑left)
     *                               so that char.topPosition.y must be converted using the PDF page height.
     */
    private fun strikeoutMultiLineSelectedWord(
        pdfDocument: PDDocument,
        pageIndex: Int,
        selectedChars: List<PdfChar>,
        color: Int = Color.RED,
        coordinatesInViewSpace: Boolean = true
    ) {
        try {
            val page = pdfDocument.getPage(pageIndex)
            val pdfPageHeight = page.mediaBox.height
            // Group selected characters by their lineId.
            val groups: Map<Int, List<PdfChar>> = selectedChars.groupBy { it.lineId }
            val quadPointsList = mutableListOf<Float>()
            var overallMinX = Float.MAX_VALUE
            var overallMinY = Float.MAX_VALUE
            var overallMaxX = Float.MIN_VALUE
            var overallMaxY = Float.MIN_VALUE
            // Thickness of the strikeout band (total thickness); half-thickness is used above and below the center.
            val halfThickness = 1.0f

            groups.forEach { (_, charsInLine) ->
                var lineMinX = Float.MAX_VALUE
                var lineMaxX = Float.MIN_VALUE
                var lineTopPDF = Float.MAX_VALUE  // lower edge in PDF space (converted top)
                var lineBottomPDF = Float.MIN_VALUE  // upper edge in PDF space

                charsInLine.forEach { char ->
                    // X bounds (using char.topPosition.x and size.width)
                    val charLeft = char.topPosition.x
                    val charRight = char.topPosition.x + char.size.width
                    // Convert char.topPosition.y from view space to PDF space if needed.
                    val charTop = if (coordinatesInViewSpace) pdfPageHeight - char.topPosition.y else char.topPosition.y
                    // Assume char.bottomPosition.y is already in PDF space.
                    val charBottom = char.bottomPosition.y

                    lineMinX = min(lineMinX, charLeft)
                    lineMaxX = max(lineMaxX, charRight)
                    lineTopPDF = min(lineTopPDF, charTop)       // lower value in PDF space
                    lineBottomPDF = max(lineBottomPDF, charBottom) // higher value
                }
                // Compute the vertical center for this line.
                val centerY = (lineTopPDF + lineBottomPDF) / 2.0f
                // For strikeout, we want a thin band around centerY.
                val strikeTop = centerY + halfThickness
                val strikeBottom = centerY - halfThickness
                // Update overall union bounds.
                overallMinX = min(overallMinX, lineMinX)
                overallMaxX = max(overallMaxX, lineMaxX)
                overallMinY = min(overallMinY, strikeBottom)
                overallMaxY = max(overallMaxY, strikeTop)
                // For each line, create quad points using the computed band.
                // PDF quad points order: [top-left, top-right, bottom-left, bottom-right]
                val lineQuadPoints = floatArrayOf(
                    lineMinX, strikeTop,    // top-left (x, higher Y)
                    lineMaxX, strikeTop,    // top-right
                    lineMinX, strikeBottom, // bottom-left
                    lineMaxX, strikeBottom  // bottom-right
                )
                quadPointsList.addAll(lineQuadPoints.toList())
            }
            // Create overall union bounding rectangle.
            val unionRect = PDRectangle(
                overallMinX,
                overallMinY,
                overallMaxX - overallMinX,
                overallMaxY - overallMinY
            )
            // Create the strikeout annotation.
            val annotation = PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT)
            annotation.rectangle = unionRect
            annotation.quadPoints = quadPointsList.toFloatArray()
            // Convert Android color to PDF color.
            val colorArray = floatArrayOf(
                Color.red(color) / 255f,
                Color.green(color) / 255f,
                Color.blue(color) / 255f
            )
            annotation.color = PDColor(colorArray, PDDeviceRGB.INSTANCE)
            annotation.contents = "Strikethrough text"

            page.annotations.add(annotation)
            Log.d("Strikeout", "Multi-line strikeout added on page $pageIndex with union rect: $unionRect")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Adds a signature bitmap to the PDF at the exact position defined in view coordinates.
     *
     * @param pdfDocument The loaded PDF document.
     * @param signature The signature model containing coordinates (from the view) and bitmap.
     * @param viewWidth The width of the view where the signature was captured.
     * @param viewHeight The height of the view where the signature was captured.
     */
    fun addSignatureToPdf(
        pdfDocument: PDDocument,
        signature: SignatureModel,
        viewWidth: Float,
        viewHeight: Float
    ) {
        val coords = signature.coordinates ?: return
        val signatureBitmap = signature.bitmap ?: return
        val pageIndex = signature.page - 1
        val page: PDPage = pdfDocument.getPage(pageIndex)
        val pdfPageWidth = page.mediaBox.width
        val pdfPageHeight = page.mediaBox.height
        val width = pdfPageWidth * (coords.endX - coords.startX) / viewWidth
        val height = pdfPageHeight * (coords.endY - coords.startY) / viewHeight
        val x = pdfPageWidth * coords.startX / viewWidth
        val y = (pdfPageHeight - (pdfPageHeight * coords.endY / viewHeight)) - 10
        val pdImage = LosslessFactory.createFromImage(pdfDocument, signatureBitmap)
        val contentStream = PDPageContentStream(
            pdfDocument,
            page,
            PDPageContentStream.AppendMode.APPEND,
            true,
            true
        )
        contentStream.drawImage(pdImage, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
        contentStream.close()
    }

    fun capturePdfPage(pdfFile: File, pageIndex: Int): Boolean {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)

            if (pageIndex < 0 || pageIndex >= renderer.pageCount) {
                renderer.close()
                return false
            }

            val page = renderer.openPage(pageIndex)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()


            val ssDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Screenshots")
            if (!ssDir.exists()) ssDir.mkdirs()

            // Save the screenshot
            val screenshotFile = File(ssDir, "PDF_Screenshot_${System.currentTimeMillis()}.jpeg")
            FileOutputStream(screenshotFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
