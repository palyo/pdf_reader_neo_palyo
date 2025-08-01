package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Text;

public class FlyweightText extends AbstractText implements Text {
    protected String text;

    public FlyweightText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultText(parent, getText());
    }
}