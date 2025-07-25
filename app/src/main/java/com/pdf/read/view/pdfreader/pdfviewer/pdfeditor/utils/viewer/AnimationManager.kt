package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.PointF
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import androidx.core.animation.doOnEnd

internal class AnimationManager(private val pdfView: PDFView) {
    private var animation: ValueAnimator? = null
    private val scroller: OverScroller = OverScroller(pdfView.context)
    private var flinging = false
    private var pageFlinging = false

    fun startXAnimation(xFrom: Float, xTo: Float) {
        stopAll()
        animation = ValueAnimator.ofFloat(xFrom, xTo)
        val xAnimation = XAnimation()
        animation?.interpolator = DecelerateInterpolator()
        animation?.addUpdateListener(xAnimation)
        animation?.addListener(xAnimation)
        animation?.setDuration(400)
        animation?.start()
    }

    fun startYAnimation(yFrom: Float, yTo: Float) {
        stopAll()
        animation = ValueAnimator.ofFloat(yFrom, yTo)
        val yAnimation = YAnimation()
        animation?.interpolator = DecelerateInterpolator()
        animation?.addUpdateListener(yAnimation)
        animation?.addListener(yAnimation)
        animation?.setDuration(400)
        animation?.start()
    }

    fun startZoomAnimation(centerX: Float, centerY: Float, zoomFrom: Float, zoomTo: Float, onAnimationEnd: (() -> Unit)? = null) {
        stopAll()
        animation = ValueAnimator.ofFloat(zoomFrom, zoomTo)
        animation?.interpolator = DecelerateInterpolator()
        val zoomAnim = ZoomAnimation(centerX, centerY)
        animation?.addUpdateListener(zoomAnim)
        animation?.addListener(zoomAnim)
        animation?.setDuration(400)
        if (onAnimationEnd != null) {
            animation?.doOnEnd {
                onAnimationEnd.invoke()
            }
        }
        animation?.start()
    }

    fun startFlingAnimation(
        startX: Int,
        startY: Int,
        velocityX: Int,
        velocityY: Int,
        minX: Int,
        maxX: Int,
        minY: Int,
        maxY: Int,
    ) {
        stopAll()
        flinging = true
        scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
    }

    fun startPageFlingAnimation(targetOffset: Float) {
        if (pdfView.isSwipeVertical) {
            startYAnimation(pdfView.currentYOffset, targetOffset)
        } else {
            startXAnimation(pdfView.currentXOffset, targetOffset)
        }
        pageFlinging = true
    }

    fun computeFling() {
        if (scroller.computeScrollOffset()) {
            pdfView.moveTo(scroller.currX.toFloat(), scroller.currY.toFloat())
            pdfView.loadPageByOffset()
        } else if (flinging) {
            flinging = false
            pdfView.loadPages()
            hideHandle()
            pdfView.performPageSnap()
        }
    }

    fun stopAll() {
        if (animation != null) {
            animation!!.cancel()
            animation = null
        }
        stopFling()
    }

    fun stopFling() {
        flinging = false
        scroller.forceFinished(true)
    }

    fun isFlinging(): Boolean {
        return flinging || pageFlinging
    }

    internal inner class XAnimation : AnimatorListenerAdapter(), AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            val offset = animation.animatedValue as Float
            pdfView.moveTo(offset, pdfView.currentYOffset)
            pdfView.loadPageByOffset()
        }

        override fun onAnimationCancel(animation: Animator) {
            pdfView.loadPages()
            pageFlinging = false
            hideHandle()
        }

        override fun onAnimationEnd(animation: Animator) {
            pdfView.loadPages()
            pageFlinging = false
            hideHandle()
        }
    }

    internal inner class YAnimation : AnimatorListenerAdapter(), AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            val offset = animation.animatedValue as Float
            pdfView.moveTo(pdfView.currentXOffset, offset)
            pdfView.loadPageByOffset()
        }

        override fun onAnimationCancel(animation: Animator) {
            pdfView.loadPages()
            pageFlinging = false
            hideHandle()
        }

        override fun onAnimationEnd(animation: Animator) {
            pdfView.loadPages()
            pageFlinging = false
            hideHandle()
        }
    }

    internal inner class ZoomAnimation(private val centerX: Float, private val centerY: Float) :
        AnimatorUpdateListener, Animator.AnimatorListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            val zoom = animation.animatedValue as Float
            pdfView.zoomCenteredTo(zoom, PointF(centerX, centerY))
        }

        override fun onAnimationCancel(animation: Animator) {
            pdfView.loadPages()
            hideHandle()
        }

        override fun onAnimationEnd(animation: Animator) {
            pdfView.loadPages()
            pdfView.performPageSnap()
            hideHandle()
        }

        override fun onAnimationRepeat(animation: Animator) {}
        override fun onAnimationStart(animation: Animator) {}
    }

    private fun hideHandle() {
        if (pdfView.scrollHandle != null) {
            pdfView.scrollHandle?.hideDelayed()
        }
    }
}
