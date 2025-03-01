package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.graphics.Bitmap;

public interface OfficeToPictureListener {

    Bitmap getBitmap(int componentWidth, int componentHeight);

    void callBack(Bitmap bitmap);
}
