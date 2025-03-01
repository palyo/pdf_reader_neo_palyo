package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.IClientAnchor;

public interface Picture {

    void resize();

    void resize(double scale);

    IClientAnchor getPreferredSize();

    PictureData getPictureData();

}
