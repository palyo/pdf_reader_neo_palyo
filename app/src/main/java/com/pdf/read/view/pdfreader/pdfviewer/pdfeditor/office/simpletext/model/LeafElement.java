package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;

public class LeafElement extends AbstractElement {

    private String text;

    public LeafElement(String text) {
        super();
        this.text = text;
    }

    public short getType() {
        return WPModelConstant.LEAF_ELEMENT;
    }

    public String getText(IDocument doc) {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.setEndOffset(getStartOffset() + text.length());
    }

    public void dispose() {
        super.dispose();
        text = null;
    }
}
