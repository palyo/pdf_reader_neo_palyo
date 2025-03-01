package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class TblPtg extends ControlPtg {
    public final static short sid = 0x02;
    private final static int SIZE = 5;
    private final int field_1_first_row;
    private final int field_2_first_col;

    public TblPtg(LittleEndianInput in) {
        field_1_first_row = in.readUShort();
        field_2_first_col = in.readUShort();
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

        throw new RuntimeException("Table and Arrays are not yet supported");
    }

    public String toString() {
        String buffer = "[Data Table - Parent cell is an interior cell in a data table]\n" + "top left row = " + getRow() + "\n" +
                "top left col = " + getColumn() + "\n";
        return buffer;
    }
}
