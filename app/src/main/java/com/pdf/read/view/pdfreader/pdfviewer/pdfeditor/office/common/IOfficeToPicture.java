package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common;

import android.graphics.Bitmap;

public interface IOfficeToPicture {
    byte VIEW_CHANGING = 0;
    byte VIEW_CHANGE_END = VIEW_CHANGING + 1;

    byte getModeType();

    void setModeType(byte modeType);

    Bitmap getBitmap(int visibleWidth, int visibleHeight);

    void callBack(Bitmap bitmap);

    boolean isZoom();

    void dispose();
}
