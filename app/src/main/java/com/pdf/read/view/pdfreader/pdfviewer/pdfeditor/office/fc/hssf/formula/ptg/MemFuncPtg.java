package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class MemFuncPtg extends OperandPtg {

    public final static byte sid = 0x29;
    private final int field_1_len_ref_subexpression;

    public MemFuncPtg(LittleEndianInput in) {
        this(in.readUShort());
    }

    public MemFuncPtg(int subExprLen) {
        field_1_len_ref_subexpression = subExprLen;
    }

    public int getSize() {
        return 3;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
        out.writeShort(field_1_len_ref_subexpression);
    }

    public String toFormulaString() {
        return "";
    }

    public byte getDefaultOperandClass() {
        return Ptg.CLASS_REF;
    }

    public int getNumberOfOperands() {
        return field_1_len_ref_subexpression;
    }

    public int getLenRefSubexpression() {
        return field_1_len_ref_subexpression;
    }

    @Override
    public String toString() {
        String sb = getClass().getName() + " [len=" +
                field_1_len_ref_subexpression +
                "]";
        return sb;
    }
}
