package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.scroll;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.PDFView;

public interface ScrollHandle {

    void setScroll(float position);

    void setupLayout(PDFView pdfView);

    void destroyLayout();

    void setPageNum(int pageNum);

    boolean shown();

    void show();

    void hide();

    void hideDelayed();
}
