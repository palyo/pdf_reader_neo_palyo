package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.ErrorConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class RefErrorPtg extends OperandPtg {

    public final static byte sid = 0x2A;
    private final static int SIZE = 5;
    private final int field_1_reserved;

    public RefErrorPtg() {
        field_1_reserved = 0;
    }

    public RefErrorPtg(LittleEndianInput in) {
        field_1_reserved = in.readInt();
    }

    public String toString() {
        return getClass().getName();
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
        out.writeInt(field_1_reserved);
    }

    public int getSize() {
        return SIZE;
    }

    public String toFormulaString() {
        return ErrorConstants.getText(ErrorConstants.ERROR_REF);
    }

    public byte getDefaultOperandClass() {
        return Ptg.CLASS_REF;
    }
}
