package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.graphics.Bitmap;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ReaderThumbnail;

public class ThumbnailKit {
    private static final ThumbnailKit kit = new ThumbnailKit();

    public static ThumbnailKit instance() {
        return kit;
    }

    public Bitmap getPPTThumbnail(String filePath, int width, int height) {
        try {
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                    && width > 0
                    && height > 0
                    && (lowerCase.endsWith(KeyKt.FILE_TYPE_PPT)
                    || lowerCase.endsWith(KeyKt.FILE_TYPE_POT))) {
                return ReaderThumbnail.instance().getThumbnailForPPT(filePath, width, height);
            }
        } catch (Exception e) {

        }

        return null;
    }

    public Bitmap getPPTXThumbnail(String filePath) {
        try {
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                    && (lowerCase.endsWith(KeyKt.FILE_TYPE_PPTX)
                    || lowerCase.endsWith(KeyKt.FILE_TYPE_PPTM)
                    || lowerCase.endsWith(KeyKt.FILE_TYPE_POTX)
                    || lowerCase.endsWith(KeyKt.FILE_TYPE_POTM))) {
                return ReaderThumbnail.instance().getThumbnailForPPTX(filePath);
            }
        } catch (Exception e) {

        }

        return null;
    }

    public Bitmap getPDFThumbnail(String filePath, int zoom) {
        try {

            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                    && lowerCase.endsWith(KeyKt.FILE_TYPE_PDF)
                    && zoom > 0 && zoom <= Application.MAXZOOM_THUMBNAIL) {
                return ReaderThumbnail.instance().getThumbnailForPDF(filePath, zoom / (float) KeyKt.STANDARD_RATE);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
