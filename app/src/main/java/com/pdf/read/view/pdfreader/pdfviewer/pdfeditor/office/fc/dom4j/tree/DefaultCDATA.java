package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;

public class DefaultCDATA extends FlyweightCDATA {
    private Element parent;

    public DefaultCDATA(String text) {
        super(text);
    }

    public DefaultCDATA(Element parent, String text) {
        super(text);
        this.parent = parent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public boolean supportsParent() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }
}