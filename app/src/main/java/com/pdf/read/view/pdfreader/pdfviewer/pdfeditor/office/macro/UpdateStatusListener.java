package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

public interface UpdateStatusListener {

    byte ALLPages = -1;

    void updateStatus();

    void changeZoom();

    void changePage();

    void completeLayout();

    void updateViewImage(Integer[] views);

}
