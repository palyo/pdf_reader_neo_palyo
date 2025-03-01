package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer

import android.graphics.*
import android.media.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*
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
                .pageSnap(pageSnap = true)
                .autoSpacing(autoSpacing = true)
                .enableDoubleTap(enableDoubleTap = true)
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
            binding?.progressBar?.visibility = View.GONE
        }

        override fun onPreparationFailed(error: String, e: Exception?) {
            binding?.progressBar?.visibility = View.GONE
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
        }
        actionUnderline.setOnClickListener {
            drawingView.beGone()
            val selectionData = pdfView.textSelection
            val selectedText = selectionData.getSelectedText()
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
        }
        actionStrikethrough.setOnClickListener {
            drawingView.beGone()
            val selectionData = pdfView.textSelection
            val selectedText = selectionData.getSelectedText()
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
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback(this@EditPdfActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewSaveSheet("${System.currentTimeMillis()}") { action: Int, value: String ->
                    if (action == 1) {
                        binding?.pdfView?.apply {
                            if (findAllAnnotations().size > 0) {
                                val highlighter = PDFHighlighter()
                                val viewWidth = pdfView.width.toFloat()
                                val viewHeight = pdfView.height.toFloat()
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
                    } else if (action == 2) {
                        finish()
                    }
                }
            }
        })
    }
}