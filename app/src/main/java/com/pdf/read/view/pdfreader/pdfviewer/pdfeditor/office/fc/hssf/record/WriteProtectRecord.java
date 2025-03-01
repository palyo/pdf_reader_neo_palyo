package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class WriteProtectRecord extends StandardRecord {
    public final static short sid = 0x86;

    public WriteProtectRecord() {
    }

    public WriteProtectRecord(RecordInputStream in) {
    }

    public String toString() {

        String buffer = "[WRITEPROTECT]\n" +
                "[/WRITEPROTECT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid() {
        return sid;
    }
}
