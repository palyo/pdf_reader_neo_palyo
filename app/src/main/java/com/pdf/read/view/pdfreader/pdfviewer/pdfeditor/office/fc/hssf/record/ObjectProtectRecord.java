package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ObjectProtectRecord
        extends StandardRecord {
    public final static short sid = 0x63;
    private short field_1_protect;

    public ObjectProtectRecord() {
    }

    public ObjectProtectRecord(RecordInputStream in) {
        field_1_protect = in.readShort();
    }

    public boolean getProtect() {
        return (field_1_protect == 1);
    }

    public void setProtect(boolean protect) {
        if (protect) {
            field_1_protect = 1;
        } else {
            field_1_protect = 0;
        }
    }

    public String toString() {

        String buffer = "[SCENARIOPROTECT]\n" +
                "    .protect         = " + getProtect() +
                "\n" +
                "[/SCENARIOPROTECT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_protect);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        ObjectProtectRecord rec = new ObjectProtectRecord();
        rec.field_1_protect = field_1_protect;
        return rec;
    }
}
