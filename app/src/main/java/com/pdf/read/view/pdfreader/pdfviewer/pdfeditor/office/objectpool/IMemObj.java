package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.objectpool;

public interface IMemObj {

    void free();

    IMemObj getCopy();
}
