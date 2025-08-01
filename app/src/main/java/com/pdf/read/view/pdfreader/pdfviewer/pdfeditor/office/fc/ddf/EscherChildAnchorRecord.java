package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class EscherChildAnchorRecord
        extends EscherRecord {
    public static final short RECORD_ID = (short) 0xF00F;
    public static final String RECORD_DESCRIPTION = "MsofbtChildAnchor";

    private int field_1_dx1;
    private int field_2_dy1;
    private int field_3_dx2;
    private int field_4_dy2;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;
        field_1_dx1 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_2_dy1 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_3_dx2 = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_4_dy2 = LittleEndian.getInt(data, pos + size);
        size += 4;
        return 8 + size;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        pos += 2;
        LittleEndian.putShort(data, pos, getRecordId());
        pos += 2;
        LittleEndian.putInt(data, pos, getRecordSize() - 8);
        pos += 4;
        LittleEndian.putInt(data, pos, field_1_dx1);
        pos += 4;
        LittleEndian.putInt(data, pos, field_2_dy1);
        pos += 4;
        LittleEndian.putInt(data, pos, field_3_dx2);
        pos += 4;
        LittleEndian.putInt(data, pos, field_4_dy2);
        pos += 4;

        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return 8 + 4 * 4;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ChildAnchor";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");

        return getClass().getName() + ":" + nl +
                "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl +
                "  Options: 0x" + HexDump.toHex(getOptions()) + nl +
                "  X1: " + field_1_dx1 + nl +
                "  Y1: " + field_2_dy1 + nl +
                "  X2: " + field_3_dx2 + nl +
                "  Y2: " + field_4_dy2 + nl;

    }

    public int getDx1() {
        return field_1_dx1;
    }

    public void setDx1(int field_1_dx1) {
        this.field_1_dx1 = field_1_dx1;
    }

    public int getDy1() {
        return field_2_dy1;
    }

    public void setDy1(int field_2_dy1) {
        this.field_2_dy1 = field_2_dy1;
    }

    public int getDx2() {
        return field_3_dx2;
    }

    public void setDx2(int field_3_dx2) {
        this.field_3_dx2 = field_3_dx2;
    }

    public int getDy2() {
        return field_4_dy2;
    }

    public void setDy2(int field_4_dy2) {
        this.field_4_dy2 = field_4_dy2;
    }

    public void dispose() {

    }
}
