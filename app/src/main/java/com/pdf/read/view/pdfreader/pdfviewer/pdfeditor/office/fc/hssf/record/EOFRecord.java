package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class EOFRecord extends StandardRecord {
    public final static short sid = 0x0A;
    public static final int ENCODED_SIZE = 4;

    public static final EOFRecord instance = new EOFRecord();

    private EOFRecord() {

    }

    public EOFRecord(RecordInputStream in) {
    }

    public String toString() {

        String buffer = "[EOF]\n" +
                "[/EOF]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return ENCODED_SIZE - 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return instance;
    }
}
