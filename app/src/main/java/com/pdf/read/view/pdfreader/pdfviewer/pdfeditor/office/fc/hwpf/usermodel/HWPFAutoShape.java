package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;

public class HWPFAutoShape extends HWPFShape {
    public HWPFAutoShape(EscherContainerRecord escherRecord, HWPFShape parent) {
        super(escherRecord, parent);
    }

    public String getShapeName() {
        return ShapeKit.getShapeName(escherContainer);
    }
}
