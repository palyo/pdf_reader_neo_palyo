package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.control;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IHighlight;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class SSEditor implements IWord {

    private Spreadsheet ss;

    public SSEditor(Spreadsheet ss) {
        this.ss = ss;
    }

    public IHighlight getHighlight() {

        return null;
    }

    @Override
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {

        return null;
    }

    @Override
    public IDocument getDocument() {

        return null;
    }

    @Override
    public String getText(long start, long end) {

        return "";
    }

    @Override
    public long viewToModel(int x, int y, boolean isBack) {

        return 0;
    }

    @Override
    public byte getEditType() {
        return KeyKt.APPLICATION_TYPE_SS;
    }

    @Override
    public IAnimation getParagraphAnimation(int pargraphID) {

        return null;
    }

    @Override
    public IShape getTextBox() {

        return null;
    }

    @Override
    public IControl getControl() {

        return ss.getControl();
    }

    @Override
    public void dispose() {
        ss = null;
    }
}
