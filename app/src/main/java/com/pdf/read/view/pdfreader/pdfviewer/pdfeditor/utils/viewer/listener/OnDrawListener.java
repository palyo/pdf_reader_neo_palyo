package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.listener;

import android.graphics.Canvas;

public interface OnDrawListener {
    void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage, float zoom);
}
