package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DefaultRowHeightRecord
        extends StandardRecord {
    public final static short sid = 0x225;
    public static final short DEFAULT_ROW_HEIGHT = 0xFF;
    private short field_1_option_flags;
    private short field_2_row_height;

    public DefaultRowHeightRecord() {
        field_1_option_flags = 0x0000;
        field_2_row_height = DEFAULT_ROW_HEIGHT;
    }

    public DefaultRowHeightRecord(RecordInputStream in) {
        field_1_option_flags = in.readShort();
        field_2_row_height = in.readShort();
    }

    public short getOptionFlags() {
        return field_1_option_flags;
    }

    public void setOptionFlags(short flags) {
        field_1_option_flags = flags;
    }

    public short getRowHeight() {
        return field_2_row_height;
    }

    public void setRowHeight(short height) {
        field_2_row_height = height;
    }

    public String toString() {

        String buffer = "[DEFAULTROWHEIGHT]\n" +
                "    .optionflags    = " +
                Integer.toHexString(getOptionFlags()) + "\n" +
                "    .rowheight      = " +
                Integer.toHexString(getRowHeight()) + "\n" +
                "[/DEFAULTROWHEIGHT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getOptionFlags());
        out.writeShort(getRowHeight());
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DefaultRowHeightRecord rec = new DefaultRowHeightRecord();
        rec.field_1_option_flags = field_1_option_flags;
        rec.field_2_row_height = field_2_row_height;
        return rec;
    }
}
