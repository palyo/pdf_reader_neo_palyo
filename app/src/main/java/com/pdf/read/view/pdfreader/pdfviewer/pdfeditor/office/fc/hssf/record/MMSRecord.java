package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class MMSRecord
        extends StandardRecord {
    public final static short sid = 0xC1;
    private byte field_1_addMenuCount;
    private byte field_2_delMenuCount;

    public MMSRecord() {
    }

    public MMSRecord(RecordInputStream in) {
        if (in.remaining() == 0) {
            return;
        }

        field_1_addMenuCount = in.readByte();
        field_2_delMenuCount = in.readByte();
    }

    public byte getAddMenuCount() {
        return field_1_addMenuCount;
    }

    public void setAddMenuCount(byte am) {
        field_1_addMenuCount = am;
    }

    public byte getDelMenuCount() {
        return field_2_delMenuCount;
    }

    public void setDelMenuCount(byte dm) {
        field_2_delMenuCount = dm;
    }

    public String toString() {

        String buffer = "[MMS]\n" +
                "    .addMenu        = " +
                Integer.toHexString(getAddMenuCount()) + "\n" +
                "    .delMenu        = " +
                Integer.toHexString(getDelMenuCount()) + "\n" +
                "[/MMS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(getAddMenuCount());
        out.writeByte(getDelMenuCount());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
