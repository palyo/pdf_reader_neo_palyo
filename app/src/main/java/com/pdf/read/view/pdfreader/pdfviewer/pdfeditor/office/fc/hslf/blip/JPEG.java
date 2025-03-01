package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.blip;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture;

public final class JPEG extends Bitmap {

    public int getType() {
        return Picture.JPEG;
    }

    public int getSignature() {
        return 0x46A0;
    }
}
