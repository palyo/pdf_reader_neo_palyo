package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;

public final class Placeholder extends TextBox {

    private Placeholder(EscherContainerRecord escherRecord, Shape parent) {
        super(escherRecord, parent);
    }

    public Placeholder(Shape parent) {
        super(parent);
    }

    public Placeholder() {
        super();
    }

    protected EscherContainerRecord createSpContainer(boolean isChild) {
        _escherContainer = super.createSpContainer(isChild);

        return _escherContainer;
    }
}
