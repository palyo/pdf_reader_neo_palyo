package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.CDATA;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;

public class FlyweightCDATA extends AbstractCDATA implements CDATA {
    protected String text;

    public FlyweightCDATA(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultCDATA(parent, getText());
    }
}