package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DimensionsRecord
        extends StandardRecord {
    public final static short sid = 0x200;
    private int field_1_first_row;
    private int field_2_last_row;
    private short field_3_first_col;
    private short field_4_last_col;
    private short field_5_zero;

    public DimensionsRecord() {
    }

    public DimensionsRecord(RecordInputStream in) {
        field_1_first_row = in.readInt();
        field_2_last_row = in.readInt();
        field_3_first_col = in.readShort();
        field_4_last_col = in.readShort();
        field_5_zero = in.readShort();
    }

    public int getFirstRow() {
        return field_1_first_row;
    }

    public void setFirstRow(int row) {
        field_1_first_row = row;
    }

    public int getLastRow() {
        return field_2_last_row;
    }

    public void setLastRow(int row) {
        field_2_last_row = row;
    }

    public short getFirstCol() {
        return field_3_first_col;
    }

    public void setFirstCol(short col) {
        field_3_first_col = col;
    }

    public short getLastCol() {
        return field_4_last_col;
    }

    public void setLastCol(short col) {
        field_4_last_col = col;
    }

    public String toString() {

        String buffer = "[DIMENSIONS]\n" +
                "    .firstrow       = " +
                Integer.toHexString(getFirstRow()) + "\n" +
                "    .lastrow        = " +
                Integer.toHexString(getLastRow()) + "\n" +
                "    .firstcol       = " +
                Integer.toHexString(getFirstCol()) + "\n" +
                "    .lastcol        = " +
                Integer.toHexString(getLastCol()) + "\n" +
                "    .zero           = " +
                Integer.toHexString(field_5_zero) + "\n" +
                "[/DIMENSIONS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(getFirstRow());
        out.writeInt(getLastRow());
        out.writeShort(getFirstCol());
        out.writeShort(getLastCol());
        out.writeShort((short) 0);
    }

    protected int getDataSize() {
        return 14;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DimensionsRecord rec = new DimensionsRecord();
        rec.field_1_first_row = field_1_first_row;
        rec.field_2_last_row = field_2_last_row;
        rec.field_3_first_col = field_3_first_col;
        rec.field_4_last_col = field_4_last_col;
        rec.field_5_zero = field_5_zero;
        return rec;
    }
}
