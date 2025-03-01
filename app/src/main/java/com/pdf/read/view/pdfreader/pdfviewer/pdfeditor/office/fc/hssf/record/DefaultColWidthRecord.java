package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DefaultColWidthRecord extends StandardRecord {
    public final static short sid = 0x0055;
    public final static int DEFAULT_COLUMN_WIDTH = 0x0008;
    private int field_1_col_width;

    public DefaultColWidthRecord() {
        field_1_col_width = DEFAULT_COLUMN_WIDTH;
    }

    public DefaultColWidthRecord(RecordInputStream in) {
        field_1_col_width = in.readUShort();
    }

    public int getColWidth() {
        return field_1_col_width;
    }

    public void setColWidth(int width) {
        field_1_col_width = width;
    }

    public String toString() {

        String buffer = "[DEFAULTCOLWIDTH]\n" +
                "    .colwidth      = " +
                Integer.toHexString(getColWidth()) + "\n" +
                "[/DEFAULTCOLWIDTH]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getColWidth());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DefaultColWidthRecord rec = new DefaultColWidthRecord();
        rec.field_1_col_width = field_1_col_width;
        return rec;
    }
}
