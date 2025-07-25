package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.source

import android.content.Context
import android.os.ParcelFileDescriptor
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileSource(
    private val file: File,
    private val startPageIndex: Int = 0,
    private val defaultPageIndexToLoad: Int = 0
) : DocumentSource {

    private var bytes: ByteArray? = null

    init {
        if (file.exists()) {
            try {
                bytes = FileInputStream(file).use { it.readBytes() }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            throw IOException("File not found: ${file.absolutePath}")
        }
    }

    @Throws(IOException::class)
    override fun createDocument(
        context: Context?,
        core: PdfiumCore?,
        password: String?
    ): PdfDocument? {
        return if (file.exists()) {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            core?.newDocument(pfd, password)
        } else {
            throw IOException("File not found: ${file.absolutePath}")
        }
    }

    override fun getBytes(): ByteArray {
        return bytes ?: throw IOException("File data not available")
    }

    override fun getFile(): File = file

    override fun getStartPageIndex(): Int = startPageIndex

    override fun defaultPageIndexToLoad(): Int = defaultPageIndexToLoad
}

