package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.content.Context
import android.graphics.*
import android.graphics.pdf.*
import android.media.MediaScannerConnection
import coder.apps.space.library.extension.log
import kotlinx.coroutines.*
import java.io.*

suspend fun saveImagesToPdf(
    imagePaths: MutableList<String>,
    destinationDir: File,
    fileName: String,
    progress: ((current: Int, total: Int, percentage: Int, imagePath: String) -> Unit)? = null,
    complete: (savedPaths: MutableList<String?>) -> Unit,
): String? = withContext(Dispatchers.IO) {
    val pdfDocument = PdfDocument()
    val totalImages = imagePaths.size

    try {
        for ((index, imagePath) in imagePaths.withIndex()) {
            val bitmap = BitmapFactory.decodeFile(imagePath) ?: continue
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            val progressPercentage = ((index + 1) * 100) / totalImages
            progress?.invoke(index + 1, totalImages, progressPercentage, imagePath)
        }

        val pdfFile = File(destinationDir, "$fileName.pdf")
        withContext(Dispatchers.IO) {
            FileOutputStream(pdfFile).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
        }

        complete.invoke(mutableListOf(pdfFile.absolutePath))
        return@withContext pdfFile.absolutePath

    } catch (e: IOException) {
        e.printStackTrace()
        complete.invoke(mutableListOf())
        return@withContext null
    } finally {
        pdfDocument.close()
    }
}

suspend fun saveBitmapsToDirectoryWithProgress(
    bitmaps: MutableList<Bitmap>,
    destinationDir: File,
    progress: (current: Int, total: Int, percentage: Int, bitmapIndex: Int) -> Unit,
    complete: (savedPaths: MutableList<String?>) -> Unit,
): MutableList<String?> = withContext(Dispatchers.IO) {
    val savedPaths = mutableListOf<String?>()
    val totalBitmaps = bitmaps.size

    for ((index, bitmap) in bitmaps.withIndex()) {
        val file = File(destinationDir, "_${index + 1}.jpeg")
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream)
            fileOutputStream.flush()
            savedPaths.add(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            savedPaths.add(null)
        } finally {
            fileOutputStream?.close()
        }

        val progressPercentage = ((index + 1) * 100) / totalBitmaps
        progress.invoke(index + 1, totalBitmaps, progressPercentage, index)
    }

    complete.invoke(savedPaths)
    return@withContext savedPaths
}

suspend fun Context.copyFilesWithProgress(
    files: MutableList<String>, destinationDir: File,
    progress: (current: Int, total: Int, percentage: Int, imagePath: String) -> Unit,
    complete: (savedPaths: MutableList<String>) -> Unit,
) = withContext(Dispatchers.IO) {
    val copiedFiles = mutableListOf<String>()
    val totalFiles = files.size

    files.forEachIndexed { index, fileSrc ->
        val fileDest = File(destinationDir, "$index.${File(fileSrc).extension}")
        try {
            File(fileSrc).copyTo(fileDest, overwrite = true)
            copiedFiles.add(fileDest.absolutePath)
            MediaScannerConnection.scanFile(this@copyFilesWithProgress, arrayOf(fileDest.absolutePath), null, null)
        } catch (e: Exception) {
            "copyFilesWithProgressLocalMedia".log("$e")
        }

        val progressPercentage = ((index + 1) * 100) / totalFiles
        progress.invoke(index + 1, totalFiles, progressPercentage, fileSrc)
    }
    complete.invoke(copiedFiles)
}