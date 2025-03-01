package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

public interface PictureData {
    int getType();

    byte[] getUID();

    byte[] getData();

    void setData(byte[] data);
}
