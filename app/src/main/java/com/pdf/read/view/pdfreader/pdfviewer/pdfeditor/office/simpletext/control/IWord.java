package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public interface IWord {

    IHighlight getHighlight();

    Rectangle modelToView(long offset, Rectangle rect, boolean isBack);

    IDocument getDocument();

    String getText(long start, long end);

    long viewToModel(int x, int y, boolean isBack);

    byte getEditType();

    IAnimation getParagraphAnimation(int pargraphID);

    IShape getTextBox();

    IControl getControl();

    void dispose();
}
