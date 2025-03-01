package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface IHyperlink extends com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.usermodel.Hyperlink {

    int getFirstRow();

    void setFirstRow(int row);

    int getLastRow();

    void setLastRow(int row);

    int getFirstColumn();

    void setFirstColumn(int col);

    int getLastColumn();

    void setLastColumn(int col);
}
