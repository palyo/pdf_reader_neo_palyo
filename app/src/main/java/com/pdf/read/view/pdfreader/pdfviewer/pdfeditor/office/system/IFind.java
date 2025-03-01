package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

public interface IFind {

    boolean find(String value);

    boolean findBackward();

    boolean findForward();

    int getPageIndex();

    void resetSearchResult();

    void dispose();
}
