package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.blip;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.PictureData;

public abstract class Bitmap extends PictureData {

    public byte[] getData() {
        byte[] rawdata = getRawData();
        byte[] imgdata = new byte[rawdata.length - 17];
        System.arraycopy(rawdata, 17, imgdata, 0, imgdata.length);
        return imgdata;
    }

    public void setData(byte[] data) throws IOException {
    }
}
