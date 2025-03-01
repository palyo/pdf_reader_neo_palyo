package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;

public final class Background extends Shape {

    Background(EscherContainerRecord escherRecord, Shape parent) {
        super(escherRecord, parent);
    }

    protected EscherContainerRecord createSpContainer(boolean isChild) {
        return null;
    }
}
