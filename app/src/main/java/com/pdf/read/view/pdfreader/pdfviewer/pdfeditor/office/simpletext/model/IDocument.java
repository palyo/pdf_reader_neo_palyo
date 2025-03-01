package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

public interface IDocument {

    long getArea(long offset);

    long getAreaStart(long offset);

    long getAreaEnd(long offset);

    long getLength(long offset);

    IElement getSection(long offset);

    IElement getParagraph(long offset);

    IElement getParagraphForIndex(int index, long area);

    void appendParagraph(IElement element, long offset);

    void appendSection(IElement elem);

    void appendElement(IElement elem, long offset);

    IElement getLeaf(long offset);

    IElement getHFElement(long area, byte type);

    IElement getFEElement(long offset);

    void insertString(String str, IAttributeSet attr, long offset);

    void setSectionAttr(long start, int len, IAttributeSet attr);

    void setParagraphAttr(long start, int len, IAttributeSet attr);

    void setLeafAttr(long start, int len, IAttributeSet attr);

    int getParaCount(long area);

    String getText(long start, long end);

    void dispose();

}
