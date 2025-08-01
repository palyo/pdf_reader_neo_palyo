package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ExpPtg extends ControlPtg {
    public final static short sid = 0x1;
    private final static int SIZE = 5;
    private final int field_1_first_row;
    private final int field_2_first_col;

    public ExpPtg(LittleEndianInput in) {
        field_1_first_row = in.readShort();
        field_2_first_col = in.readShort();
    }

    public ExpPtg(int firstRow, int firstCol) {
        this.field_1_first_row = firstRow;
        this.field_2_first_col = firstCol;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(sid + getPtgClass());
        out.writeShort(field_1_first_row);
        out.writeShort(field_2_first_col);
    }

    public int getSize() {
        return SIZE;
    }

    public int getRow() {
        return field_1_first_row;
    }

    public int getColumn() {
        return field_2_first_col;
    }

    public String toFormulaString() {
        throw new RuntimeException("Coding Error: Expected ExpPtg to be converted from Shared to Non-Shared Formula by ValueRecordsAggregate, but it wasn't");
    }

    public String toString() {
        String buffer = "[Array Formula or Shared Formula]\n" + "row = " + getRow() + "\n" +
                "col = " + getColumn() + "\n";
        return buffer;
    }
}
