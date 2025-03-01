package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

public interface IElement {

    short getType();

    long getStartOffset();

    void setStartOffset(long start);

    long getEndOffset();

    void setEndOffset(long end);

    IAttributeSet getAttribute();

    void setAttribute(IAttributeSet attrSet);

    String getText(IDocument doc);

    void dispose();

}
