package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.annotation

import android.content.res.Resources
import android.graphics.*
import android.util.TypedValue
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.*

class PdfAnnotationHandler(
    resource: Resources,
) {
    var annotations = arrayListOf<PdfAnnotationModel>()
    private var noteColor = Color.parseColor("#FA5D3B")

    private var addedNoteStampDetails = HashMap<Int, List<AddedStampDetails>>()
    private var addedStrikethroughDetails = HashMap<Int, List<AddedStampDetails>>()

    val textSize = getDpValue(resource, 8f)

    fun drawAnnotations(paginationPageIndexes: List<Int>, pageOffsets: List<Float>, canvas: Canvas, zoom: Float) {
        addedNoteStampDetails.clear()
        addedStrikethroughDetails.clear()
        for (annotation in annotations) {
            val index = paginationPageIndexes.indexOfFirst { it == annotation.paginationPageIndex }
            if (index == -1) continue
            val pageOffset = pageOffsets[index]
            canvas.translate(0f, pageOffset * zoom)
            try {
                when (annotation.type) {
                    PdfAnnotationModel.Type.Underline -> drawUnderlineAnnotations(canvas, zoom, annotation.asUnderline()!!, index)
                    PdfAnnotationModel.Type.StrikeThrough -> drawStrikethroughAnnotations(canvas, zoom, annotation.asStrikeThrough()!!, index)
                    PdfAnnotationModel.Type.Highlight -> drawHighlights(canvas, zoom, annotation.asHighlight()!!)
                    PdfAnnotationModel.Type.Signature -> drawSignature(canvas, zoom, annotation.asSignature()!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            canvas.translate(0f, -pageOffset * zoom)
        }
    }

    private fun drawStrikethroughAnnotations(
        canvas: Canvas,
        zoom: Float,
        note: StrikeThroughModel,
        mainPageIndex: Int
    ) {
        val paint = Paint().apply {
            color = noteColor
            style = Paint.Style.STROKE
            strokeWidth = 2f * zoom
        }
        val selectionDetail = note.charDrawSegments
        selectionDetail.forEachIndexed { index, data ->
            val rect = data.rect.zoom(zoom)
            // Calculate the center Y position (strikethrough line position)
            val centerY = (rect.top + rect.bottom) / 2
            if (index == 0) {
                // Store note start position (we'll use this later for drawing badge and number)
                addStrikethroughDetails(centerY, mainPageIndex, note.id.toInt())
            }
            // Draw a line from the left to the right of the segment at the vertical center.
            canvas.drawLine(
                rect.left,
                centerY,
                rect.right,
                centerY,
                paint,
            )
        }
    }

    private fun drawUnderlineAnnotations(canvas: Canvas, zoom: Float, note: UnderlineModel, mainPageIndex: Int) {
        val paint = Paint().apply {
            color = noteColor
            style = Paint.Style.STROKE
            strokeWidth = 2f * zoom
        }
        val selectionDetail = note.charDrawSegments
        selectionDetail.forEachIndexed { index, data ->
            val rect = data.rect.zoom(zoom)
            if (index == 0) {
                // storing note start position , we will later use this value to draw badge and number
                val middleY = (data.rect.top + data.rect.bottom) / 2
                addUnderlineStampDetails(middleY, mainPageIndex, note.id.toInt())
            }
            canvas.drawLine(
                rect.left,
                rect.bottom,
                rect.right,
                rect.bottom,
                paint,
            )
        }
    }
    private fun addStrikethroughDetails(y: Float, page: Int, noteId: Int) {
        val alreadyAddedYs = ArrayList(addedStrikethroughDetails[page] ?: emptyList())
        val addedStamp = alreadyAddedYs.filter {
            it.y == y
        }
        if (addedStamp.isEmpty()) {
            // there no stamp exist already , so  we add new one
            val stampDetails = AddedStampDetails(y).also { it.noteIds.add(noteId) }
            alreadyAddedYs.add(stampDetails)
            addedStrikethroughDetails[page] = alreadyAddedYs
        } else {
            // already badge in that position, so we will add the note id
            addedStamp.firstOrNull()?.noteIds?.add(noteId)
        }
    }

    private fun addUnderlineStampDetails(y: Float, page: Int, noteId: Int) {
        val alreadyAddedYs = ArrayList(addedNoteStampDetails[page] ?: emptyList())
        val addedStamp = alreadyAddedYs.filter {
            it.y == y
        }
        if (addedStamp.isEmpty()) {
            // there no stamp exist already , so  we add new one
            val stampDetails = AddedStampDetails(y).also { it.noteIds.add(noteId) }
            alreadyAddedYs.add(stampDetails)
            addedNoteStampDetails[page] = alreadyAddedYs
        } else {
            // already badge in that position, so we will add the note id
            addedStamp.firstOrNull()?.noteIds?.add(noteId)
        }
    }

    private fun drawHighlights(canvas: Canvas, zoom: Float, highlight: HighlightModel) {
        val paint = Paint().apply {
            color = Color.parseColor(highlight.color)
            style = Paint.Style.FILL
            alpha = 50
        }
        val selectionDetail = highlight.charDrawSegments
        selectionDetail.forEach { data ->
            canvas.drawRoundRect(data.rect.zoom(zoom), 2f, 2f, paint)
        }
    }

    private fun drawSignature(canvas: Canvas, zoom: Float, signature: SignatureModel) {
        val coords = signature.coordinates ?: return
        val signatureBitmap = signature.bitmap ?: return
        val left = (coords.startX * zoom).toFloat()
        val top = (coords.startY * zoom).toFloat()
        val right = (coords.endX * zoom).toFloat()
        val bottom = (coords.endY * zoom).toFloat()
        val destRect = RectF(left, top, right, bottom)
        canvas.drawBitmap(signatureBitmap, null, destRect, null)
    }

    fun findAnnotationOnPoint(paginationPageIndex: Int, point: PointF): PdfAnnotationModel? {
        for (annotation in annotations) {
            if (annotation.paginationPageIndex != paginationPageIndex) continue
            annotation.charDrawSegments.forEach {
                if (it.rect.contains(point.x, point.y)) {
                    return annotation
                }
            }
        }
        return null
    }

    fun findHighlightOnPoint(point: PointF): HighlightModel? {
        for (annotation in annotations) {
            if (annotation.type != PdfAnnotationModel.Type.Highlight) { continue }
            annotation.charDrawSegments.forEach {
                if (it.rect.contains(point.x, point.y)) {
                    return annotation.asHighlight()
                }
            }
        }
        return null
    }

    fun removeStrikeThroughAnnotation(noteIds: List<Long>) {
        annotations.removeAll {
            it.type == PdfAnnotationModel.Type.StrikeThrough && noteIds.contains(it.asStrikeThrough()!!.id)
        }
    }

    fun removeUnderlineAnnotation(noteIds: List<Long>) {
        annotations.removeAll {
            it.type == PdfAnnotationModel.Type.Underline && noteIds.contains(it.asUnderline()!!.id)
        }
    }

    fun removeHighlightAnnotation(highlightIds: List<Long>) {
        annotations.removeAll {
            it.type == PdfAnnotationModel.Type.Highlight && highlightIds.contains(it.asHighlight()!!.id)
        }
    }

    fun removeSignatureAnnotation(ids: List<Long>) {
        annotations.removeAll {
            it.type == PdfAnnotationModel.Type.Signature && ids.contains(it.asSignature()!!.id)
        }
    }

    fun getStrikeThroughAnnotation(noteId: Int): StrikeThroughModel? {
        return annotations.find { it.type == PdfAnnotationModel.Type.StrikeThrough && it.asStrikeThrough() != null && it.asStrikeThrough()!!.id.toInt() == noteId }?.asStrikeThrough()
    }

    fun getUnderlineAnnotation(noteId: Int): UnderlineModel? {
        return annotations.find { it.type == PdfAnnotationModel.Type.Underline && it.asUnderline() != null && it.asUnderline()!!.id.toInt() == noteId }?.asUnderline()
    }

    fun getDpValue(resource: Resources, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            resource.displayMetrics,
        ).toInt()
    }

    fun clearAllAnnotations() {
        annotations.clear()
    }

    data class AddedStampDetails(
        val y: Float,
        val noteIds: ArrayList<Int> = arrayListOf(),
    )

    companion object {
        private const val TAG = "AnnotationHandler"
    }
}
