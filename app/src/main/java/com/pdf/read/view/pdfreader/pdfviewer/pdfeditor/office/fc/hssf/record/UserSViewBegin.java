package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UserSViewBegin extends StandardRecord {

    public final static short sid = 0x01AA;
    private final byte[] _rawData;

    public UserSViewBegin(byte[] data) {
        _rawData = data;
    }

    public UserSViewBegin(RecordInputStream in) {
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

    public byte[] getGuid() {
        byte[] guid = new byte[16];
        System.arraycopy(_rawData, 0, guid, 0, guid.length);
        return guid;
    }

    public String toString() {

        String sb = "[" + "USERSVIEWBEGIN" + "] (0x" +
                Integer.toHexString(sid).toUpperCase() + ")\n" +
                "  rawData=" + HexDump.toHex(_rawData) + "\n" +
                "[/" + "USERSVIEWBEGIN" + "]\n";
        return sb;
    }

    public Object clone() {
        return cloneViaReserialise();
    }

}
