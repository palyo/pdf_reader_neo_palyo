package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface Textbox {

    short OBJECT_TYPE_TEXT = 6;

    RichTextString getString();

    void setString(RichTextString string);

    int getMarginLeft();

    void setMarginLeft(int marginLeft);

    int getMarginRight();

    void setMarginRight(int marginRight);

    int getMarginTop();

    void setMarginTop(int marginTop);

    int getMarginBottom();

    void setMarginBottom(int marginBottom);

}
