package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.graphics.*
import androidx.exifinterface.media.*
import jp.co.cyberagent.android.gpuimage.*
import jp.co.cyberagent.android.gpuimage.filter.*
import jp.co.cyberagent.android.gpuimage.util.*
import java.io.*

fun getExifRotationDegrees(file: String): Int {
    return try {
        val exif = ExifInterface(file)
        when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: IOException) {
        e.printStackTrace()
        0
    }
}

fun correctImageRotation(file: String): Bitmap {
    val rotationDegrees = getExifRotationDegrees(file)
    val bitmap = BitmapFactory.decodeFile(file)
    val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun imageRotation(bitmap: Bitmap, rotationDegrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(rotationDegrees) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun GPUImageView.gpuRotateImage(angle: Float) {
    val transformFilter = GPUImageTransformFilter()
    transformFilter.setTransform3D(getRotationMatrix(angle))
    filter = transformFilter
    requestRender()
}

private fun getRotationMatrix(angle: Float): FloatArray {
    val radians = Math.toRadians(angle.toDouble()).toFloat()
    val cos = Math.cos(radians.toDouble()).toFloat()
    val sin = Math.sin(radians.toDouble()).toFloat()

    val matrix = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    )

    matrix[0] = cos
    matrix[1] = -sin
    matrix[4] = sin
    matrix[5] = cos

    matrix[10] = 1f

    return matrix
}