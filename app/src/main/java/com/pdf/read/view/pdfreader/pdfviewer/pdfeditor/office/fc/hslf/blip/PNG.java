package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.blip;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture;

public final class PNG extends Bitmap {

    public byte[] getData() {
        byte[] data = super.getData();

        return data;
    }

    public int getType() {
        return Picture.PNG;
    }

    public int getSignature() {
        return 0x6E00;
    }
}
