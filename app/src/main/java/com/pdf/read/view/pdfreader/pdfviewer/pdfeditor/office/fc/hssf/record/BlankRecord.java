package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BlankRecord extends StandardRecord implements CellValueRecordInterface {
    public final static short sid = 0x0201;
    private int field_1_row;
    private short field_2_col;
    private short field_3_xf;

    public BlankRecord() {
    }

    public BlankRecord(RecordInputStream in) {
        field_1_row = in.readUShort();
        field_2_col = in.readShort();
        field_3_xf = in.readShort();
    }

    public int getRow() {
        return field_1_row;
    }

    public void setRow(int row) {
        field_1_row = row;
    }

    public short getColumn() {
        return field_2_col;
    }

    public void setColumn(short col) {
        field_2_col = col;
    }

    public short getXFIndex() {
        return field_3_xf;
    }

    public void setXFIndex(short xf) {
        field_3_xf = xf;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {

        String sb = "[BLANK]\n" +
                "    row= " + String.valueOf(HexDump.shortToHex(getRow())) + "\n" +
                "    col= " + String.valueOf(HexDump.shortToHex(getColumn())) + "\n" +
                "    xf = " + String.valueOf(HexDump.shortToHex(getXFIndex())) + "\n" +
                "[/BLANK]\n";
        return sb;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getRow());
        out.writeShort(getColumn());
        out.writeShort(getXFIndex());
    }

    protected int getDataSize() {
        return 6;
    }

    public Object clone() {
        BlankRecord rec = new BlankRecord();
        rec.field_1_row = field_1_row;
        rec.field_2_col = field_2_col;
        rec.field_3_xf = field_3_xf;
        return rec;
    }
}
