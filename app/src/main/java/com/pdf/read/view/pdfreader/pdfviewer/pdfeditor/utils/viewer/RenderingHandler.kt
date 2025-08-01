package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer

import android.graphics.*
import android.os.*
import android.util.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.RenderingHandler.RenderingTask
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.exception.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*

class RenderingHandler internal constructor(looper: Looper?, private val pdfView: PDFView) :
    Handler(
        looper!!
    ) {
    private val renderBounds = RectF()
    private val roundedRenderBounds = Rect()
    private val renderMatrix = Matrix()
    private var running = false
    fun addRenderingTask(
        page: Int,
        width: Float,
        height: Float,
        bounds: RectF?,
        thumbnail: Boolean,
        cacheOrder: Int,
        bestQuality: Boolean,
        annotationRendering: Boolean
    ) {
        val task: RenderingTask = RenderingTask(
            width,
            height,
            bounds ?: RectF(),
            page,
            thumbnail,
            cacheOrder,
            bestQuality,
            annotationRendering
        )
        val msg = obtainMessage(MSG_RENDER_TASK, task)
        sendMessage(msg)
    }

    override fun handleMessage(message: Message) {
        val task = message.obj as RenderingTask
        task.annotationRendering
        try {
            val part = proceed(task)
            if (part != null) {
                if (running) {
                    pdfView.post { pdfView.onBitmapRendered(part) }
                } else {
                    part.renderedBitmap.recycle()
                }
            }
        } catch (ex: PageRenderingException) {
            pdfView.post { pdfView.onPageError(ex) }
        }
    }


    @Throws(PageRenderingException::class)
    private fun proceed(renderingTask: RenderingTask): PagePart? {
        try {
            var pdfFile = pdfView.pdfFile
            var pageIndex = renderingTask.page
            pdfFile!!.openPage(pageIndex)
            val w = Math.round(renderingTask.width)
            val h = Math.round(renderingTask.height)
            if (w == 0 || h == 0 || pdfFile.pageHasError(pageIndex)) {
                return null
            }
            val render: Bitmap
            render = try {
                Bitmap.createBitmap(
                    w,
                    h,
                    if (renderingTask.bestQuality) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                )
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Cannot create bitmap", e)
                return null
            }
            calculateBounds(w, h, renderingTask.bounds)
            pdfFile.renderPageBitmap(
                render,
                pageIndex,
                roundedRenderBounds,
                renderingTask.annotationRendering
            )
            return PagePart(
                renderingTask.page, render,
                renderingTask.bounds, renderingTask.thumbnail,
                renderingTask.cacheOrder
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun calculateBounds(width: Int, height: Int, pageSliceBounds: RectF) {
        renderMatrix.reset()
        renderMatrix.postTranslate(-pageSliceBounds.left * width, -pageSliceBounds.top * height)
        renderMatrix.postScale(1 / pageSliceBounds.width(), 1 / pageSliceBounds.height())
        renderBounds[0f, 0f, width.toFloat()] = height.toFloat()
        renderMatrix.mapRect(renderBounds)
        renderBounds.round(roundedRenderBounds)
    }

    fun stop() {
        running = false
    }

    fun start() {
        running = true
    }

    private inner class RenderingTask(
        var width: Float,
        var height: Float,
        var bounds: RectF,
        var page: Int,
        var thumbnail: Boolean,
        var cacheOrder: Int,
        var bestQuality: Boolean,
        var annotationRendering: Boolean
    ) {
        override fun toString(): String {
            return "width:$width ,height:$height ,bounds:$bounds ,page:$page ,thumbnail:$thumbnail ,cacheOrder:$cacheOrder ,bestQuality:$bestQuality ,annotationRendering:$annotationRendering , "
        }
    }

    companion object {
        /**
         * [Message.what] kind of message this handler processes.
         */
        const val MSG_RENDER_TASK = 1
        private val TAG = RenderingHandler::class.java.name
    }
}