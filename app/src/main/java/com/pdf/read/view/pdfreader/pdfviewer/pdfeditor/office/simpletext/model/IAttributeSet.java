package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

public interface IAttributeSet {

    int getID();

    void setAttribute(short attrID, int value);

    void removeAttribute(short attrID);

    int getAttribute(short attrID);

    void mergeAttribute(IAttributeSet attr);

    IAttributeSet clone();

    void dispose();

}
