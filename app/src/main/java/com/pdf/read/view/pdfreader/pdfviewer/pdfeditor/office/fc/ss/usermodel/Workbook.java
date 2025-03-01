package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface Workbook {

    int PICTURE_TYPE_EMF = 2;

    int PICTURE_TYPE_WMF = 3;

    int PICTURE_TYPE_PICT = 4;

    int PICTURE_TYPE_JPEG = 5;

    int PICTURE_TYPE_PNG = 6;

    int PICTURE_TYPE_DIB = 7;

    int SHEET_STATE_VISIBLE = 0;

    int SHEET_STATE_HIDDEN = 1;

    int SHEET_STATE_VERY_HIDDEN = 2;

    int getNumberOfSheets();

    Sheet getSheetAt(int index);

}
