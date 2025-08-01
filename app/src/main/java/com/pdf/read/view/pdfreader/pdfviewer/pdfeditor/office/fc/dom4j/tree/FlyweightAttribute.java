package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Namespace;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.QName;

public class FlyweightAttribute extends AbstractAttribute {
    private final QName qname;
    protected String value;

    public FlyweightAttribute(QName qname) {
        this.qname = qname;
    }

    public FlyweightAttribute(QName qname, String value) {
        this.qname = qname;
        this.value = value;
    }

    public FlyweightAttribute(String name, String value) {
        this.qname = getDocumentFactory().createQName(name);
        this.value = value;
    }

    public FlyweightAttribute(String name, String value, Namespace namespace) {
        this.qname = getDocumentFactory().createQName(name, namespace);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public QName getQName() {
        return qname;
    }
}