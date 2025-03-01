package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UserSViewEnd extends StandardRecord {

    public final static short sid = 0x01AB;
    private final byte[] _rawData;

    public UserSViewEnd(byte[] data) {
        _rawData = data;
    }

    public UserSViewEnd(RecordInputStream in) {
        _rawData = in.readRemainder();
    }

    public void serialize(LittleEndianOutput out) {
        out.write(_rawData);
    }

    protected int getDataSize() {
        return _rawData.length;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {

        String sb = "[" + "USERSVIEWEND" + "] (0x" +
                Integer.toHexString(sid).toUpperCase() + ")\n" +
                "  rawData=" + HexDump.toHex(_rawData) + "\n" +
                "[/" + "USERSVIEWEND" + "]\n";
        return sb;
    }

    public Object clone() {
        return cloneViaReserialise();
    }

}
