package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import java.util.Arrays;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class HeaderFooterRecord extends StandardRecord {

    public final static short sid = 0x089C;
    private static final byte[] BLANK_GUID = new byte[16];
    private final byte[] _rawData;

    public HeaderFooterRecord(byte[] data) {
        _rawData = data;
    }

    public HeaderFooterRecord(RecordInputStream in) {
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
        System.arraycopy(_rawData, 12, guid, 0, Math.min(guid.length, _rawData.length - 12));
        return guid;
    }

    public boolean isCurrentSheet() {
        return Arrays.equals(getGuid(), BLANK_GUID);
    }

    public String toString() {

        String sb = "[" + "HEADERFOOTER" + "] (0x" +
                Integer.toHexString(sid).toUpperCase() + ")\n" +
                "  rawData=" + HexDump.toHex(_rawData) + "\n" +
                "[/" + "HEADERFOOTER" + "]\n";
        return sb;
    }

    public Object clone() {
        return cloneViaReserialise();
    }

}
