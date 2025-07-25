package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer

import android.annotation.SuppressLint
import android.graphics.PointF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.LinkTapEvent
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.Constants

@SuppressLint("ClickableViewAccessibility")
internal class DragPinchManager(
    private val pdfView: PDFView,
    private val animationManager: AnimationManager,
    private val listener: Listener,
) : GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    ScaleGestureDetector.OnScaleGestureListener,
    View.OnTouchListener {

    var disableHorizontalScroll = true

    private val gestureDetector: GestureDetector = GestureDetector(pdfView.context, this)
    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(pdfView.context, this)
    private var scrolling = false
    private var scaling = false
    private var enabled = false

    companion object {
        private const val TAG = "DragPinchManager"
    }

    init {
        pdfView.setOnTouchListener(this)
    }

    fun isScrolling(): Boolean {
        return scrolling
    }

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }

    fun disableLongpress() {
        gestureDetector.setIsLongpressEnabled(false)
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        val onTapHandled = pdfView.callbacks.callOnTap(e)
        listener.onTap(e)
        val linkTapped = checkLinkTapped(e.x, e.y)
        if (!onTapHandled && !linkTapped) {
            val ps = pdfView.scrollHandle
            if (ps != null && !pdfView.documentFitsView()) {
                if (!ps.shown()) {
                    ps.show()
                } else {
                    ps.hide()
                }
            }
        }
        pdfView.performClick()
        return true
    }

    private fun checkLinkTapped(x: Float, y: Float): Boolean {
        val pdfFile = pdfView.pdfFile ?: return false
        val mappedX = -pdfView.currentXOffset + x
        val mappedY = -pdfView.currentYOffset + y
        val page = pdfFile.getPageAtOffset(
            if (pdfView.isSwipeVertical) mappedY else mappedX,
            pdfView.zoom
        )
        val pageSize = pdfFile.getScaledPageSize(page, pdfView.zoom)
        val pageX: Int
        val pageY: Int
        if (pdfView.isSwipeVertical) {
            pageX = pdfFile.getSecondaryPageOffset(page, pdfView.zoom).toInt()
            pageY = pdfFile.getPageOffsetWithZoom(page, pdfView.zoom).toInt()
        } else {
            pageY = pdfFile.getSecondaryPageOffset(page, pdfView.zoom).toInt()
            pageX = pdfFile.getPageOffsetWithZoom(page, pdfView.zoom).toInt()
        }
        for (link in pdfFile.getPageLinks(page)) {
            val mapped = pdfFile.mapRectToDevice(
                page,
                pageX,
                pageY,
                pageSize.width.toInt(),
                pageSize.height.toInt(),
                link.bounds,
            )
            mapped.sort()
            if (mapped.contains(mappedX, mappedY)) {
                pdfView.callbacks.callLinkHandler(
                    LinkTapEvent(
                        x,
                        y,
                        mappedX,
                        mappedY,
                        mapped,
                        link,
                    ),
                )
                return true
            }
        }
        return false
    }

    private fun startPageFling(
        downEvent: MotionEvent,
        ev: MotionEvent,
        velocityX: Float,
        velocityY: Float,
    ) {
        if (!checkDoPageFling(velocityX, velocityY)) {
            return
        }
        val direction: Int = if (pdfView.isSwipeVertical) {
            if (velocityY > 0) -1 else 1
        } else {
            if (velocityX > 0) -1 else 1
        }
        // Get the focused page during the down event to ensure only a single page is changed
        val delta = if (pdfView.isSwipeVertical) ev.y - downEvent.y else ev.x - downEvent.x
        val offsetX = pdfView.currentXOffset - delta * pdfView.zoom
        val offsetY = pdfView.currentYOffset - delta * pdfView.zoom
        val startingPage = pdfView.findFocusPage(offsetX, offsetY)
        val targetPage = Math.max(0, Math.min(pdfView.pageCount - 1, startingPage + direction))
        val edge = pdfView.findSnapEdge(targetPage)
        val offset = pdfView.snapOffsetForPage(targetPage, edge)
        animationManager.startPageFlingAnimation(-offset)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        if (!pdfView.isDoubleTapEnabled) {
            return false
        }
        if (pdfView.zoom < pdfView.midZoom) {
            pdfView.zoomWithAnimation(e.x, e.y, pdfView.midZoom)
        } else if (pdfView.zoom < pdfView.maxZoom) {
            pdfView.zoomWithAnimation(e.x, e.y, pdfView.maxZoom)
        } else {
            pdfView.resetZoomWithAnimation()
        }
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent): Boolean {
        animationManager.stopFling()
        return true
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float,
    ): Boolean {
        if (!scrolling) {
            listener.onScrollingBegin()
        }
        scrolling = true
        // When horizontal scroll is disabled, ignore the horizontal delta.
        val deltaX = if (disableHorizontalScroll) 0f else -distanceX
        pdfView.moveRelativeTo(deltaX, -distanceY)
        return true
    }

    private fun onScrollEnd(event: MotionEvent) {
        pdfView.loadPages()
        hideHandle()
        if (!animationManager.isFlinging()) {
            pdfView.performPageSnap()
        }
    }

    override fun onLongPress(e: MotionEvent) {
        listener.onLongPressed(e.x, e.y)
        pdfView.callbacks.callOnLongPress(e)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float,
    ): Boolean {
        if (!pdfView.isSwipeEnabled) {
            return false
        }
        if (pdfView.isPageFlingEnabled) {
            if (disableHorizontalScroll) {
                val xOffset = pdfView.currentXOffset.toInt()
                val yOffset = pdfView.currentYOffset.toInt()
                // Compute vertical bounds for fling when horizontal scrolling is locked.
                val pdfFile = pdfView.pdfFile
                val minY: Float
                val maxY: Float
                if (pdfView.isSwipeVertical) {
                    val pageStart = -pdfFile!!.getPageOffsetWithZoom(pdfView.currentPage, pdfView.zoom)
                    val pageEnd = pageStart - pdfFile.getPageLength(pdfView.currentPage, pdfView.zoom)
                    minY = pageEnd + pdfView.height
                    maxY = pageStart
                } else {
                    minY = 0f
                    maxY = 0f
                }
                animationManager.startFlingAnimation(
                    xOffset,
                    yOffset,
                    0, // ignore horizontal velocity
                    velocityY.toInt(),
                    xOffset, // fix x bounds to current xOffset
                    xOffset,
                    minY.toInt(),
                    maxY.toInt(),
                )
                return true
            } else {
                if (pdfView.pageFillsScreen()) {
                    onBoundedFling(velocityX, velocityY)
                } else {
                    startPageFling(e1!!, e2, velocityX, velocityY)
                }
                return true
            }
        }
        // Normal fling animation when page fling is disabled.
        val xOffset = pdfView.currentXOffset.toInt()
        val yOffset = pdfView.currentYOffset.toInt()
        val pdfFile = pdfView.pdfFile
        val minX: Float
        val minY: Float
        if (pdfView.isSwipeVertical) {
            minX = if (disableHorizontalScroll) xOffset.toFloat()
            else -(pdfView.toCurrentScale(pdfFile!!.maxPageWidth) - pdfView.width)
            minY = -(pdfFile!!.getDocLen(pdfView.zoom) - pdfView.height)
        } else {
            minX = -(pdfFile!!.getDocLen(pdfView.zoom) - pdfView.width)
            minY = if (disableHorizontalScroll) yOffset.toFloat()
            else -(pdfView.toCurrentScale(pdfFile.maxPageHeight) - pdfView.height)
        }
        animationManager.startFlingAnimation(
            xOffset,
            yOffset,
            if (disableHorizontalScroll) 0 else velocityX.toInt(),
            velocityY.toInt(),
            minX.toInt(),
            if (disableHorizontalScroll) xOffset else 0,
            minY.toInt(),
            if (disableHorizontalScroll) yOffset else 0,
        )
        return true
    }

    private fun onBoundedFling(velocityX: Float, velocityY: Float) {
        val xOffset = pdfView.currentXOffset.toInt()
        val yOffset = pdfView.currentYOffset.toInt()
        val pdfFile = pdfView.pdfFile
        val pageStart = -pdfFile!!.getPageOffsetWithZoom(pdfView.currentPage, pdfView.zoom)
        val pageEnd = pageStart - pdfFile.getPageLength(pdfView.currentPage, pdfView.zoom)
        val minX: Float
        val minY: Float
        val maxX: Float
        val maxY: Float
        if (pdfView.isSwipeVertical) {
            minX = -(pdfView.toCurrentScale(pdfFile.maxPageWidth) - pdfView.width)
            minY = pageEnd + pdfView.height
            maxX = 0f
            maxY = pageStart
        } else {
            minX = pageEnd + pdfView.width
            minY = -(pdfView.toCurrentScale(pdfFile.maxPageHeight) - pdfView.height)
            maxX = pageStart
            maxY = 0f
        }
        animationManager.startFlingAnimation(
            xOffset,
            yOffset,
            velocityX.toInt(),
            velocityY.toInt(),
            minX.toInt(),
            maxX.toInt(),
            minY.toInt(),
            maxY.toInt(),
        )
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        var dr = detector.scaleFactor
        val wantedZoom = pdfView.zoom * dr
        val minZoom = Math.min(Constants.Pinch.MINIMUM_ZOOM, pdfView.minZoom)
        val maxZoom = Math.min(Constants.Pinch.MAXIMUM_ZOOM, pdfView.maxZoom)
        if (wantedZoom < minZoom) {
            dr = minZoom / pdfView.zoom
        } else if (wantedZoom > maxZoom) {
            dr = maxZoom / pdfView.zoom
        }
        pdfView.zoomCenteredRelativeTo(dr, PointF(detector.focusX, detector.focusY))
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        scaling = true
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        pdfView.loadPages()
        hideHandle()
        scaling = false
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!enabled) {
            return false
        }
        if (listener.onTouch(event)) {
            return true
        }

        var retVal = scaleGestureDetector.onTouchEvent(event)
        retVal = gestureDetector.onTouchEvent(event) || retVal
        if (event.action == MotionEvent.ACTION_UP) {
            if (scrolling) {
                scrolling = false
                onScrollEnd(event)
            }
        }
        return retVal
    }

    private fun hideHandle() {
        val scrollHandle = pdfView.scrollHandle
        if (scrollHandle != null && scrollHandle.shown()) {
            scrollHandle.hideDelayed()
        }
    }

    private fun checkDoPageFling(velocityX: Float, velocityY: Float): Boolean {
        val absX = Math.abs(velocityX)
        val absY = Math.abs(velocityY)
        return if (pdfView.isSwipeVertical) absY > absX else absX > absY
    }

    interface Listener {
        fun onLongPressed(x: Float, y: Float)
        fun onTouch(e: MotionEvent): Boolean
        fun onTap(e: MotionEvent)
        fun onScrollingBegin()
        fun onScrollingEnd()
    }
}
