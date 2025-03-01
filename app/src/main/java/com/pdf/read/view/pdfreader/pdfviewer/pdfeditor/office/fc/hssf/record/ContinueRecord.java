package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ContinueRecord extends StandardRecord {
    public final static short sid = 0x003C;
    private byte[] _data;

    public ContinueRecord(byte[] data) {
        _data = data;
    }

    public ContinueRecord(RecordInputStream in) {
        _data = in.readRemainder();
    }

    protected int getDataSize() {
        if (_data != null) {
            return _data.length;
        }

        return 0;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(_data);
    }

    public byte[] getData() {
        return _data;
    }

    public void resetData() {
        _data = null;
    }

    public String toString() {

        String buffer = "[CONTINUE RECORD]\n" +
                "    .data = " + HexDump.toHex(_data) + "\n" +
                "[/CONTINUE RECORD]\n";
        return buffer;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return new ContinueRecord(_data);
    }
}
