package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class HideObjRecord
        extends StandardRecord {
    public final static short sid = 0x8d;
    public final static short HIDE_ALL = 2;
    public final static short SHOW_PLACEHOLDERS = 1;
    public final static short SHOW_ALL = 0;
    private short field_1_hide_obj;

    public HideObjRecord() {
    }

    public HideObjRecord(RecordInputStream in) {
        field_1_hide_obj = in.readShort();
    }

    public short getHideObj() {
        return field_1_hide_obj;
    }

    public void setHideObj(short hide) {
        field_1_hide_obj = hide;
    }

    public String toString() {

        String buffer = "[HIDEOBJ]\n" +
                "    .hideobj         = " +
                Integer.toHexString(getHideObj()) + "\n" +
                "[/HIDEOBJ]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getHideObj());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
