package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class RecalcIdRecord extends StandardRecord {
    public final static short sid = 0x01C1;
    private final int _reserved0;

    private int _engineId;

    public RecalcIdRecord() {
        _reserved0 = 0;
        _engineId = 0;
    }

    public RecalcIdRecord(RecordInputStream in) {
        in.readUShort();
        _reserved0 = in.readUShort();
        _engineId = in.readInt();
    }

    public boolean isNeeded() {
        return true;
    }

    public int getEngineId() {
        return _engineId;
    }

    public void setEngineId(int val) {
        _engineId = val;
    }

    public String toString() {

        String buffer = "[RECALCID]\n" +
                "    .reserved = " + String.valueOf(HexDump.shortToHex(_reserved0)) + "\n" +
                "    .engineId = " + String.valueOf(HexDump.intToHex(_engineId)) + "\n" +
                "[/RECALCID]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(sid);
        out.writeShort(_reserved0);
        out.writeInt(_engineId);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }
}
