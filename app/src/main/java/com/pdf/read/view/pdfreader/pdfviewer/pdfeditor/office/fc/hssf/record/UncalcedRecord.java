package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UncalcedRecord extends StandardRecord {
    public final static short sid = 0x005E;

    private final short _reserved;

    public UncalcedRecord() {
        _reserved = 0;
    }

    public UncalcedRecord(RecordInputStream in) {
        _reserved = in.readShort();
    }

    public static int getStaticRecordSize() {
        return 6;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        String buffer = "[UNCALCED]\n" +
                "    _reserved: " + _reserved + '\n' +
                "[/UNCALCED]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_reserved);
    }

    protected int getDataSize() {
        return 2;
    }
}
