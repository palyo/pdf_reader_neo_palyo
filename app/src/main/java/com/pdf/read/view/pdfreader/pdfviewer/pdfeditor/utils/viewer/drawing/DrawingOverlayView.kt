package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.drawing

import android.content.*
import android.graphics.*
import android.util.*
import android.view.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*

class DrawingOverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val drawPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private data class BrushPoint(
        val x: Float,
        val y: Float,
        val width: Float
    )

    private inner class BrushPath {
        val path = Path()
        private val points = mutableListOf<BrushPoint>()
        private var lastControlPoint: PointF? = null

        fun moveTo(x: Float, y: Float) {
            points.clear()
            lastControlPoint = null
            path.moveTo(x, y)
            points.add(BrushPoint(x, y, STROKE_WIDTH))
        }

        fun addPoint(x: Float, y: Float) {
            val newPoint = BrushPoint(x, y, STROKE_WIDTH)
            points.add(newPoint)

            if (points.size >= 3) {
                val p1 = points[points.size - 3]
                val p2 = points[points.size - 2]
                val p3 = points[points.size - 1]

                if (lastControlPoint == null) {
                    lastControlPoint = PointF((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
                    path.lineTo(lastControlPoint!!.x, lastControlPoint!!.y)
                }
                val controlPoint1 = lastControlPoint!!
                val controlPoint2 = PointF((p2.x + p3.x) / 2, (p2.y + p3.y) / 2)

                path.cubicTo(
                    controlPoint1.x, controlPoint1.y,
                    p2.x, p2.y,
                    controlPoint2.x, controlPoint2.y
                )

                lastControlPoint = controlPoint2
            }
        }

        fun getLastPoint(): BrushPoint? = points.lastOrNull()
        fun getWidth(): Float = STROKE_WIDTH
    }

    private val paths = mutableListOf<BrushPath>()
    private var currentPath: BrushPath? = null

    companion object {
        private const val STROKE_WIDTH = 10f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = BrushPath().apply {
                    moveTo(x, y)
                }
                drawPaint.strokeWidth = STROKE_WIDTH
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                currentPath?.apply {
                    addPoint(x, y)
                    drawPaint.strokeWidth = STROKE_WIDTH
                }
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                currentPath?.apply {
                    addPoint(x, y)
                }
                currentPath?.let { paths.add(it) }
                currentPath = null
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (brushPath in paths) {
            drawPaint.strokeWidth = STROKE_WIDTH
            canvas.drawPath(brushPath.path, drawPaint)
        }
        currentPath?.let {
            drawPaint.strokeWidth = STROKE_WIDTH
            canvas.drawPath(it.path, drawPaint)
        }
    }

    private fun getDrawingBoundingRect(): RectF? {
        var unionRect: RectF? = null
        for (brushPath in paths) {
            val bounds = RectF()
            brushPath.path.computeBounds(bounds, true)
            if (unionRect == null) {
                unionRect = RectF(bounds)
            } else {
                unionRect.union(bounds)
            }
        }
        currentPath?.let {
            val bounds = RectF()
            it.path.computeBounds(bounds, true)
            if (unionRect == null) {
                unionRect = RectF(bounds)
            } else {
                unionRect?.union(bounds)
            }
        }
        return unionRect
    }

    fun getCroppedDrawingBitmap(): Pair<Bitmap, Coordinates>? {
        val bounds = getDrawingBoundingRect() ?: return null
        val fullBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(fullBitmap)
        draw(canvas)
        val padding = 10

        val cropLeft = (bounds.left - padding).toInt().coerceAtLeast(0)
        val cropTop = (bounds.top - padding).toInt().coerceAtLeast(0)
        val cropRight = (bounds.right + padding).toInt().coerceAtMost(width)
        val cropBottom = (bounds.bottom + padding).toInt().coerceAtMost(height)

        val cropWidth = cropRight - cropLeft
        val cropHeight = cropBottom - cropTop

        val croppedBitmap = Bitmap.createBitmap(fullBitmap, cropLeft, cropTop, cropWidth, cropHeight)
        val coordinates = Coordinates(
            startX = cropLeft.toDouble(),
            startY = cropTop.toDouble(),
            endX = cropRight.toDouble(),
            endY = cropBottom.toDouble()
        )

        return Pair(croppedBitmap, coordinates)
    }


    fun convertDrawingViewRectToPdfRect(
        viewRect: RectF,
        drawingViewHeight: Float,
        pdfPageHeight: Float,
        scaleX: Float,
        scaleY: Float
    ): RectF {
        val pdfLeft = viewRect.left * scaleX
        val pdfRight = viewRect.right * scaleX
        val scaledTop = viewRect.top * scaleY
        val scaledHeight = viewRect.height() * scaleY
        val pdfBottom = pdfPageHeight - (scaledTop + scaledHeight)
        val pdfTop = pdfPageHeight - scaledTop

        return RectF(pdfLeft, pdfBottom, pdfRight, pdfTop)
    }

    fun setStrokeWidth(width: Float) {
        drawPaint.strokeWidth = width
    }

    fun clearDrawing() {
        paths.clear()
        currentPath = null
        invalidate()
    }
}
