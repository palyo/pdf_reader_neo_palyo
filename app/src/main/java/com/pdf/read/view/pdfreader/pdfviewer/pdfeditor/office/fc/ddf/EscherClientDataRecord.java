package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import java.io.ByteArrayOutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class EscherClientDataRecord
        extends EscherRecord {
    public static final short RECORD_ID = (short) 0xF011;
    public static final String RECORD_DESCRIPTION = "MsofbtClientData";

    private byte[] remainingData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos, remainingData, 0, bytesRemaining);
        return 8 + bytesRemaining;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);

        if (remainingData == null) remainingData = new byte[0];
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, remainingData.length);
        System.arraycopy(remainingData, 0, data, offset + 8, remainingData.length);
        int pos = offset + 8 + remainingData.length;

        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return 8 + (remainingData == null ? 0 : remainingData.length);
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientData";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");

        String extraData;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            HexDump.dump(this.remainingData, 0, b, 0);
            extraData = b.toString();
        } catch (Exception e) {
            extraData = "error\n";
        }
        return getClass().getName() + ":" + nl +
                "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl +
                "  Options: 0x" + HexDump.toHex(getOptions()) + nl +
                "  Extra Data:" + nl +
                extraData;

    }

    public byte[] getRemainingData() {
        return remainingData;
    }

    public void setRemainingData(byte[] remainingData) {
        this.remainingData = remainingData;
    }

    public void dispose() {
        remainingData = null;
    }
}
