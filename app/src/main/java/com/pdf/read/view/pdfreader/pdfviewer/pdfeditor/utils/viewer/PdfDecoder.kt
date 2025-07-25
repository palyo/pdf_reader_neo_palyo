package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer

import android.app.Activity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.source.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.*
import com.shockwave.pdfium.*
import com.shockwave.pdfium.util.Size
import com.tom_roush.pdfbox.cos.*
import com.tom_roush.pdfbox.io.*
import com.tom_roush.pdfbox.pdmodel.*
import kotlinx.coroutines.*
import java.io.*
import java.lang.ref.*

class PdfDecoder(
    pdfView: PDFView,
    private val pdfiumCore: PdfiumCore?,
) {
    private var cancelled = false
    private val pdfViewReference = WeakReference(pdfView)
    private lateinit var pdfFile: PdfFile
    private var lineId: Int = 0
    private var wordId: Int = 0
    private var charId: Int = 0

    suspend fun decode(docSource: DocumentSource, password: String?) {
        val result = withContext(Dispatchers.Default) {
            try {
                val pdfView = pdfViewReference.get() ?: return@withContext Exception("PDFView is null")
                val pdfDocument = docSource.createDocument(pdfView.context, pdfiumCore, password)
                    ?: return@withContext Exception("Failed to create PDF document")

                val pageDetails = when (docSource) {
                    is FileSource -> getPdfPageDetails(docSource.getFile())
                    is ByteArraySource -> getPdfPageDetails(docSource.getBytes())
                    else -> arrayListOf()
                }

                pdfFile = PdfFile(
                    pdfiumCore, pdfDocument, pdfView.pageFitPolicy,
                    getViewSize(pdfView), null, pdfView.isSwipeVertical,
                    pdfView.spacingPx, pdfView.isAutoSpacingEnabled,
                    pdfView.isFitEachPage,
                    pageDetails,
                    docSource.getStartPageIndex(),
                )
                null
            } catch (t: Exception) {
                t.printStackTrace()
                t
            }
        }

        withContext(Dispatchers.Main) {
            val pdfView = pdfViewReference.get()
            if (pdfView == null) {
                finishActivitySafely()
                return@withContext
            }

            if (result != null) {
                pdfView.loadError(result)
            } else if (!cancelled) {
                pdfView.loadComplete(pdfFile)
            }
        }
    }

    private fun getViewSize(pdfView: PDFView) = Size(pdfView.width, pdfView.height)

    fun cancel() {
        cancelled = true
    }

    private fun getPdfPageDetails(pdfFileBytes: ByteArray): ArrayList<PageModel> {
        val pageDetails = arrayListOf<PageModel>()

        ByteArrayInputStream(pdfFileBytes).use { stream ->
            PDDocument.load(stream, MemoryUsageSetting.setupTempFileOnly()).use { pdfDocument ->
                for (index in 0 until pdfDocument.numberOfPages) {
                    val documentPageRect = pdfDocument.getPage(index).mediaBox
                    val textStripper = PdfTextStripper(index + 1, lineId, wordId, charId).apply {
                        sortByPosition = true
                        startPage = index + 1
                        endPage = index + 1
                    }
                    textStripper.getText(pdfDocument)

                    lineId = textStripper.getLastLineId() + 1
                    wordId = textStripper.getLastWordId() + 1
                    charId = textStripper.getLastCharId() + 1

                    pageDetails.add(
                        PageModel(documentPageRect.width, documentPageRect.height, textStripper.getTextCoordinates())
                    )
                }
            }
        }
        return pageDetails
    }

    private fun getPdfPageDetails(pdfFile: File): ArrayList<PageModel> {
        val pageDetails = arrayListOf<PageModel>()

        // Load PDF with memory optimization
        PDDocument.load(pdfFile, MemoryUsageSetting.setupMainMemoryOnly()).use { pdfDocument ->
            val totalPages = pdfDocument.numberOfPages

            for (index in 0 until totalPages) {
                val page = pdfDocument.getPage(index)  // Load only one page at a time
                val documentPageRect = page.mediaBox

                val textStripper = PdfTextStripper(index + 1, lineId, wordId, charId).apply {
                    sortByPosition = true
                    startPage = index + 1
                    endPage = index + 1
                }

                val textCoordinates = textStripper.getTextCoordinates() // Extract text

                // Increment IDs
                lineId = textStripper.getLastLineId() + 1
                wordId = textStripper.getLastWordId() + 1
                charId = textStripper.getLastCharId() + 1

                // Store only necessary data
                pageDetails.add(PageModel(documentPageRect.width, documentPageRect.height, textCoordinates))

                // Force GC after processing a few pages
                if (index % 5 == 0) System.gc()
            }
        }
        return pageDetails
    }


    private fun finishActivitySafely() {
            (pdfViewReference.get()?.context as? Activity)?.finish()
    }
}

