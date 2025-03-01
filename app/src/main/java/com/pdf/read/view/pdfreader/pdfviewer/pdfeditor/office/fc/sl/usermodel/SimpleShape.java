package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

public interface SimpleShape extends Shape {
    Fill getFill();

    LineStyle getLineStyle();

    Hyperlink getHyperlink();

    void setHyperlink(Hyperlink hyperlink);
}
