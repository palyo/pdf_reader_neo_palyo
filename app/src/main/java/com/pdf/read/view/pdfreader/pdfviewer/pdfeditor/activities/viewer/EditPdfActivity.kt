package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer

import android.graphics.*
import android.media.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*
import com.tom_roush.pdfbox.pdmodel.*
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.*
import kotlinx.coroutines.*
import java.io.*

class EditPdfActivity : BaseActivity<ActivityEditPdfBinding>(ActivityEditPdfBinding::inflate) {
    private var documentViewModel: DocumentViewModel? = null
    private val TAG = "EditPdfActivity"
    private var filePath: String = ""
    private var pdfRenderScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun ActivityEditPdfBinding.initExtra() {
        filePath = intent?.getStringExtra("file_path") ?: ""
        documentViewModel = DocumentViewModel.getInstance(application)
        preparePdfView()
    }

    private fun ActivityEditPdfBinding.preparePdfView() {
        pdfView.attachCoroutineScope(pdfRenderScope)
            .setListener(pdfCallBack)
        loadPdf()
    }

    private fun ActivityEditPdfBinding.loadPdf() {
        if (filePath.isNotEmpty()) {
            val file = File(filePath)
            pdfView
                .fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .autoSpacing(true)
                .spacing(12)
                .pageSnap(true)
                .pageFling(false)
                .onLoadComplete {
                    "onLoadComplete".log("onLoadComplete")
                    delayed(1000) {
                        PDDocument.load(File(filePath.toString())).use { document ->
                            var pageIndex = 0
                            // Iterate over every page in the document.
                            for (page in document.pages) {
                                page.annotations.forEach { annotation ->
                                    val subtype = annotation.subtype
                                    if (annotation is PDAnnotationTextMarkup) {
                                        val quadPoints = annotation.quadPoints

                                        if (quadPoints.size >= 8) {
                                            val xValues = listOf(quadPoints[0], quadPoints[2], quadPoints[4], quadPoints[6])
                                            val yValues = listOf(quadPoints[1], quadPoints[3], quadPoints[5], quadPoints[7])
                                            val left = xValues.minOrNull() ?: 0f
                                            val right = xValues.maxOrNull() ?: 0f
                                            val top = yValues.maxOrNull() ?: 0f
                                            val bottom = yValues.minOrNull() ?: 0f
                                            val coordinates = Coordinates(left.toDouble(), top.toDouble(), right.toDouble(), bottom.toDouble())

                                            when (subtype) {
                                                PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT ->
                                                    pdfView.addHighlightExisting(HighlightModel(System.currentTimeMillis(), "", "#FFFF00", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf()))

                                                PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE ->
                                                    pdfView.addUnderlineExisting(UnderlineModel(System.currentTimeMillis(), "", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf()))

                                                PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT ->
                                                    pdfView.addStrikeThroughExisting(StrikeThroughModel(System.currentTimeMillis(), "", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf()))
                                            }
                                        }
                                    }
                                }
                            }

                            pageIndex++
                        }

                        binding?.progressBar?.visibility = View.GONE
                        "onLoadComplete".log("onLoadComplete")
                    }
                }
                .load()
        } else {
            Toast.makeText(this@EditPdfActivity, "pdf file not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private val pdfCallBack = object : PDFView.Listener {
        override fun onPreparationStarted() {
            binding?.progressBar?.visibility = View.VISIBLE
        }

        override fun onPreparationSuccess() {
        }

        override fun onPreparationFailed(error: String, e: Exception?) {
        }

        override fun onPageChanged(pageIndex: Int, paginationPageIndex: Int) {
            val page = pageIndex + 1
//            showPageNumber(page)
        }

        override fun onTextSelected(selection: TextSelectionData, rawPoint: PointF) {}

        override fun hideTextSelectionOptionWindow() {}

        override fun onTextSelectionCleared() {}

        override fun onNotesStampsClicked(comments: List<UnderlineModel>, pointOfNote: PointF) {}

        override fun loadTopPdfChunk(mergeId: Int, pageIndexToLoad: Int) {}

        override fun loadBottomPdfChunk(mergedId: Int, pageIndexToLoad: Int) {}

        override fun onScrolling() {}

        override fun onTap() {}

        override fun onMergeStart(mergeId: Int, mergeType: PdfFile.MergeType) {}

        override fun onMergeEnd(mergeId: Int, mergeType: PdfFile.MergeType) {}

        override fun onMergeFailed(
            mergeId: Int,
            mergeType: PdfFile.MergeType,
            message: String,
            exception: java.lang.Exception?
        ) {
        }
    }

    override fun ActivityEditPdfBinding.initListeners() {
        drawingView.beGone()
        actionHighlight.setOnClickListener {
            drawingView.beGone()
            val selectionData = pdfView.textSelection
            val selectedText = selectionData.getSelectedText()
            if (selectedText.isNotEmpty()) {
                pdfView.addHighlight(
                    HighlightModel(
                        System.currentTimeMillis(),
                        selectedText,
                        "#FFFF00",
                        selectionData.getPdfPageNumber(),
                        System.currentTimeMillis(),
                        selectionData.getStartEndCoordinates(),
                        selectionData.getSelections()
                    )
                )
                pdfView.clearAllTextSelectionAndCoordinates()
            } else {
                Toast.makeText(this@EditPdfActivity, "Please select text to apply highlight", Toast.LENGTH_SHORT).show()
            }
        }
        actionUnderline.setOnClickListener {
            drawingView.beGone()
            val selectionData = pdfView.textSelection
            val selectedText = selectionData.getSelectedText()
            if (selectedText.isNotEmpty()) {
                pdfView.addUnderline(
                    UnderlineModel(
                        System.currentTimeMillis(),
                        selectedText,
                        selectionData.getPdfPageNumber(),
                        System.currentTimeMillis(),
                        selectionData.getStartEndCoordinates(),
                        selectionData.getSelections()
                    )
                )
                pdfView.clearAllTextSelectionAndCoordinates()
            } else {
                Toast.makeText(this@EditPdfActivity, "Please select text to apply underline", Toast.LENGTH_SHORT).show()
            }
        }
        actionStrikethrough.setOnClickListener {
            drawingView.beGone()
            val selectionData = pdfView.textSelection
            val selectedText = selectionData.getSelectedText()
            if (selectedText.isNotEmpty()) {
                pdfView.addStrikeThrough(
                    StrikeThroughModel(
                        System.currentTimeMillis(),
                        selectedText,
                        selectionData.getPdfPageNumber(),
                        System.currentTimeMillis(),
                        selectionData.getStartEndCoordinates(),
                        selectionData.getSelections()
                    )
                )
                pdfView.clearAllTextSelectionAndCoordinates()
            } else {
                Toast.makeText(this@EditPdfActivity, "Please select text to apply strikethrough", Toast.LENGTH_SHORT).show()
            }
        }

        actionSignature.setOnClickListener {
            drawingView.beVisible()
            val pdfPage = pdfView.getCurrentPdfPage()
            val pdfPageWidth = pdfView.pdfFile?.getPageDetails()?.get(pdfPage)?.width
            val pdfPageHeight = pdfView.pdfFile?.getPageDetails()?.get(pdfPage)?.height
            val width = screenWidth
            val height = screenWidth * (pdfPageHeight?.toInt() ?: 0) / (pdfPageWidth?.toInt() ?: 0)
            val params = drawingView.layoutParams
            params.width = width
            params.height = height
            drawingView.layoutParams = params
            bottomSignController.beVisible()
        }

        actionCancel.setOnClickListener {
            drawingView.beGone()
            drawingView.clearDrawing()
            bottomSignController.beGone()
        }
        actionDone.setOnClickListener {
            drawingView.beGone()
            bottomSignController.beGone()
            val values = drawingView.getCroppedDrawingBitmap()
            values?.apply {
                val pdfPage = pdfView.currentPage
                pdfView.addSignature(SignatureModel(id = System.currentTimeMillis(), bitmap = first, page = pdfPage + 1, updatedAt = System.currentTimeMillis(), coordinates = second))
            }
            drawingView.clearDrawing()
        }
    }

    override fun ActivityEditPdfBinding.initView() {
        updateNavigationBarColor(R.color.colorCardBackground)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        buttonSave.setOnClickListener {
            viewSaveSheet("${System.currentTimeMillis()}", isDiscardEnable = false) { action: Int, value: String ->
                if (action == 1) {
                    layoutSave.beVisible()
                    CoroutineScope(Dispatchers.IO).launch {
                        binding?.pdfView?.apply {
                            if (findAllAnnotations().size > 0) {
                                val highlighter = PDFHighlighter()
                                val viewWidth = drawingView.width.toFloat()
                                val viewHeight = drawingView.height.toFloat()
                                highlighter.processAnnotations(
                                    annotations = findAllAnnotations(),
                                    viewWidth = viewWidth,
                                    viewHeight = viewHeight,
                                    src = filePath,
                                    dest = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf").absolutePath
                                )
                                { isError: Boolean, message: String? ->
                                    if (isError) {
                                        Toast.makeText(this@EditPdfActivity, message, Toast.LENGTH_SHORT).show()
                                    } else {
                                        MediaScannerConnection.scanFile(this@EditPdfActivity, arrayOf(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf").absolutePath), null, null)
                                        documentViewModel?.insertToCurrentList(mutableListOf(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf")))
                                    }
                                }
                            }
                        }?.run {
                            finish()
                        }
                    }
                } else if (action == 2) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this@EditPdfActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewSaveSheet("${System.currentTimeMillis()}", isDiscardEnable = true) { action: Int, value: String ->
                    if (action == 1) {
                        layoutSave.beVisible()
                        CoroutineScope(Dispatchers.IO).launch {
                            binding?.pdfView?.apply {
                                if (findAllAnnotations().size > 0) {
                                    val highlighter = PDFHighlighter()
                                    val viewWidth = drawingView.width.toFloat()
                                    val viewHeight = drawingView.height.toFloat()
                                    highlighter.processAnnotations(
                                        annotations = findAllAnnotations(),
                                        viewWidth = viewWidth,
                                        viewHeight = viewHeight,
                                        src = filePath,
                                        dest = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf").absolutePath
                                    )
                                    { isError: Boolean, message: String? ->
                                        if (isError) {
                                            Toast.makeText(this@EditPdfActivity, message, Toast.LENGTH_SHORT).show()
                                        } else {
                                            MediaScannerConnection.scanFile(this@EditPdfActivity, arrayOf(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf").absolutePath), null, null)
                                            documentViewModel?.insertToCurrentList(mutableListOf(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${value.replace(".pdf", "")}.pdf")))
                                        }
                                    }
                                }
                            }?.run {
                                finish()
                            }
                        }
                    } else if (action == 2) {
                        finish()
                    }
                }
            }
        })
    }
}