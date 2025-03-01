package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class MemAreaPtg extends OperandPtg {
    public final static short sid = 0x26;
    private final static int SIZE = 7;
    private final int field_1_reserved;
    private final int field_2_subex_len;

    public MemAreaPtg(int subexLen) {
        field_1_reserved = 0;
        field_2_subex_len = subexLen;
    }

    public MemAreaPtg(LittleEndianInput in) {
        field_1_reserved = in.readInt();
        field_2_subex_len = in.readShort();
    }

    public int getLenRefSubexpression() {
        return field_2_subex_len;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
        out.writeInt(field_1_reserved);
        out.writeShort(field_2_subex_len);
    }

    public int getSize() {
        return SIZE;
    }

    public String toFormulaString() {
        return "";
    }

    public byte getDefaultOperandClass() {
        return Ptg.CLASS_VALUE;
    }

    @Override
    public String toString() {
        String sb = getClass().getName() + " [len=" +
                field_2_subex_len +
                "]";
        return sb;
    }
}
