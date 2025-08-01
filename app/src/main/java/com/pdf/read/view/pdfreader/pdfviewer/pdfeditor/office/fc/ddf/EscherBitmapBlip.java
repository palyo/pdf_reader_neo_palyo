package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import java.io.ByteArrayOutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class EscherBitmapBlip extends EscherBlipRecord {
    public static final short RECORD_ID_JPEG = (short) 0xF018 + 5;
    public static final short RECORD_ID_PNG = (short) 0xF018 + 6;
    public static final short RECORD_ID_DIB = (short) 0xF018 + 7;

    private static final int HEADER_SIZE = 8;

    private byte[] field_1_UID;
    private byte field_2_marker = (byte) 0xFF;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + HEADER_SIZE;

        field_1_UID = new byte[16];
        System.arraycopy(data, pos, field_1_UID, 0, 16);
        pos += 16;
        field_2_marker = data[pos];
        pos++;

        field_pictureData = new byte[bytesAfterHeader - 17];
        System.arraycopy(data, pos, field_pictureData, 0, field_pictureData.length);

        return bytesAfterHeader + HEADER_SIZE;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);

        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, getRecordSize() - HEADER_SIZE);
        int pos = offset + HEADER_SIZE;

        System.arraycopy(field_1_UID, 0, data, pos, 16);
        data[pos + 16] = field_2_marker;
        System.arraycopy(field_pictureData, 0, data, pos + 17, field_pictureData.length);

        listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
        return HEADER_SIZE + 16 + 1 + field_pictureData.length;
    }

    public int getRecordSize() {
        return 8 + 16 + 1 + field_pictureData.length;
    }

    public byte[] getUID() {
        return field_1_UID;
    }

    public void setUID(byte[] field_1_UID) {
        this.field_1_UID = field_1_UID;
    }

    public byte getMarker() {
        return field_2_marker;
    }

    public void setMarker(byte field_2_marker) {
        this.field_2_marker = field_2_marker;
    }

    public String toString() {
        String nl = System.getProperty("line.separator");

        String extraData;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            HexDump.dump(this.field_pictureData, 0, b, 0);
            extraData = b.toString();
        } catch (Exception e) {
            extraData = e.toString();
        }
        return getClass().getName() + ":" + nl +
                "  RecordId: 0x" + HexDump.toHex(getRecordId()) + nl +
                "  Options: 0x" + HexDump.toHex(getOptions()) + nl +
                "  UID: 0x" + HexDump.toHex(field_1_UID) + nl +
                "  Marker: 0x" + HexDump.toHex(field_2_marker) + nl +
                "  Extra Data:" + nl + extraData;
    }
}
