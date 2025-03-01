package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pdf;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class RepaintAreaInfo {

    public Bitmap bm;

    public int viewWidth;

    public int viewHeight;

    public Rect repaintArea;

    public RepaintAreaInfo(Bitmap reapintBitmap, int viewWidth, int viewHeight, Rect repaintArea) {
        this.bm = reapintBitmap;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.repaintArea = repaintArea;
    }
}
