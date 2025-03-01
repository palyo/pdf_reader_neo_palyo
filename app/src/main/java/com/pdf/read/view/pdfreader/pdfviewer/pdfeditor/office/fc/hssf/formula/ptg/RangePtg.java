package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class RangePtg extends OperationPtg {
    public final static int SIZE = 1;
    public final static byte sid = 0x11;

    public static final OperationPtg instance = new RangePtg();

    private RangePtg() {

    }

    public boolean isBaseToken() {
        return true;
    }

    public int getSize() {
        return SIZE;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
    }

    public String toFormulaString() {
        return ":";
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                ":" +
                operands[1];
        return buffer;
    }

    public int getNumberOfOperands() {
        return 2;
    }

}
