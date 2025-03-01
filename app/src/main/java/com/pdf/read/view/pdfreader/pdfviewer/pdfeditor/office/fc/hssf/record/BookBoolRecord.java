package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BookBoolRecord
        extends StandardRecord {
    public final static short sid = 0xDA;
    private short field_1_save_link_values;

    public BookBoolRecord() {
    }

    public BookBoolRecord(RecordInputStream in) {
        field_1_save_link_values = in.readShort();
    }

    public short getSaveLinkValues() {
        return field_1_save_link_values;
    }

    public void setSaveLinkValues(short flag) {
        field_1_save_link_values = flag;
    }

    public String toString() {

        String buffer = "[BOOKBOOL]\n" +
                "    .savelinkvalues  = " +
                Integer.toHexString(getSaveLinkValues()) + "\n" +
                "[/BOOKBOOL]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_save_link_values);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
