package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UnionPtg extends OperationPtg {
    public final static byte sid = 0x10;

    public static final OperationPtg instance = new UnionPtg();

    private UnionPtg() {

    }

    public boolean isBaseToken() {
        return true;
    }

    public int getSize() {
        return 1;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
    }

    public String toFormulaString() {
        return ",";
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                "," +
                operands[1];
        return buffer;
    }

    public int getNumberOfOperands() {
        return 2;
    }

}
