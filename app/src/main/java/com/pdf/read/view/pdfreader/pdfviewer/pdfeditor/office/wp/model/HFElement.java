package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AbstractElement;

public class HFElement extends AbstractElement {

    private final byte hfType;

    private final short elemType;

    public HFElement(short elemType, byte hftype) {
        super();
        this.hfType = hftype;
        this.elemType = elemType;
    }

    public short getType() {
        return elemType;
    }

    public byte getHFType() {
        return hfType;
    }
}
