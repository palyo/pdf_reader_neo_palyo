package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FnGroupCountRecord
        extends StandardRecord {
    public final static short sid = 0x9c;

    public final static short COUNT = 14;
    private short field_1_count;

    public FnGroupCountRecord() {
    }

    public FnGroupCountRecord(RecordInputStream in) {
        field_1_count = in.readShort();
    }

    public short getCount() {
        return field_1_count;
    }

    public void setCount(short count) {
        field_1_count = count;
    }

    public String toString() {

        String buffer = "[FNGROUPCOUNT]\n" +
                "    .count            = " + getCount() +
                "\n" +
                "[/FNGROUPCOUNT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCount());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
