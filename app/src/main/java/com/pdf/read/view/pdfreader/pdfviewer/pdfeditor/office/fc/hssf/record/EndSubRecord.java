package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class EndSubRecord extends SubRecord {
    public final static short sid = 0x0000;
    private static final int ENCODED_SIZE = 0;

    public EndSubRecord() {

    }

    public EndSubRecord(LittleEndianInput in, int size) {
        if ((size & 0xFF) != ENCODED_SIZE) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
    }

    @Override
    public boolean isTerminating() {
        return true;
    }

    public String toString() {

        String buffer = "[ftEnd]\n" +
                "[/ftEnd]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(sid);
        out.writeShort(ENCODED_SIZE);
    }

    protected int getDataSize() {
        return ENCODED_SIZE;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        EndSubRecord rec = new EndSubRecord();

        return rec;
    }
}
