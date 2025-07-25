package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.filter

import android.graphics.*
import jp.co.cyberagent.android.gpuimage.filter.*

class GPUBWFilter(value: Float) : GPUImageFilterGroup() {

    private val blendFilter: GPUImageHardLightBlendFilter

    init {
        addFilter(GPUImageFilter())
        addFilter(GPULaplacianTextFilter(value))
        addFilter(GPUImageWeakPixelInclusionFilter())
        addFilter(GPUImageColorInvertFilter())
        addFilter(GPUImageGaussianBlurFilter(0.2f))
        blendFilter = GPUImageHardLightBlendFilter()
        addFilter(blendFilter)
        addFilter(GPUImageSaturationFilter(0f))
        updateMergedFilters()
    }

    fun setBlendBitmap(bitmap: Bitmap?) {
        blendFilter.onDestroy()
        blendFilter.bitmap = bitmap
    }
}