package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.annotation.*
import android.content.*
import android.provider.*
import android.webkit.*
import coder.apps.space.library.extension.*
import java.io.*

@SuppressLint("Range")
fun Context.fetchDocument(load: ((File) -> Unit)? = null, complete: ((MutableList<File>) -> Unit)? = null, type: String? = ALL_FILE): MutableList<File> {
    val files: MutableList<File> = mutableListOf()
    try {
        val pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
        val docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx")
        val xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls")
        val xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx")
        val ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt")
        val pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx")
        val txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
        val where = when (type) {
            DOC_FILE, XLS_FILE, PPT_FILE -> (MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?")
            TXT_FILE, PDF_FILE -> MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            else -> (MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?")
        }
        val args = when (type) {
            DOC_FILE -> arrayOf(doc, docx)
            XLS_FILE -> arrayOf(xls, xlsx)
            PPT_FILE -> arrayOf(ppt, pptx)
            TXT_FILE -> arrayOf(txt)
            PDF_FILE -> arrayOf(pdf)
            else -> arrayOf(pdf, doc, docx, xls, xlsx, ppt, pptx, txt)
        }
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_MODIFIED), where, args,
            "${MediaStore.Files.FileColumns.DATE_MODIFIED}"
        )
        if (cursor?.moveToLast() == true) {
            do {
                if (Thread.interrupted()) {
                    return files
                }
                val file = File(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)))
                if (file.length() > 100) {
                    files.add(file)
                    load?.invoke(file)
                }
            } while (cursor.moveToPrevious())
        }

        File(filesDir, "samples").listFiles()?.filter { file ->
            args.contains(file.mimeType)
        }?.forEach { file ->
            files.add(file)
            load?.invoke(file)
        }
        cursor?.close()
        complete?.invoke(files)
        return files
    } catch (e: Exception) {
        complete?.invoke(files)
        return files
    }
}