package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;

public class PDFLibKit {
    private static final PDFLibKit kit = new PDFLibKit();
    private static PDFLib lib = PDFLib.getPDFLib();

    public static PDFLibKit instance() {
        return kit;
    }

    public synchronized void openFileSync(String filename) throws Exception {
        lib.openFileSync(filename);
    }

    public int getPageCountSync() {
        return lib.getPageCountSync();
    }

    public Rect[] getAllPagesSize() {
        return lib.getAllPagesSize();
    }

    public synchronized void drawPageSync(Bitmap bitmap, int pageIndex, float pageWidth, float pageHeight,
                                          int paintX, int paintY, int paintWidth, int paintHeight, int drawObject) {
        lib.drawPageSync(bitmap, pageIndex, pageWidth, pageHeight, paintX, paintY, paintWidth, paintHeight, drawObject);
    }

    public synchronized boolean hasPasswordSync() {
        return lib.hasPasswordSync();
    }

    public synchronized boolean authenticatePasswordSync(String password) {
        return lib.authenticatePasswordSync(password);
    }

    public void setStopFlagSync(int flag) {
        lib.setStopFlagSync(flag);
    }

    public synchronized void dispose() {
        lib = null;
    }
}
