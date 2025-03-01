package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;

public interface DialogListener {

    byte DIALOGTYPE_PASSWORD = ICustomDialog.DIALOGTYPE_PASSWORD;

    byte DIALOGTYPE_ENCODE = ICustomDialog.DIALOGTYPE_ENCODE;

    byte DIALOGTYPE_LOADING = ICustomDialog.DIALOGTYPE_LOADING;

    byte DIALOGTYPE_ERROR = ICustomDialog.DIALOGTYPE_ERROR;

    byte DIALOGTYPE_FIND = ICustomDialog.DIALOGTYPE_FIND;

    void showDialog(byte type);

    void dismissDialog(byte type);
}
