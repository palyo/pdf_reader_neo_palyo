package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer

import android.annotation.*
import android.content.*
import android.graphics.*
import android.graphics.Paint.Style
import android.os.*
import android.util.*
import android.view.*
import android.widget.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.annotation.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.exception.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.link.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.listener.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.scroll.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.selection.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.source.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.*
import com.shockwave.pdfium.*
import com.shockwave.pdfium.PdfDocument.Bookmark
import com.shockwave.pdfium.PdfDocument.Meta
import com.shockwave.pdfium.util.Size
import com.shockwave.pdfium.util.SizeF
import kotlinx.coroutines.*
import java.io.*

class PDFView(context: Context?, set: AttributeSet?) :
    RelativeLayout(context, set),
    DragPinchManager.Listener,
    PdfFile.MergePdfListener {
    var minZoom = DEFAULT_MIN_SCALE
    var midZoom = DEFAULT_MID_SCALE
    var maxZoom = DEFAULT_MAX_SCALE

    internal enum class ScrollDir {
        NONE, START, END
    }

    private var scrollDir = ScrollDir.NONE
    var cacheManager: CacheManager? = null
    private var animationManager: AnimationManager? = null
    private var dragPinchManager: DragPinchManager? = null
    var pdfFile: PdfFile? = null
    var file: File? = null
    var currentPage = 0
        private set
    var currentXOffset = 0f
        private set
    var currentYOffset = 0f
        private set
    var zoom = 1f
        private set
    private var isRecycled = true
    private var state = State.DEFAULT

    enum class MergeState { TOP_MERGING_LOADING, BOTTOM_MERGE_LOADING, IDLE }

    private var mergeState: MergeState = MergeState.IDLE
    private var currentMergeId: Int = 1
    private var totalPdfPage = -1
    private var pdfDecoder: PdfDecoder? = null
    private var renderingHandlerThread: HandlerThread?
    var renderingHandler: RenderingHandler? = null
    private var pagesLoader: PagesLoader? = null

    @JvmField
    var callbacks = Callbacks()
    private var paint: Paint = Paint()
    private var debugPaint: Paint = Paint()
    var pageFitPolicy = FitPolicy.BOTH
        private set
    var isFitEachPage = true
        private set
    private var defaultPage = 0
    var isSwipeVertical = true
        private set
    var isSwipeEnabled = true
    var isDoubleTapEnabled = true
        private set
    private var nightMode = false
    var isPageSnap = true
    private var pdfiumCore: PdfiumCore? = null
    var scrollHandle: ScrollHandle? = null
        private set
    private var isScrollHandleInit = false
    var isBestQuality = true
        private set
    var isAnnotationRendering = true
        private set
    private var renderDuringScale = false
    var isAntialiasing = true
        private set
    private val antialiasFilter =
        PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    var spacingPx = 0
        private set
    var isAutoSpacingEnabled = false
        private set
    var isPageFlingEnabled = true
        private set
    private val onDrawPagesNumbs: MutableList<Int> = ArrayList(10)
    private var hasSize = false
    private var waitingDocumentConfigurator: Configurator? = null
    private var vibratorManager: VibratorManager? = null
    private var vibrator: Vibrator? = null
    private var textSelectionHelper = PdfTextSelectionHelper()
    val textSelection = TextSelectionData()
    private var listener: Listener? = null
    private var scope: CoroutineScope? = null
    private val annotationHandler = PdfAnnotationHandler(resources)
    private val pageDivisionPaint = Paint().apply {
        this.color = Color.TRANSPARENT
        this.strokeWidth = 2f
        this.style = Style.STROKE
    }

    init {
        renderingHandlerThread = HandlerThread("PDF renderer")
        if (!isInEditMode) {
            cacheManager = CacheManager()
            animationManager = AnimationManager(this)
            dragPinchManager = DragPinchManager(this, animationManager!!, this)
            pagesLoader = PagesLoader(this)
            debugPaint.style = Style.STROKE
            pdfiumCore = PdfiumCore(context)
            setWillNotDraw(false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibratorManager = context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        } else {
            vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        }
    }

    private fun getMergeId(): Int {
        currentMergeId += 1
        return currentMergeId
    }

    @SuppressLint("NewApi")
    private fun vibrate(seconds: Long = 50L) {
        if (vibratorManager != null) {
            val vibe = VibrationEffect.createOneShot(seconds, VibrationEffect.DEFAULT_AMPLITUDE)
            vibratorManager?.defaultVibrator?.vibrate(vibe)
        } else if (vibrator != null && vibrator?.hasVibrator() == true) {
            val vibe = VibrationEffect.createOneShot(seconds, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator?.vibrate(vibe)
        }
    }

    fun setListener(listener: Listener): PDFView {
        this.listener = listener
        return this
    }

    fun setTotalPdfPageCount(pageCount: Int): PDFView {
        this.totalPdfPage = pageCount
        return this
    }

    fun attachCoroutineScope(scope: CoroutineScope): PDFView {
        this.scope = scope
        return this
    }

    private fun load(docSource: DocumentSource, password: String?) {
        check(isRecycled) { "Don't call load on a PDF View without recycling it first." }
        listener?.onPreparationStarted()
        isRecycled = false
        pdfDecoder = PdfDecoder(this, pdfiumCore)
        scope?.launch(Dispatchers.IO) {
            pdfDecoder?.decode(docSource, password)
        }
    }

    @JvmOverloads
    fun jumpTo(page: Int, withAnimation: Boolean = false, resetZoom: Boolean = false, resetHorizontalScroll: Boolean = false) {
        var mPage = page
        if (pdfFile == null) {
            return
        }
        if (resetZoom) {
            zoom = 1f
        }
        if (resetHorizontalScroll) {
            currentXOffset = 0f
        }
        mPage = pdfFile!!.determineValidPageNumberFrom(mPage)
        val offset: Float = if (mPage == 0) 0f else -pdfFile!!.getPageOffsetWithZoom(mPage, zoom)
        if (isSwipeVertical) {
            if (withAnimation) {
                animationManager?.startYAnimation(currentYOffset, offset)
            } else {
                moveTo(currentXOffset, offset)
            }
        } else {
            if (withAnimation) {
                animationManager?.startXAnimation(currentXOffset, offset)
            } else {
                moveTo(offset, currentYOffset)
            }
        }
        showPage(mPage)
    }

    fun jumpToWithPaginationPageIndex(paginationPageIndex: Int, withAnimation: Boolean = false, resetZoom: Boolean = false, resetHorizontalScroll: Boolean = false) {
        if (pdfFile == null) {
            return
        }
        val page = pdfFile!!.getPageIndexFromPaginationIndex(paginationPageIndex)
        if (page >= 0) {
            jumpTo(page, withAnimation, resetZoom, resetHorizontalScroll)
        }
    }

    fun showPage(page: Int) {
        var pageNb = page
        if (isRecycled) {
            return
        }
        // Check the page number and makes the
        // difference between UserPages and DocumentPages
        pageNb = pdfFile!!.determineValidPageNumberFrom(pageNb)
        currentPage = pageNb
        loadPages()
        if (scrollHandle != null && !documentFitsView()) {
            scrollHandle!!.setPageNum(currentPage + 1)
        }
        callbacks.callOnPageChange(currentPage, pdfFile!!.pagesCount)
        scope?.launch(Dispatchers.Main) { listener?.onPageChanged(currentPage, pdfFile!!.getPaginationIndexFromPageIndex(currentPage)) }
    }

    var positionOffset: Float
        get() {
            val offset: Float = if (isSwipeVertical) {
                -currentYOffset / (pdfFile!!.getDocLen(zoom) - height)
            } else {
                -currentXOffset / (pdfFile!!.getDocLen(zoom) - width)
            }
            return MathUtils.limit(offset, 0f, 1f)
        }
        set(progress) {
            setPositionOffset(progress, true)
        }

    fun setPositionOffset(progress: Float, moveHandle: Boolean) {
        if (isSwipeVertical) {
            moveTo(currentXOffset, (-pdfFile!!.getDocLen(zoom) + height) * progress, moveHandle)
        } else {
            moveTo((-pdfFile!!.getDocLen(zoom) + width) * progress, currentYOffset, moveHandle)
        }
        loadPageByOffset()
    }

    fun stopFling() {
        animationManager?.stopFling()
    }

    val pageCount: Int
        get() = if (pdfFile == null) {
            0
        } else {
            pdfFile!!.pagesCount
        }

    fun setNightMode(nightMode: Boolean) {
        this.nightMode = nightMode
        if (nightMode) {
            val colorMatrixInverted = ColorMatrix(
                floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            )
            val filter = ColorMatrixColorFilter(colorMatrixInverted)
            paint.colorFilter = filter
        } else {
            paint.colorFilter = null
        }
    }

    fun enableDoubleTap(enableDoubleTap: Boolean) {
        isDoubleTapEnabled = enableDoubleTap
    }

    fun enableSwipeHorizontal(enableSwipe: Boolean) {
        dragPinchManager?.disableHorizontalScroll = enableSwipe
    }

    fun onPageError(ex: PageRenderingException) {
        if (!callbacks.callOnPageError(ex.page, ex.cause)) {
            Log.e(TAG, "Cannot open page " + ex.page, ex.cause)
        }
    }

    fun recycle() {
        waitingDocumentConfigurator = null
        animationManager?.stopAll()
        dragPinchManager?.disable()
        // Stop tasks
        if (renderingHandler != null) {
            renderingHandler?.stop()
            renderingHandler?.removeMessages(RenderingHandler.MSG_RENDER_TASK)
        }

        pdfDecoder?.cancel()
        // Clear caches
        cacheManager?.recycle()
        if (scrollHandle != null && isScrollHandleInit) {
            scrollHandle?.destroyLayout()
        }
        if (pdfFile != null) {
            pdfFile?.dispose()
            pdfFile = null
        }
        renderingHandler = null
        scrollHandle = null
        isScrollHandleInit = false
        currentYOffset = 0f
        currentXOffset = currentYOffset
        zoom = 1f
        isRecycled = true
        callbacks = Callbacks()
        state = State.DEFAULT
        mergeState = MergeState.IDLE
    }

    override fun computeScroll() {
        super.computeScroll()
        if (isInEditMode) {
            return
        }
        animationManager?.computeFling()
    }

    override fun onDetachedFromWindow() {
        recycle()
        if (renderingHandlerThread != null) {
            renderingHandlerThread!!.quitSafely()
            renderingHandlerThread = null
        }
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        hasSize = true
        if (waitingDocumentConfigurator != null) {
            waitingDocumentConfigurator!!.load()
        }
        if (isInEditMode || state != State.SHOWN) {
            return
        }
        // calculates the position of the point which in the center of view relative to big strip
        val centerPointInStripXOffset = -currentXOffset + oldw * 0.5f
        val centerPointInStripYOffset = -currentYOffset + oldh * 0.5f
        val relativeCenterPointInStripXOffset: Float
        val relativeCenterPointInStripYOffset: Float
        if (isSwipeVertical) {
            relativeCenterPointInStripXOffset = centerPointInStripXOffset / pdfFile!!.maxPageWidth
            relativeCenterPointInStripYOffset =
                centerPointInStripYOffset / pdfFile!!.getDocLen(zoom)
        } else {
            relativeCenterPointInStripXOffset =
                centerPointInStripXOffset / pdfFile!!.getDocLen(zoom)
            relativeCenterPointInStripYOffset = centerPointInStripYOffset / pdfFile!!.maxPageHeight
        }
        animationManager?.stopAll()
        pdfFile!!.recalculatePageSizes(Size(w, h))
        if (isSwipeVertical) {
            currentXOffset = -relativeCenterPointInStripXOffset * pdfFile!!.maxPageWidth + w * 0.5f
            currentYOffset =
                -relativeCenterPointInStripYOffset * pdfFile!!.getDocLen(zoom) + h * 0.5f
        } else {
            currentXOffset =
                -relativeCenterPointInStripXOffset * pdfFile!!.getDocLen(zoom) + w * 0.5f
            currentYOffset = -relativeCenterPointInStripYOffset * pdfFile!!.maxPageHeight + h * 0.5f
        }
        moveTo(currentXOffset, currentYOffset)
        loadPageByOffset()
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        if (pdfFile == null) {
            return true
        }
        if (isSwipeVertical) {
            if (direction < 0 && currentXOffset < 0) {
                return true
            } else if (direction > 0 && currentXOffset + toCurrentScale(pdfFile!!.maxPageWidth) > width) {
                return true
            }
        } else {
            if (direction < 0 && currentXOffset < 0) {
                return true
            } else if (direction > 0 && currentXOffset + pdfFile!!.getDocLen(zoom) > width) {
                return true
            }
        }
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        if (pdfFile == null) {
            return true
        }
        if (isSwipeVertical) {
            if (direction < 0 && currentYOffset < 0) {
                return true
            } else if (direction > 0 && currentYOffset + pdfFile!!.getDocLen(zoom) > height) {
                return true
            }
        } else {
            if (direction < 0 && currentYOffset < 0) {
                return true
            } else if (direction > 0 && currentYOffset + toCurrentScale(pdfFile!!.maxPageHeight) > height) {
                return true
            }
        }
        return false
    }

    override fun onLongPressed(x: Float, y: Float) {
        findWordInSelectedPoint(x, y)
        redraw()
    }

    private var touchInPoint: PointF? = null

    override fun onTouch(e: MotionEvent): Boolean {
        val yOffset = e.y - currentYOffset
        val pageIndex = pdfFile!!.getPageAtOffset(yOffset, zoom)
        val pageOffset = pdfFile!!.getPageOffset(pageIndex)
        val mX = e.x.scaledX() / zoom
        val mY = (e.y.scaledY() / zoom) - pageOffset
        val point = PointF(mX, mY)
        val touchYPointTop = 2 // (2 / zoom).coerceAtLeast(1f)
        val selectionPoint = when (textSelectionHelper.touchState) {
            PdfTextSelectionHelper.TouchState.StartHandlePressed -> PointF(
                mX,
                mY - (textSelectionHelper.handleRoundRadius * touchYPointTop),
            )

            PdfTextSelectionHelper.TouchState.EndHandlePressed -> PointF(
                mX - textSelectionHelper.handleRoundRadius,
                mY - (textSelectionHelper.handleRoundRadius * touchYPointTop),
            )

            PdfTextSelectionHelper.TouchState.IDLE -> PointF(mX, mY - (textSelectionHelper.handleRoundRadius * 3))
        }
        var touchConsumed = false
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                touchInPoint = point
                if (CanvasUtils.isCircleCollided(
                        mX,
                        mY,
                        textSelectionHelper.handleRoundRadius,
                        textSelectionHelper.startHandlePosition.x,
                        textSelectionHelper.startHandlePosition.y,
                        textSelectionHelper.handleRoundRadius,
                    )
                ) {
                    textSelectionHelper.touchState =
                        PdfTextSelectionHelper.TouchState.StartHandlePressed
                    touchConsumed = true
                } else if (CanvasUtils.isCircleCollided(
                        mX,
                        mY,
                        textSelectionHelper.handleRoundRadius,
                        textSelectionHelper.endHandlePosition.x,
                        textSelectionHelper.endHandlePosition.y,
                        textSelectionHelper.handleRoundRadius,
                    )
                ) {
                    textSelectionHelper.touchState =
                        PdfTextSelectionHelper.TouchState.EndHandlePressed
                    touchConsumed = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when (textSelectionHelper.touchState) {
                    PdfTextSelectionHelper.TouchState.StartHandlePressed -> {
                        touchConsumed = true
                        findCharactersInTheSelectedPositions(
                            selectionPoint,
                            textSelectionHelper.endSelectionPosition,
                        )
                    }

                    PdfTextSelectionHelper.TouchState.EndHandlePressed -> {
                        touchConsumed = true
                        findCharactersInTheSelectedPositions(
                            textSelectionHelper.startSelectionPosition,
                            selectionPoint,
                        )
                    }

                    else -> {}
                }
            }

            MotionEvent.ACTION_UP -> {
                when (textSelectionHelper.touchState) {
                    PdfTextSelectionHelper.TouchState.StartHandlePressed -> {
                        findCharactersInTheSelectedPositions(
                            selectionPoint,
                            textSelectionHelper.endSelectionPosition,
                        )
                        textSelectionHelper.touchState = PdfTextSelectionHelper.TouchState.IDLE
                        touchConsumed = true
                    }

                    PdfTextSelectionHelper.TouchState.EndHandlePressed -> {
                        findCharactersInTheSelectedPositions(
                            textSelectionHelper.startSelectionPosition,
                            selectionPoint,
                        )
                        textSelectionHelper.touchState = PdfTextSelectionHelper.TouchState.IDLE
                        touchConsumed = true
                    }

                    else -> {
                    }
                }
                // Passing selected text details through listener
                if (textSelection.hasTextSelected() && dragPinchManager?.isScrolling() != true) {
                    onTextSelected()
                } else if (!textSelection.hasTextSelected()) {
                    // if there is no text selected
                    clearAllTextSelectionAndCoordinates()
                }
            }
        }
        return touchConsumed
    }

    private fun onTextSelected() {
        val page = pdfFile!!.getPageIndexFromPaginationIndex(textSelection.currentSelectionPaginationIndex)
        val currentOffset = pdfFile!!.getPageCurrentOffset(page, zoom, currentYOffset) // getting current y offset of the page that have this notes
        val firstSelection = textSelection.getSelections().firstOrNull()
        if (firstSelection != null) {
            val x = (firstSelection.rect.left)
            val y = currentOffset + (firstSelection.rect.top - firstSelection.rect.height()) * zoom
            val point = PointF(x, y)
            listener?.onTextSelected(textSelection, point)
        }
    }

    override fun onTap(e: MotionEvent) {
        clearAllTextSelection(true)
        listener?.onTap()
    }

    override fun onScrollingBegin() {
        listener?.hideTextSelectionOptionWindow()
    }

    override fun onScrollingEnd() {
        if (textSelection.hasTextSelected()) {
            onTextSelected()
        }
    }

    private fun findWordInSelectedPoint(x: Float, y: Float) {
        val yOffset = y - currentYOffset
        val pageIndex = pdfFile!!.getPageAtOffset(yOffset, zoom)
        val pageOffset = pdfFile!!.getPageOffset(pageIndex)
        clearAllTextSelectionAndCoordinates()
        textSelection.currentSelectionPageIndex = pageIndex
        textSelection.currentSelectionPaginationIndex = pdfFile!!.getPaginationIndexFromPageIndex(pageIndex)
        val mX = x.scaledX() / zoom
        val mY = y.scaledY() / zoom
        val selectedWord = getWordInPoint(pageIndex, mX, mY - pageOffset)

        if (selectedWord != null) {
            textSelection.addSelection(selectedWord.characters)
            vibrate()
        }
    }

    private fun getWordInPoint(pageIndex: Int, x: Float, y: Float): PdfWord? {
        pdfFile?.pageDetails?.getOrNull(pageIndex)?.let { page ->
            for (line in page.coordinates) {
                for (word in line.words) {
                    if (word.rect.contains(x, y)) {
                        return word
                    }
                }
            }
        }
        return null
    }

    private fun rearrangeStartEndPoints(startPoint: PointF, endPoint: PointF): Boolean {
        when (textSelectionHelper.touchState) {
            PdfTextSelectionHelper.TouchState.StartHandlePressed -> {
                if (startPoint.y > endPoint.y + textSelectionHelper.endCharHeight / 2) {
                    if (startPoint.y <= endPoint.y + textSelectionHelper.endCharHeight / 2 && startPoint.y >= endPoint.y - textSelectionHelper.endCharHeight / 2) {
                        if (startPoint.x > endPoint.x) {
                            textSelectionHelper.touchState =
                                PdfTextSelectionHelper.TouchState.EndHandlePressed
                            return true
                        }
                    } else {
                        textSelectionHelper.touchState =
                            PdfTextSelectionHelper.TouchState.EndHandlePressed
                        return true
                    }
                }
            }

            PdfTextSelectionHelper.TouchState.EndHandlePressed -> {
                if (endPoint.y < startPoint.y - textSelectionHelper.startCharHeight / 2) {
                    if (endPoint.y <= startPoint.y + textSelectionHelper.startCharHeight / 2 && endPoint.y >= startPoint.y - textSelectionHelper.startCharHeight / 2) {
                        if (startPoint.x > endPoint.x) {
                            textSelectionHelper.touchState =
                                PdfTextSelectionHelper.TouchState.StartHandlePressed
                            return true
                        }
                    } else {
                        textSelectionHelper.touchState =
                            PdfTextSelectionHelper.TouchState.StartHandlePressed
                        return true
                    }
                }
            }

            else -> {}
        }

        return false
    }

    private fun findCharactersInTheSelectedPositions(startPoint: PointF, endPoint: PointF) {
        val page = textSelection.currentSelectionPageIndex
        clearAllTextSelection(false)
        val selectedLines = arrayListOf<CharDrawSegments>()
        var mStartPoint = startPoint
        var mEndPoint = endPoint
        val swapPoints = rearrangeStartEndPoints(startPoint, endPoint)
        if (swapPoints) {
            val tempStart = mStartPoint
            mStartPoint = mEndPoint
            mEndPoint = tempStart
        }

        pdfFile?.pageDetails?.getOrNull(page)?.let {
            it.coordinates.forEach { line ->
                // extracting characters inside the selection points
                // Checking all the lines that between start and end point
                if ((line.relatedPosition.y + line.relatedSize.height) >= mStartPoint.y && line.relatedPosition.y <= mEndPoint.y) {
                    // Checking startPoint and endPoint are in the same line or not
                    // if they are in same line , we need to check the chars that
                    // are appear between x axis
                    if (
                        mStartPoint.y >= line.relatedPosition.y && mStartPoint.y <= (line.relatedPosition.y + line.relatedSize.height) &&
                        mEndPoint.y >= line.relatedPosition.y && mEndPoint.y <= (line.relatedPosition.y + line.relatedSize.height)
                    ) {
                        val chars = arrayListOf<PdfChar>()
                        line.words.forEach { word -> chars.addAll(word.characters) }
                        val filteredCharsFirst = arrayListOf<PdfChar>()
                        chars.forEach { char ->
                            // if characters between startX and endX add the character in selected text
                            if (char.relatedPosition.x >= mStartPoint.x && char.relatedPosition.x <= mEndPoint.x) {
                                filteredCharsFirst.add(char)
                            }
                        }
                        if (filteredCharsFirst.isNotEmpty()) {
                            selectedLines.add(CharDrawSegments(filteredCharsFirst))
                        }
                    } else {
                        // if the code reach here , that means the selection is not single line
                        // checking current line is the first line or not
                        if (mStartPoint.y >= line.relatedPosition.y && mStartPoint.y <= (line.relatedPosition.y + line.relatedSize.height)) {
                            val chars = arrayListOf<PdfChar>()
                            line.words.forEach { word -> chars.addAll(word.characters) }
                            val filteredCharsFirst = arrayListOf<PdfChar>()
                            chars.forEach { char ->
                                // if current line is the first line , then adding characters those are-
                                // in x selection
                                if (char.relatedPosition.x >= mStartPoint.x) {
                                    filteredCharsFirst.add(char)
                                }
                            }
                            if (filteredCharsFirst.isNotEmpty()) {
                                selectedLines.add(
                                    CharDrawSegments(filteredCharsFirst),
                                )
                            }
                        } else if (
                        // checking current line is the last line or not
                            mEndPoint.y >= line.relatedPosition.y && mEndPoint.y <= (line.relatedPosition.y + line.relatedSize.height)
                        ) {
                            val chars = arrayListOf<PdfChar>()
                            line.words.forEach { word -> chars.addAll(word.characters) }
                            val filteredCharsFirst = arrayListOf<PdfChar>()
                            chars.forEach { char ->
                                // if current line is the last line , then adding characters those are-
                                // in x selection
                                if (char.relatedPosition.x <= mEndPoint.x) {
                                    filteredCharsFirst.add(char)
                                }
                            }
                            if (filteredCharsFirst.isNotEmpty()) {
                                selectedLines.add(CharDrawSegments(filteredCharsFirst))
                            }
                        } else {
                            // if the code reach here it means this line is between start and end line,
                            // so we don't need to check x values, so we just add them all in to selection
                            val chars = arrayListOf<PdfChar>()
                            line.words.forEach { word -> chars.addAll(word.characters) }
                            selectedLines.add(CharDrawSegments(chars))
                        }
                    }
                }
            }
        }
        textSelection.addLineSelection(selectedLines)
        redraw()
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            return
        }

        if (isAntialiasing) {
            canvas.drawFilter = antialiasFilter
        }
        val bg = background
        if (bg == null) {
            canvas.drawColor(if (nightMode) Color.GRAY else Color.GRAY)
        } else {
            bg.draw(canvas)
        }
        if (isRecycled) {
            return
        }
        if (state != State.SHOWN) {
            Log.e(TAG, "onDraw: cancelling drawing because state is $state")
            return
        }
        val currentXOffset = currentXOffset
        val currentYOffset = currentYOffset
        canvas.translate(currentXOffset, currentYOffset)

        for (part in cacheManager?.thumbnails ?: emptyList()) {
            drawPart(canvas, part, true)
        }

        onDrawPagesNumbs.clear()
        val currentPagesPaginationIndexes = arrayListOf<Int>()
        val pageOffsets = arrayListOf<Float>()
        for (part in cacheManager?.pageParts ?: emptyList()) {
            drawPart(canvas, part)
            val page = part.page
            if (!onDrawPagesNumbs.contains(page)) {
                onDrawPagesNumbs.add(page)
                pageOffsets.add(pdfFile!!.getPageOffset(page))
                currentPagesPaginationIndexes.add(pdfFile!!.getPaginationIndexFromPageIndex(page))
            }
        }

        annotationHandler.drawAnnotations(currentPagesPaginationIndexes, pageOffsets, canvas, zoom)
        val selectionPage = textSelection.currentSelectionPageIndex
        if (selectionPage != -1) {
            val pageOffset = pdfFile!!.getPageOffset(selectionPage)
            textSelectionHelper.drawSelection(pageOffset, canvas, textSelection, zoom)
        }

        canvas.translate(-currentXOffset, -currentYOffset)
    }

    private fun drawPart(canvas: Canvas, part: PagePart, isCache: Boolean = false) {
        // Can seem strange, but avoid lot of calls
        val pageRelativeBounds = part.pageRelativeBounds
        val renderedBitmap = part.renderedBitmap
        if (renderedBitmap.isRecycled) {
            return
        }
        // Move to the target page
        val localTranslationX: Float
        val localTranslationY: Float
        val size = pdfFile!!.getPageSize(part.page)
        if (isSwipeVertical) {
            localTranslationY = pdfFile!!.getPageOffsetWithZoom(part.page, zoom)
            if (!isCache) {
                canvas.drawLine(
                    0f,
                    localTranslationY,
                    toCurrentScale(width.toFloat()),
                    localTranslationY,
                    pageDivisionPaint,
                )
            }
            val maxWidth = pdfFile!!.maxPageWidth
            localTranslationX = toCurrentScale(maxWidth - size.width) / 2
        } else {
            localTranslationX = pdfFile!!.getPageOffsetWithZoom(part.page, zoom)
            val maxHeight = pdfFile!!.maxPageHeight
            localTranslationY = toCurrentScale(maxHeight - size.height) / 2
        }
        canvas.translate(localTranslationX, localTranslationY)
        val srcRect = Rect(
            0,
            0,
            renderedBitmap.width,
            renderedBitmap.height,
        )
        val offsetX = toCurrentScale(pageRelativeBounds.left * size.width)
        val offsetY = toCurrentScale(pageRelativeBounds.top * size.height)
        val width = toCurrentScale(pageRelativeBounds.width() * size.width)
        val height = toCurrentScale(pageRelativeBounds.height() * size.height)
        // If we use float values for this rectangle, there will be
        // a possible gap between page parts, especially when
        // the zoom level is high.
        val dstRect = RectF(
            offsetX.toInt().toFloat(),
            offsetY.toInt().toFloat(),
            (offsetX + width).toInt().toFloat(),
            (offsetY + height).toInt().toFloat(),
        )
        // Check if bitmap is in the screen
        val translationX = currentXOffset + localTranslationX
        val translationY = currentYOffset + localTranslationY
        if (translationX + dstRect.left >= getWidth() || translationX + dstRect.right <= 0 || translationY + dstRect.top >= getHeight() || translationY + dstRect.bottom <= 0) {
            canvas.translate(-localTranslationX, -localTranslationY)
            return
        }
        canvas.drawBitmap(renderedBitmap, srcRect, dstRect, paint)
        if (Constants.DEBUG_MODE) {
            debugPaint.color = if (part.page % 2 == 0) Color.RED else Color.BLUE
            canvas.drawRect(dstRect, debugPaint)
        }
        // Restore the canvas position
        canvas.translate(-localTranslationX, -localTranslationY)
    }

    fun loadPages() {
        if (pdfFile == null || renderingHandler == null) {
            return
        }
        // Cancel all current tasks
        renderingHandler?.removeMessages(RenderingHandler.MSG_RENDER_TASK)
        cacheManager?.makeANewSet()
        pagesLoader?.loadPages()
        redraw()
    }

    fun loadComplete(pdfFile: PdfFile) {
        scope?.launch(Dispatchers.IO) {
            try {
                state = State.LOADED

                this@PDFView.pdfFile = pdfFile
                this@PDFView.pdfFile?.setMergeListener(this@PDFView)
                this@PDFView.pdfFile?.prepareCharsRelativeSize()

                startRendering()
                dragPinchManager?.enable()
                callbacks.callOnLoadComplete(pdfFile.pagesCount)
                withContext(Dispatchers.Main) {
                    jumpToWithPaginationPageIndex(defaultPage, false)
                    listener?.onPreparationSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    listener?.onPreparationFailed(e.message ?: "Something Went wrong", e)
                }
            }
        }
    }

    private fun startRendering() {
        if (!renderingHandlerThread!!.isAlive) {
            renderingHandlerThread!!.start()
        }
        renderingHandler = RenderingHandler(
            renderingHandlerThread!!.looper,
            this@PDFView,
        )
        renderingHandler!!.start()
        if (scrollHandle != null) {
            scrollHandle!!.setupLayout(this@PDFView)
            isScrollHandleInit = true
        }
    }

    fun loadError(t: Exception?) {
        state = State.ERROR
        val onErrorListener = callbacks.onError
        recycle()
        invalidate()
        onErrorListener?.onError(t) ?: Log.e("PDFView", "load pdf error", t)
        listener?.onPreparationFailed(t?.message ?: "Something went wrong", t)
    }

    fun redraw() {
        invalidate()
    }

    fun onBitmapRendered(part: PagePart) {
        if (state == State.LOADED) {
            state = State.SHOWN
            callbacks.callOnRender(pdfFile!!.pagesCount)
        }
        if (part.isThumbnail) {
            cacheManager?.cacheThumbnail(part)
        } else {
            cacheManager?.cachePart(part)
        }
        redraw()
    }

    private var bottomEndReached = false
    private var topEndReached = false

    @JvmOverloads
    fun moveTo(offsetX: Float, offsetY: Float, moveHandle: Boolean = true, fromScrolling: Boolean = false) {
        var mOffsetX = offsetX
        var mOffsetY = offsetY
        var maxScrollReached = false
        if (isSwipeVertical) {
            // Check X offset
            val scaledPageWidth = toCurrentScale(pdfFile!!.maxPageWidth)
            if (scaledPageWidth < width) {
                mOffsetX = width / 2 - scaledPageWidth / 2
            } else {
                if (mOffsetX > 0) {
                    mOffsetX = 0f
                } else if (mOffsetX + scaledPageWidth < width) {
                    mOffsetX = width - scaledPageWidth
                }
            }
            // Check Y offset
            val contentHeight = pdfFile!!.getDocLen(zoom)
            if (contentHeight < height) { // whole document height visible on screen
                mOffsetY = (height - contentHeight) / 2
            } else {
                if (mOffsetY > 0) { // top visible
                    mOffsetY = 0f
                    if (!topEndReached) {
                        onMaximumTopReach()
                    }
                    topEndReached = true
                    maxScrollReached = true
                } else if (mOffsetY + contentHeight < height) { // bottom visible
                    mOffsetY = -contentHeight + height
                    if (!bottomEndReached) {
                        onMaximumBottomReach()
                    }
                    bottomEndReached = true
                    maxScrollReached = true
                } else {
                    bottomEndReached = false
                    topEndReached = false
                }
            }
            scrollDir = if (mOffsetY < currentYOffset) {
                ScrollDir.END
            } else if (mOffsetY > currentYOffset) {
                ScrollDir.START
            } else {
                ScrollDir.NONE
            }
        } else {
            // Check Y offset
            val scaledPageHeight = toCurrentScale(pdfFile!!.maxPageHeight)
            if (scaledPageHeight < height) {
                mOffsetY = height / 2 - scaledPageHeight / 2
            } else {
                if (mOffsetY > 0) {
                    mOffsetY = 0f
                } else if (mOffsetY + scaledPageHeight < height) {
                    mOffsetY = height - scaledPageHeight
                }
            }
            // Check X offset
            val contentWidth = pdfFile!!.getDocLen(zoom)
            if (contentWidth < width) { // whole document width visible on screen
                mOffsetX = (width - contentWidth) / 2
            } else {
                if (mOffsetX > 0) { // left visible
                    mOffsetX = 0f
                } else if (mOffsetX + contentWidth < width) { // right visible
                    mOffsetX = -contentWidth + width
                }
            }
            scrollDir = if (mOffsetX < currentXOffset) {
                ScrollDir.END
            } else if (mOffsetX > currentXOffset) {
                ScrollDir.START
            } else {
                ScrollDir.NONE
            }
        }
        currentXOffset = mOffsetX
        currentYOffset = mOffsetY
        val positionOffset = positionOffset
        if (moveHandle && scrollHandle != null && !documentFitsView()) {
            scrollHandle!!.setScroll(positionOffset)
        }
        callbacks.callOnPageScroll(currentPage, positionOffset)
        if (!maxScrollReached && fromScrolling) {
            listener?.onScrolling()
        }
        redraw()
    }

    private fun onMaximumTopReach() {
        if (mergeState != MergeState.IDLE) {
            Log.e(TAG, "Top Pagination cancelled, already a merging working")
            return
        }
        val pageToLoad = (pdfFile?.paginationStartPageIndex ?: 0) - 1
        if (pageToLoad >= 0) {
            listener?.loadTopPdfChunk(getMergeId(), pageToLoad)
            mergeState = MergeState.TOP_MERGING_LOADING
        }
    }

    private fun onMaximumBottomReach() {
        if (mergeState != MergeState.IDLE) {
            Log.e(TAG, "Bottom Pagination cancelled, already a merging working")
            return
        }

        if (totalPdfPage == -1) {
            Log.e(TAG, "Failed to paginate pdf, you haven't set pdf total page")
            return
        }
        val pageToLoad = pdfFile!!.paginationEndPageIndex + 1
        if (pageToLoad < totalPdfPage) {
            listener?.loadBottomPdfChunk(getMergeId(), pageToLoad)
            mergeState = MergeState.BOTTOM_MERGE_LOADING
        } else {
            Log.e(TAG, "Maximum pagination limit is reached")
        }
    }

    fun loadPageByOffset() {
        if (0 == pdfFile!!.pagesCount) {
            return
        }
        val offset: Float
        val screenCenter: Float
        if (isSwipeVertical) {
            offset = currentYOffset
            screenCenter = height.toFloat() / 2
        } else {
            offset = currentXOffset
            screenCenter = width.toFloat() / 2
        }
        val page = pdfFile!!.getPageAtOffset(-(offset - screenCenter), zoom)
        if (page >= 0 && page <= pdfFile!!.pagesCount - 1 && page != currentPage) {
            showPage(page)
        } else {
            loadPages()
        }
    }

    fun performPageSnap() {
        if (!isPageSnap || pdfFile == null || pdfFile!!.pagesCount == 0) {
            return
        }
        val centerPage = findFocusPage(currentXOffset, currentYOffset)
        val edge = findSnapEdge(centerPage)
        if (edge == SnapEdge.NONE) {
            return
        }
        val offset = snapOffsetForPage(centerPage, edge)
        if (isSwipeVertical) {
            animationManager?.startYAnimation(currentYOffset, -offset)
        } else {
            animationManager?.startXAnimation(currentXOffset, -offset)
        }
    }

    fun findSnapEdge(page: Int): SnapEdge {
        if (!isPageSnap || page < 0) {
            return SnapEdge.NONE
        }
        val currentOffset = if (isSwipeVertical) currentYOffset else currentXOffset
        val offset = -pdfFile!!.getPageOffsetWithZoom(page, zoom)
        val length = if (isSwipeVertical) height else width
        val pageLength = pdfFile!!.getPageLength(page, zoom)
        return if (length >= pageLength) {
            SnapEdge.CENTER
        } else if (currentOffset >= offset) {
            SnapEdge.START
        } else if (offset - pageLength > currentOffset - length) {
            SnapEdge.END
        } else {
            SnapEdge.NONE
        }
    }

    fun snapOffsetForPage(pageIndex: Int, edge: SnapEdge): Float {
        var offset = pdfFile!!.getPageOffsetWithZoom(pageIndex, zoom)
        val length = if (isSwipeVertical) height.toFloat() else width.toFloat()
        val pageLength = pdfFile!!.getPageLength(pageIndex, zoom)
        if (edge == SnapEdge.CENTER) {
            offset = offset - length / 2f + pageLength / 2f
        } else if (edge == SnapEdge.END) {
            offset = offset - length + pageLength
        }
        return offset
    }

    fun findFocusPage(xOffset: Float, yOffset: Float): Int {
        val currOffset = if (isSwipeVertical) yOffset else xOffset
        val length = if (isSwipeVertical) height.toFloat() else width.toFloat()
        // make sure first and last page can be found
        if (currOffset > -1) {
            return 0
        } else if (currOffset < -pdfFile!!.getDocLen(zoom) + length + 1) {
            return pdfFile!!.pagesCount - 1
        }
        // else find page in center
        val center = currOffset - length / 2f
        return pdfFile!!.getPageAtOffset(-center, zoom)
    }

    fun scrollWithAnimation(scrollBy: Int) {
        animationManager?.startYAnimation(currentYOffset, currentYOffset + scrollBy)
    }

    fun pageFillsScreen(): Boolean {
        val start = -pdfFile!!.getPageOffsetWithZoom(currentPage, zoom)
        val end = start - pdfFile!!.getPageLength(currentPage, zoom)
        return if (isSwipeVertical) {
            start > currentYOffset && end < currentYOffset - height
        } else {
            start > currentXOffset && end < currentXOffset - width
        }
    }

    fun moveRelativeTo(dx: Float, dy: Float) {
        moveTo(currentXOffset + dx, currentYOffset + dy, fromScrolling = true)
    }

    fun zoomTo(zoom: Float) {
        this.zoom = zoom
    }

    fun zoomCenteredTo(zoom: Float, pivot: PointF) {
        val dZoom = zoom / this.zoom
        zoomTo(zoom)
        var baseX = currentXOffset * dZoom
        var baseY = currentYOffset * dZoom
        baseX += pivot.x - pivot.x * dZoom
        baseY += pivot.y - pivot.y * dZoom
        moveTo(baseX, baseY)
    }

    fun zoomCenteredRelativeTo(dZoom: Float, pivot: PointF) {
        zoomCenteredTo(zoom * dZoom, pivot)
    }

    fun documentFitsView(): Boolean {
        val len = pdfFile!!.getDocLen(1f)
        return if (isSwipeVertical) {
            len < height
        } else {
            len < width
        }
    }

    fun fitToWidth(page: Int) {
        if (state != State.SHOWN) {
            Log.e(TAG, "Cannot fit, document not rendered yet")
            return
        }
        zoomTo(width / pdfFile!!.getPageSize(page).width)
        jumpTo(page)
    }

    fun getPageSize(pageIndex: Int): SizeF {
        return if (pdfFile == null) {
            SizeF(0f, 0f)
        } else {
            pdfFile!!.getPageSize(pageIndex)
        }
    }

    fun toRealScale(size: Float): Float {
        return size / zoom
    }

    fun toCurrentScale(size: Float): Float {
        return size * zoom
    }

    private fun Float.scaledX(): Float {
        return this - currentXOffset
    }

    private fun Float.scaledY(): Float {
        return this - currentYOffset
    }

    val isZooming: Boolean
        get() = zoom != minZoom && TinyDB(context).getBoolean(IS_ZOOM_ENABLED, true)

    private fun setDefaultPage(defaultPage: Int) {
        this.defaultPage = defaultPage
    }

    fun resetZoom() {
        zoomTo(minZoom)
    }

    fun resetZoomWithAnimation() {
        zoomWithAnimation(minZoom)
    }

    fun zoomWithAnimation(centerX: Float, centerY: Float, scale: Float) {
        animationManager?.startZoomAnimation(centerX, centerY, zoom, scale)
    }

    fun zoomWithAnimation(scale: Float, onAnimationEnd: (() -> Unit)? = null) {
        animationManager?.startZoomAnimation(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            zoom,
            scale,
            onAnimationEnd,
        )
    }

    fun getPageAtPositionOffset(positionOffset: Float): Int {
        return pdfFile!!.getPageAtOffset(pdfFile!!.getDocLen(zoom) * positionOffset, zoom)
    }

    fun useBestQuality(bestQuality: Boolean) {
        isBestQuality = bestQuality
    }

    fun enableAnnotationRendering(annotationRendering: Boolean) {
        isAnnotationRendering = annotationRendering
    }

    fun enableRenderDuringScale(renderDuringScale: Boolean) {
        this.renderDuringScale = renderDuringScale
    }

    fun enableAntialiasing(enableAntialiasing: Boolean) {
        isAntialiasing = enableAntialiasing
    }

    fun setPageFling(pageFling: Boolean) {
        isPageFlingEnabled = pageFling
    }

    private fun setSpacing(spacingDp: Int) {
        spacingPx = Util.getDP(context, spacingDp)
    }

    private fun setAutoSpacing(autoSpacing: Boolean) {
        isAutoSpacingEnabled = autoSpacing
    }

    fun doRenderDuringScale(): Boolean {
        return renderDuringScale
    }

    val documentMeta: Meta?
        get() = if (pdfFile == null) {
            null
        } else {
            pdfFile!!.metaData
        }
    val tableOfContents: List<Bookmark>
        get() = if (pdfFile == null) {
            emptyList()
        } else {
            pdfFile!!.bookmarks
        }

    fun getLinks(page: Int): List<PdfDocument.Link> {
        return if (pdfFile == null) {
            emptyList()
        } else {
            pdfFile!!.getPageLinks(page)
        }
    }

    fun fromFile(file: File, startPageIndex: Int = 0): Configurator {
        this.file = file
        return Configurator(FileSource(file, startPageIndex))
    }

    fun fromBytes(bytes: ByteArray): Configurator {
        return Configurator(ByteArraySource(bytes))
    }

    private enum class State {
        DEFAULT, LOADED, SHOWN, ERROR
    }

    fun isPinchZoom(isEnable: Boolean) {
        if (isEnable) dragPinchManager?.enable()
        else dragPinchManager?.disable()
    }

    inner class Configurator constructor(private val documentSource: DocumentSource) {
        private var pageNumbers: IntArray? = null
        private var enableSwipe = true
        private var enableDoubleTap = true
        private var onDrawListener: OnDrawListener? = null
        private var onDrawAllListener: OnDrawListener? = null
        private var onLoadCompleteListener: OnLoadCompleteListener? = null
        private var onErrorListener: OnErrorListener? = null
        private var onPageChangeListener: OnPageChangeListener? = null
        private var onPageScrollListener: OnPageScrollListener? = null
        private var onRenderListener: OnRenderListener? = null
        private var onTapListener: OnTapListener? = null
        private var onLongPressListener: OnLongPressListener? = null
        private var onPageErrorListener: OnPageErrorListener? = null
        private var onPageCompleteListener: OnLoadCompleteListener? = null
        private var linkHandler: LinkHandler = DefaultLinkHandler(this@PDFView)
        private var defaultPage = 0
        private var swipeHorizontal = false
        private var annotationRendering = true
        private var password: String? = null
        private var scrollHandle: ScrollHandle? = null
        private var antialiasing = true
        private var spacing = 0
        private var autoSpacing = false
        private var pageFitPolicy = FitPolicy.BOTH
        private var fitEachPage = true
        private var pageFling = false
        private var pageSnap = false
        private var nightMode = false
        private var file: File? = null
        private var startPage: Int = 1
        private var paginationDefaultPageIndex = -1
        fun pages(vararg pageNumbers: Int): Configurator {
            this.pageNumbers = pageNumbers
            return this
        }

        fun enableSwipe(enableSwipe: Boolean): Configurator {
            this.enableSwipe = enableSwipe
            return this
        }

        fun enableDoubleTap(enableDoubleTap: Boolean): Configurator {
            this.enableDoubleTap = enableDoubleTap
            return this
        }

        fun enableAnnotationRendering(annotationRendering: Boolean): Configurator {
            this.annotationRendering = annotationRendering
            return this
        }

        fun onDraw(onDrawListener: OnDrawListener?): Configurator {
            this.onDrawListener = onDrawListener
            return this
        }

        fun onDrawAll(onDrawAllListener: OnDrawListener?): Configurator {
            this.onDrawAllListener = onDrawAllListener
            return this
        }

        fun onLoad(onLoadCompleteListener: OnLoadCompleteListener?): Configurator {
            this.onLoadCompleteListener = onLoadCompleteListener
            return this
        }

        fun onPageScroll(onPageScrollListener: OnPageScrollListener?): Configurator {
            this.onPageScrollListener = onPageScrollListener
            return this
        }

        fun onError(onErrorListener: OnErrorListener?): Configurator {
            this.onErrorListener = onErrorListener
            return this
        }

        fun onPageError(onPageErrorListener: OnPageErrorListener?): Configurator {
            this.onPageErrorListener = onPageErrorListener
            return this
        }

        fun onLoadComplete(onPageCompleteListener: OnLoadCompleteListener?): Configurator {
            this.onPageCompleteListener = onPageCompleteListener
            return this
        }

        fun onPageChange(onPageChangeListener: OnPageChangeListener?): Configurator {
            this.onPageChangeListener = onPageChangeListener
            return this
        }

        fun onRender(onRenderListener: OnRenderListener?): Configurator {
            this.onRenderListener = onRenderListener
            return this
        }

        fun onTap(onTapListener: OnTapListener?): Configurator {
            this.onTapListener = onTapListener
            return this
        }

        fun onLongPress(onLongPressListener: OnLongPressListener?): Configurator {
            this.onLongPressListener = onLongPressListener
            return this
        }

        fun linkHandler(linkHandler: LinkHandler): Configurator {
            this.linkHandler = linkHandler
            return this
        }

        fun defaultPage(defaultPage: Int): Configurator {
            this.defaultPage = defaultPage
            return this
        }

        fun swipeHorizontal(swipeHorizontal: Boolean): Configurator {
            this.swipeHorizontal = swipeHorizontal
            return this
        }

        fun password(password: String?): Configurator {
            this.password = password
            return this
        }

        fun scrollHandle(scrollHandle: ScrollHandle?): Configurator {
            this.scrollHandle = scrollHandle
            return this
        }

        fun enableAntialiasing(antialiasing: Boolean): Configurator {
            this.antialiasing = antialiasing
            return this
        }

        fun spacing(spacing: Int): Configurator {
            this.spacing = spacing
            return this
        }

        fun autoSpacing(autoSpacing: Boolean): Configurator {
            this.autoSpacing = autoSpacing
            return this
        }

        fun pageFitPolicy(pageFitPolicy: FitPolicy): Configurator {
            this.pageFitPolicy = pageFitPolicy
            return this
        }

        fun fitEachPage(fitEachPage: Boolean): Configurator {
            this.fitEachPage = fitEachPage
            return this
        }

        fun pageSnap(pageSnap: Boolean): Configurator {
            this.pageSnap = pageSnap
            return this
        }

        fun pageFling(pageFling: Boolean): Configurator {
            this.pageFling = pageFling
            return this
        }

        fun nightMode(nightMode: Boolean): Configurator {
            this.nightMode = nightMode
            return this
        }

        fun disableLongPress(): Configurator {
            dragPinchManager?.disableLongpress()
            return this
        }

        fun setPdfStartPage(page: Int): Configurator {
            this.startPage = page
            return this
        }

        fun pdfDefaultPage(paginationPageIndex: Int): Configurator {
            paginationDefaultPageIndex = paginationPageIndex
            return this
        }

        fun load() {
            if (!hasSize) {
                waitingDocumentConfigurator = this
                return
            }
            recycle()
            callbacks.setOnLoadComplete(onLoadCompleteListener)
            callbacks.onError = onErrorListener
            callbacks.onDraw = onDrawListener
            callbacks.onDrawAll = onDrawAllListener
            callbacks.setOnPageChange(onPageChangeListener)
            callbacks.setOnPageScroll(onPageScrollListener)
            callbacks.setOnRender(onRenderListener)
            callbacks.setOnTap(onTapListener)
            callbacks.setOnLongPress(onLongPressListener)
            callbacks.setOnPageError(onPageErrorListener)
            callbacks.setOnLoadComplete(onPageCompleteListener)
            callbacks.setLinkHandler(linkHandler)
            isSwipeEnabled = enableSwipe
            setNightMode(nightMode)
            this@PDFView.enableDoubleTap(enableDoubleTap)
            setDefaultPage(defaultPage)
            isSwipeVertical = !swipeHorizontal
            this@PDFView.enableAnnotationRendering(annotationRendering)
            this@PDFView.scrollHandle = scrollHandle
            this@PDFView.enableAntialiasing(antialiasing)
            setSpacing(spacing)
            setAutoSpacing(autoSpacing)
            this@PDFView.pageFitPolicy = pageFitPolicy
            isFitEachPage = fitEachPage
            isPageSnap = pageSnap
            setPageFling(pageFling)
            this@PDFView.load(documentSource, password)
        }
    }

    companion object {
        private val TAG = PDFView::class.java.simpleName
        const val DEFAULT_MAX_SCALE = 5.0f
        const val DEFAULT_MID_SCALE = 1.75f
        const val DEFAULT_MIN_SCALE = 1.0f
    }

    fun clearAllTextSelectionAndCoordinates() {
        textSelection.clearAllSelection(true)
        textSelectionHelper.endSelectionPosition.set(0f, 0f)
        textSelectionHelper.endHandlePosition.set(0f, 0f)
        textSelectionHelper.startSelectionPosition.set(0f, 0f)
        textSelectionHelper.startHandlePosition.set(0f, 0f)
        listener?.onTextSelectionCleared()
        redraw()
    }

    fun clearAllTextSelection(redraw: Boolean = true) {
        textSelection.clearAllSelection()
        listener?.onTextSelectionCleared()
        if (redraw) {
            redraw()
        }
    }

    fun addHighlight(highlight: HighlightModel) {
        val coordinates = highlight.coordinates ?: return
        val startPoint = PointF(coordinates.startX.toFloat(), coordinates.startY.toFloat())
        val endPoint = PointF(coordinates.endX.toFloat(), coordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(highlight.paginationPageIndex), startPoint, endPoint)
        highlight.charDrawSegments = drawSegments
        annotationHandler.annotations.add(highlight)
        redraw()
    }

    fun addHighlightExisting(highlight: HighlightModel) {
        val coordinates = highlight.coordinates ?: return
        // Get full bounding values
        val left = coordinates.startX.toFloat()
        val right = coordinates.endX.toFloat()
        val fullTop = coordinates.startY.toFloat()
        val fullBottom = coordinates.endY.toFloat()
        // Calculate the vertical center
        val centerY = (fullTop + fullBottom) / 2f
        // Calculate full height of the original rectangle
        val fullHeight = kotlin.math.abs(fullTop - fullBottom)
        // Choose a reduced height (for example, 50% of the full height)
        val newHeight = fullHeight * 0.5f
        // Compute adjusted top and bottom so that the highlight is centered vertically
        val adjustedTop = centerY + newHeight / 2f
        val adjustedBottom = centerY - newHeight / 2f
        // Build new coordinates for the highlight using the same left/right values
        val adjustedCoordinates = Coordinates(
            startX = left.toDouble(),
            startY = adjustedTop.toDouble(),
            endX = right.toDouble(),
            endY = adjustedBottom.toDouble()
        )
        // Create start and end points from the adjusted coordinates.
        // (You might need additional coordinate conversion depending on your view.)
        val startPoint = PointF(adjustedCoordinates.startX.toFloat(), adjustedCoordinates.startY.toFloat())
        val endPoint = PointF(adjustedCoordinates.endX.toFloat(), adjustedCoordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(highlight.paginationPageIndex), startPoint, endPoint)
        highlight.charDrawSegments = drawSegments
        annotationHandler.annotations.add(highlight)
        redraw()
    }

    fun findAllAnnotations(): ArrayList<PdfAnnotationModel> {
        return annotationHandler.annotations
    }

    fun addUnderline(note: UnderlineModel) {
        val coordinates = note.coordinates ?: return
        val startPoint = PointF(coordinates.startX.toFloat(), coordinates.startY.toFloat())
        val endPoint = PointF(coordinates.endX.toFloat(), coordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(note.paginationPageIndex), startPoint, endPoint)
        note.charDrawSegments = drawSegments
        annotationHandler.annotations.add(note)
        redraw()
    }

    fun addUnderlineExisting(note: UnderlineModel) {
        val coordinates = note.coordinates ?: return
        // Get full bounding values
        val left = coordinates.startX.toFloat()
        val right = coordinates.endX.toFloat()
        val fullTop = coordinates.startY.toFloat()
        val fullBottom = coordinates.endY.toFloat()
        // Calculate the vertical center
        val centerY = (fullTop + fullBottom) / 2f
        // Calculate full height of the original rectangle
        val fullHeight = kotlin.math.abs(fullTop - fullBottom)
        // Choose a reduced height (for example, 50% of the full height)
        val newHeight = fullHeight * 0.5f
        // Compute adjusted top and bottom so that the highlight is centered vertically
        val adjustedTop = centerY + newHeight / 2f
        val adjustedBottom = centerY - newHeight / 2f
        // Build new coordinates for the highlight using the same left/right values
        val adjustedCoordinates = Coordinates(
            startX = left.toDouble(),
            startY = adjustedTop.toDouble(),
            endX = right.toDouble(),
            endY = adjustedBottom.toDouble()
        )
        // Create start and end points from the adjusted coordinates.
        // (You might need additional coordinate conversion depending on your view.)
        val startPoint = PointF(adjustedCoordinates.startX.toFloat(), adjustedCoordinates.startY.toFloat())
        val endPoint = PointF(adjustedCoordinates.endX.toFloat(), adjustedCoordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(note.paginationPageIndex), startPoint, endPoint)
        note.charDrawSegments = drawSegments
        annotationHandler.annotations.add(note)
        redraw()
    }

    fun addStrikeThrough(strike: StrikeThroughModel) {
        val coordinates = strike.coordinates ?: return
        val startPoint = PointF(coordinates.startX.toFloat(), coordinates.startY.toFloat())
        val endPoint = PointF(coordinates.endX.toFloat(), coordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(strike.paginationPageIndex), startPoint, endPoint)
        strike.charDrawSegments = drawSegments
        annotationHandler.annotations.add(strike)
        redraw()
    }

    fun addStrikeThroughExisting(strike: StrikeThroughModel) {
        val coordinates = strike.coordinates ?: return
        // Get full bounding values
        val left = coordinates.startX.toFloat()
        val right = coordinates.endX.toFloat()
        val fullTop = coordinates.startY.toFloat()
        val fullBottom = coordinates.endY.toFloat()
        // Calculate the vertical center
        val centerY = (fullTop + fullBottom) / 2f
        // Calculate full height of the original rectangle
        val fullHeight = kotlin.math.abs(fullTop - fullBottom)
        // Choose a reduced height (for example, 50% of the full height)
        val newHeight = fullHeight * 0.5f
        // Compute adjusted top and bottom so that the highlight is centered vertically
        val adjustedTop = centerY + newHeight / 2f
        val adjustedBottom = centerY - newHeight / 2f
        // Build new coordinates for the highlight using the same left/right values
        val adjustedCoordinates = Coordinates(
            startX = left.toDouble(),
            startY = adjustedTop.toDouble(),
            endX = right.toDouble(),
            endY = adjustedBottom.toDouble()
        )
        // Create start and end points from the adjusted coordinates.
        // (You might need additional coordinate conversion depending on your view.)
        val startPoint = PointF(adjustedCoordinates.startX.toFloat(), adjustedCoordinates.startY.toFloat())
        val endPoint = PointF(adjustedCoordinates.endX.toFloat(), adjustedCoordinates.endY.toFloat())
        val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(strike.paginationPageIndex), startPoint, endPoint)
        strike.charDrawSegments = drawSegments
        annotationHandler.annotations.add(strike)
        redraw()
    }

    fun addSignature(signatureModel: SignatureModel) {
        annotationHandler.annotations.add(signatureModel)
        redraw()
    }

    fun removeUnderlineAnnotations(noteIds: List<Long>) {
        annotationHandler.removeUnderlineAnnotation(noteIds)
        redraw()
    }

    fun updateUnderline(underlineModel: UnderlineModel) {
        annotationHandler.annotations.map {
            if (it.type == PdfAnnotationModel.Type.Underline) {
                if (it.asUnderline()?.id == underlineModel.id) {
                    it.asUnderline()?.apply {
                        updatedAt = underlineModel.updatedAt
                    }
                }
            }
        }
    }

    fun findDrawSegmentsOfAnnotation(
        pageIndex: Int,
        startPoint: PointF,
        endPoint: PointF,
    ): ArrayList<CharDrawSegments> {
        val segments = arrayListOf<CharDrawSegments>()
        pdfFile?.pageDetails?.getOrNull(pageIndex)?.let {
            it.coordinates.forEach { line ->
                val bottomYStart = it.height - startPoint.y
                val bottomYEnd = it.height - endPoint.y
                val newStartPoint = PointF(startPoint.x, bottomYStart)
                val newEndPoint = PointF(endPoint.x, bottomYEnd)
                val drawSegments = getDrawSegmentInPoints(line, newStartPoint, newEndPoint)
                if (drawSegments != null) {
                    segments.add(drawSegments)
                }
            }
        }
        return segments
    }

    private fun getDrawSegmentInPoints(
        line: PdfLine,
        startPoint: PointF,
        endPoint: PointF,
    ): CharDrawSegments? {
        // extracting characters inside the selection points
        // Checking all the lines that between start and end point
        if ((line.position.y + line.size.height) >= startPoint.y && line.position.y <= endPoint.y) {
            // Checking startPoint and endPoint are in the same line or not
            // if they are in same line , we need to check the chars that
            // are appear between x axis
            if (
                startPoint.y >= line.position.y && startPoint.y <= (line.position.y + line.size.height) &&
                endPoint.y >= line.position.y && endPoint.y <= (line.position.y + line.size.height)
            ) {
                val chars = arrayListOf<PdfChar>()
                line.words.forEach { word -> chars.addAll(word.characters) }
                val filteredCharsFirst = arrayListOf<PdfChar>()
                chars.forEach { char ->
                    // if characters between startX and endX add the character in selected text
                    if ((char.topPosition.x + char.size.width / 2) >= startPoint.x && (char.topPosition.x + char.size.width / 2) <= endPoint.x) {
                        filteredCharsFirst.add(char)
                    }
                }
                if (filteredCharsFirst.isNotEmpty()) {
                    return CharDrawSegments(filteredCharsFirst)
                }
            } else {
                // if the code reach here , that means the selection is not single line
                // checking current line is the first line or not
                if (startPoint.y >= line.position.y && startPoint.y <= (line.position.y + line.size.height)) {
                    val chars = arrayListOf<PdfChar>()
                    line.words.forEach { word -> chars.addAll(word.characters) }
                    val filteredCharsFirst = arrayListOf<PdfChar>()
                    chars.forEach { char ->
                        // if current line is the first line , then adding characters those are-
                        // in x selection
                        if ((char.topPosition.x + char.size.width / 2) >= startPoint.x) {
                            filteredCharsFirst.add(char)
                        }
                    }
                    if (filteredCharsFirst.isNotEmpty()) {
                        return CharDrawSegments(filteredCharsFirst)
                    }
                } else if (
                // checking current line is the last line or not
                    endPoint.y >= line.position.y && endPoint.y <= (line.position.y + line.size.height)
                ) {
                    val chars = arrayListOf<PdfChar>()
                    line.words.forEach { word -> chars.addAll(word.characters) }
                    val filteredCharsFirst = arrayListOf<PdfChar>()
                    chars.forEach { char ->
                        // if current line is the last line , then adding characters those are-
                        // in x selection
                        if (char.topPosition.x + char.size.width / 2 <= endPoint.x) {
                            filteredCharsFirst.add(char)
                        }
                    }
                    if (filteredCharsFirst.isNotEmpty()) {
                        return CharDrawSegments(filteredCharsFirst)
                    }
                } else {
                    // if the code reach here it means this line is between start and end line,
                    // so we don't need to check x values, so we just add them all in to selection
                    val chars = arrayListOf<PdfChar>()
                    line.words.forEach { word -> chars.addAll(word.characters) }
                    return CharDrawSegments(chars)
                }
            }
        }
        return null
    }

    override fun mergeStart(mergeType: PdfFile.MergeType) {
    }

    override fun mergeEnd(
        mergeId: Int,
        mergeType: PdfFile.MergeType,
        mergedFileDocLength: Float,
        pageToLoad: Int,
    ) {
        // Update selection page , after pagination page indexes may change!
        if (textSelection.currentSelectionPageIndex != -1 && textSelection.currentSelectionPaginationIndex != -1) {
            textSelection.currentSelectionPageIndex = pdfFile?.getPageIndexFromPaginationIndex(textSelection.currentSelectionPaginationIndex) ?: -1
        }
        // if pages are added from the top, then we need to to update currentYOffset accordingly to view the same page the user have now
        if (mergeType == PdfFile.MergeType.TOP) {
            cacheManager?.recycle()
            currentYOffset -= mergedFileDocLength * zoom
        }
        loadPages()
        // giving callbacks to reader activity to dismiss loader also changing merging state
        mergeState = MergeState.IDLE
        listener?.onMergeEnd(mergeId, mergeType)
    }

    fun resetZoomAndXOffset() {
        currentXOffset = 0f
        zoom = 1f
        redraw()
    }

    fun getCurrentVisiblePaginationPageIndexes(): List<Int> {
        if (pdfFile != null) {
            return onDrawPagesNumbs.map { pdfFile!!.getPaginationIndexFromPageIndex(it) }
        }
        return emptyList()
    }

    fun getPaginationStartPageIndex(): Int {
        return pdfFile?.paginationStartPageIndex ?: 0
    }

    fun getPaginationEndPageIndex(): Int {
        return pdfFile?.paginationEndPageIndex ?: 0
    }

    fun getCurrentPdfPage(): Int {
        return pdfFile?.getPaginationIndexFromPageIndex(currentPage) ?: -1
    }

    fun loadAnnotations(annotations: List<PdfAnnotationModel>) {
        annotationHandler.clearAllAnnotations()
        scope?.launch(Dispatchers.IO) {
            for (annotation in annotations) {
                val coordinates = when (annotation.type) {
                    PdfAnnotationModel.Type.Underline -> annotation.asUnderline()?.updateAnnotationData()?.coordinates
                    PdfAnnotationModel.Type.StrikeThrough -> annotation.asStrikeThrough()?.updateAnnotationData()?.coordinates
                    PdfAnnotationModel.Type.Highlight -> annotation.asHighlight()?.updateAnnotationData()?.coordinates
                    PdfAnnotationModel.Type.Signature -> annotation.asSignature()?.updateAnnotationData()?.coordinates
                }
                coordinates ?: continue
                val startPoint = PointF(coordinates.startX.toFloat(), coordinates.startY.toFloat())
                val endPoint = PointF(coordinates.endX.toFloat(), coordinates.endY.toFloat())
                val drawSegments = findDrawSegmentsOfAnnotation(pdfFile!!.getPageIndexFromPaginationIndex(annotation.paginationPageIndex), startPoint, endPoint)
                annotation.charDrawSegments = drawSegments
            }
            withContext(Dispatchers.Main) {
                annotationHandler.annotations.addAll(annotations)
                redraw()
            }
        }
    }

    fun getTotalPage(): Int {
        return pdfFile?.pagesCount ?: 0
    }

    fun updateMergeFailed() {
        mergeState = MergeState.IDLE
    }

    interface Listener {
        fun onPreparationStarted()
        fun onPreparationSuccess()
        fun onPreparationFailed(error: String, e: Exception?)

        fun onPageChanged(pageIndex: Int, paginationPageIndex: Int)
        fun onTextSelected(selection: TextSelectionData, rawPoint: PointF)

        fun hideTextSelectionOptionWindow()
        fun onTextSelectionCleared()
        fun onNotesStampsClicked(comments: List<UnderlineModel>, pointOfNote: PointF)
        fun loadTopPdfChunk(mergeId: Int, pageIndexToLoad: Int)
        fun loadBottomPdfChunk(mergedId: Int, pageIndexToLoad: Int)
        fun onScrolling()
        fun onTap()
        fun onMergeStart(mergeId: Int, mergeType: PdfFile.MergeType)
        fun onMergeEnd(mergeId: Int, mergeType: PdfFile.MergeType)
        fun onMergeFailed(mergeId: Int, mergeType: PdfFile.MergeType, message: String, exception: java.lang.Exception?)
    }
}
