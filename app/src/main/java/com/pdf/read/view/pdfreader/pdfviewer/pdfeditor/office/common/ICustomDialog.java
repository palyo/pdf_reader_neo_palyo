package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common;

public interface ICustomDialog {
    byte DIALOGTYPE_PASSWORD = 0;
    byte DIALOGTYPE_ENCODE = 1;
    byte DIALOGTYPE_LOADING = 2;
    byte DIALOGTYPE_ERROR = 3;
    byte DIALOGTYPE_FIND = 4;

    void showDialog(byte type);

    void dismissDialog(byte type);
}
