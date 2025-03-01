package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DateWindow1904Record
        extends StandardRecord {
    public final static short sid = 0x22;
    private short field_1_window;

    public DateWindow1904Record() {
    }

    public DateWindow1904Record(RecordInputStream in) {
        field_1_window = in.readShort();
    }

    public short getWindowing() {
        return field_1_window;
    }

    public void setWindowing(short window) {
        field_1_window = window;
    }

    public String toString() {

        String buffer = "[1904]\n" +
                "    .is1904          = " +
                Integer.toHexString(getWindowing()) + "\n" +
                "[/1904]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getWindowing());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
