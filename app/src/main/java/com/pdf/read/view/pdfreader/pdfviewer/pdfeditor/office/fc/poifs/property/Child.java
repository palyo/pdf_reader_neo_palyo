package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property;

public interface Child {

    Child getNextChild();

    void setNextChild(final Child child);

    Child getPreviousChild();

    void setPreviousChild(final Child child);
}   

