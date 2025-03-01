package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class AutoFilterInfoRecord
        extends StandardRecord {
    public final static short sid = 0x9D;
    private short _cEntries;

    public AutoFilterInfoRecord() {
    }

    public AutoFilterInfoRecord(RecordInputStream in) {
        _cEntries = in.readShort();
    }

    public short getNumEntries() {
        return _cEntries;
    }

    public void setNumEntries(short num) {
        _cEntries = num;
    }

    public String toString() {

        String buffer = "[AUTOFILTERINFO]\n" +
                "    .numEntries          = " +
                _cEntries + "\n" +
                "[/AUTOFILTERINFO]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_cEntries);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    @Override
    public Object clone() {
        return cloneViaReserialise();
    }

}
