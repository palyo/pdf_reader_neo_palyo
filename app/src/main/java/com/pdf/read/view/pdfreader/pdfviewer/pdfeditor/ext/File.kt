package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.app.Activity
import android.content.*
import android.os.*
import android.print.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import kotlinx.coroutines.*
import java.io.*

val File.iconFile: Int
    get() {
        return if (isPdf) R.drawable.ic_icon_file_pdf
        else if (isWord) R.drawable.ic_icon_file_doc
        else if (isExcel) R.drawable.ic_icon_file_xls
        else if (isPpt) R.drawable.ic_icon_file_ppt
        else R.drawable.ic_icon_file_unknown
    }
val defaultStorageLocation: String
    get() {
        val s = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS
        ).absolutePath + "/Pdf Reader - Pdf Viewer/"
        val myDir = File(s)
        createOrExistsDir(myDir)
        return s
    }

fun printPdfUsingPrintManager(context: Activity, pdfFile: File) {

}

fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

suspend fun MutableList<File>.deleteFilesWithProgress(
    progress: ((progress: Int) -> Unit)? = null,
    complete: () -> Unit
) {
    val totalFiles = size
    var deletedFiles = 0
    if (this.isNotEmpty()) {
        this.forEach { file ->
            if (file.exists()) {
                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }
            }

            deletedFiles++

            withContext(Dispatchers.Main) {
                val progressValue = (deletedFiles * 100) / totalFiles
                progress?.invoke(progressValue)
                if (deletedFiles == totalFiles) {
                    complete()
                }
            }

            delay(100)
        }
    } else complete()
}

fun File.renameFile(newFileName: String): File {
    val directory = parentFile
    val newFile = File(directory, newFileName)
    val isRenamed = renameTo(newFile)
    return if (isRenamed) newFile else this
}
