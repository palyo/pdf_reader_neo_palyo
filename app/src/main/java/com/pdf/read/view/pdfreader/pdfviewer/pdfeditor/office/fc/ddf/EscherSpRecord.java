package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class EscherSpRecord
        extends EscherRecord {
    public static final short RECORD_ID = (short) 0xF00A;
    public static final String RECORD_DESCRIPTION = "MsofbtSp";

    public static final int FLAG_GROUP = 0x0001;
    public static final int FLAG_CHILD = 0x0002;
    public static final int FLAG_PATRIARCH = 0x0004;
    public static final int FLAG_DELETED = 0x0008;
    public static final int FLAG_OLESHAPE = 0x0010;
    public static final int FLAG_HAVEMASTER = 0x0020;
    public static final int FLAG_FLIPHORIZ = 0x0040;
    public static final int FLAG_FLIPVERT = 0x0080;
    public static final int FLAG_CONNECTOR = 0x0100;
    public static final int FLAG_HAVEANCHOR = 0x0200;
    public static final int FLAG_BACKGROUND = 0x0400;
    public static final int FLAG_HASSHAPETYPE = 0x0800;

    private int field_1_shapeId;
    private int field_2_flags;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;
        field_1_shapeId = LittleEndian.getInt(data, pos + size);
        size += 4;
        field_2_flags = LittleEndian.getInt(data, pos + size);
        size += 4;

        return getRecordSize();
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = 8;
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        LittleEndian.putInt(data, offset + 8, field_1_shapeId);
        LittleEndian.putInt(data, offset + 12, field_2_flags);

        listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
        return 8 + 8;
    }

    public int getRecordSize() {
        return 8 + 8;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "Sp";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");

        return getClass().getName() + ":" + nl +
                "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl +
                "  Options: 0x" + HexDump.toHex(getOptions()) + nl +
                "  ShapeId: " + field_1_shapeId + nl +
                "  Flags: " + decodeFlags(field_2_flags) + " (0x" + HexDump.toHex(field_2_flags) + ")" + nl;

    }

    private String decodeFlags(int flags) {
        StringBuffer result = new StringBuffer();
        result.append((flags & FLAG_GROUP) != 0 ? "|GROUP" : "");
        result.append((flags & FLAG_CHILD) != 0 ? "|CHILD" : "");
        result.append((flags & FLAG_PATRIARCH) != 0 ? "|PATRIARCH" : "");
        result.append((flags & FLAG_DELETED) != 0 ? "|DELETED" : "");
        result.append((flags & FLAG_OLESHAPE) != 0 ? "|OLESHAPE" : "");
        result.append((flags & FLAG_HAVEMASTER) != 0 ? "|HAVEMASTER" : "");
        result.append((flags & FLAG_FLIPHORIZ) != 0 ? "|FLIPHORIZ" : "");
        result.append((flags & FLAG_FLIPVERT) != 0 ? "|FLIPVERT" : "");
        result.append((flags & FLAG_CONNECTOR) != 0 ? "|CONNECTOR" : "");
        result.append((flags & FLAG_HAVEANCHOR) != 0 ? "|HAVEANCHOR" : "");
        result.append((flags & FLAG_BACKGROUND) != 0 ? "|BACKGROUND" : "");
        result.append((flags & FLAG_HASSHAPETYPE) != 0 ? "|HASSHAPETYPE" : "");

        if (result.length() > 0) {
            result.deleteCharAt(0);
        }
        return result.toString();
    }

    public int getShapeId() {
        return field_1_shapeId;
    }

    public void setShapeId(int field_1_shapeId) {
        this.field_1_shapeId = field_1_shapeId;
    }

    public int getFlags() {
        return field_2_flags;
    }

    public void setFlags(int field_2_flags) {
        this.field_2_flags = field_2_flags;
    }

    public void dispose() {
    }
}
