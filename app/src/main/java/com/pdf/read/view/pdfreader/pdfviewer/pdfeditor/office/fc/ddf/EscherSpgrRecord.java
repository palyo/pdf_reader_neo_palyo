package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.RecordFormatException;

public class EscherSpgrRecord
        extends EscherRecord {
    public static final short RECORD_ID = (short) 0xF009;
    public static final String RECORD_DESCRIPTION = "MsofbtSpgr";

    private int field_1_rectX1;
    private int field_2_rectY1;
    private int field_3_rectX2;
    private int field_4_rectY2;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;
        field_1_rectX1 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_2_rectY1 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_3_rectX2 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_4_rectY2 = LittleEndian.getInt(data, pos + size);
        size += 4;
        bytesRemaining -= size;
        if (bytesRemaining != 0) throw new RecordFormatException("Expected no remaining bytes but got " + bytesRemaining);

        return 8 + size + bytesRemaining;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = 16;
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        LittleEndian.putInt(data, offset + 8, field_1_rectX1);
        LittleEndian.putInt(data, offset + 12, field_2_rectY1);
        LittleEndian.putInt(data, offset + 16, field_3_rectX2);
        LittleEndian.putInt(data, offset + 20, field_4_rectY2);

        listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), offset + getRecordSize(), this);
        return 8 + 16;
    }

    public int getRecordSize() {
        return 8 + 16;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "Spgr";
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' +
                "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + '\n' +
                "  Options: 0x" + HexDump.toHex(getOptions()) + '\n' +
                "  RectX: " + field_1_rectX1 + '\n' +
                "  RectY: " + field_2_rectY1 + '\n' +
                "  RectWidth: " + field_3_rectX2 + '\n' +
                "  RectHeight: " + field_4_rectY2 + '\n';
    }

    public int getRectX1() {
        return field_1_rectX1;
    }

    public void setRectX1(int x1) {
        this.field_1_rectX1 = x1;
    }

    public int getRectY1() {
        return field_2_rectY1;
    }

    public void setRectY1(int y1) {
        this.field_2_rectY1 = y1;
    }

    public int getRectX2() {
        return field_3_rectX2;
    }

    public void setRectX2(int x2) {
        this.field_3_rectX2 = x2;
    }

    public int getRectY2() {
        return field_4_rectY2;
    }

    public void setRectY2(int rectY2) {
        this.field_4_rectY2 = rectY2;
    }

    public void dispose() {

    }
}
