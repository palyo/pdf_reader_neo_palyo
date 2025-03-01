package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;

public final class AreaNPtg extends Area2DPtgBase {
    public final static short sid = 0x2D;

    public AreaNPtg(LittleEndianInput in) {
        super(in);
    }

    protected byte getSid() {
        return sid;
    }
}
