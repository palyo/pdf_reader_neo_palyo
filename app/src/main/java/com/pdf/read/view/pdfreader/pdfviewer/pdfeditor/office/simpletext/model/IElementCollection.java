package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

public interface IElementCollection {

    IElement getElement(long offset);

    IElement getElementForIndex(int index);

    int size();

    void dispose();

}
