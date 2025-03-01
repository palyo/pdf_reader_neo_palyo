package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view;

public interface IRoot {

    int MINLAYOUTWIDTH = 5;

    boolean canBackLayout();

    void backLayout();

    ViewContainer getViewContainer();
}
