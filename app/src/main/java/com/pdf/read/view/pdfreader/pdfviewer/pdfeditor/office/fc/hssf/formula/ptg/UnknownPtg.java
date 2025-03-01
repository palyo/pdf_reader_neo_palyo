package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public class UnknownPtg extends Ptg {
    private final int _sid;
    private final short size = 1;

    public UnknownPtg(int sid) {
        _sid = sid;
    }

    public boolean isBaseToken() {
        return true;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(_sid);
    }

    public int getSize() {
        return size;
    }

    public String toFormulaString() {
        return "UNKNOWN";
    }

    public byte getDefaultOperandClass() {
        return Ptg.CLASS_VALUE;
    }
}
