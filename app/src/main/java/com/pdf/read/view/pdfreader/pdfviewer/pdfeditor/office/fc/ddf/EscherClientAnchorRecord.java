package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import java.io.ByteArrayOutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class EscherClientAnchorRecord
        extends EscherRecord {
    public static final short RECORD_ID = (short) 0xF010;
    public static final String RECORD_DESCRIPTION = "MsofbtClientAnchor";

    private short field_1_flag;
    private short field_2_col1;
    private short field_3_dx1;
    private short field_4_row1;
    private short field_5_dy1;
    private short field_6_col2;
    private short field_7_dx2;
    private short field_8_row2;
    private short field_9_dy2;
    private byte[] remainingData;
    private boolean shortRecord = false;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;

        if (bytesRemaining == 4) {

        } else {
            if (bytesRemaining == 16) {
                field_1_flag = LittleEndian.getShort(data, pos + size);
                size += 4;
                field_2_col1 = LittleEndian.getShort(data, pos + size);
                size += 4;
                field_3_dx1 = LittleEndian.getShort(data, pos + size);
                size += 4;
                field_4_row1 = LittleEndian.getShort(data, pos + size);
                size += 4;
                shortRecord = false;
            } else {
                field_1_flag = LittleEndian.getShort(data, pos + size);
                size += 2;
                field_2_col1 = LittleEndian.getShort(data, pos + size);
                size += 2;
                field_3_dx1 = LittleEndian.getShort(data, pos + size);
                size += 2;
                field_4_row1 = LittleEndian.getShort(data, pos + size);
                size += 2;

                if (bytesRemaining >= 18) {
                    field_5_dy1 = LittleEndian.getShort(data, pos + size);
                    size += 2;
                    field_6_col2 = LittleEndian.getShort(data, pos + size);
                    size += 2;
                    field_7_dx2 = LittleEndian.getShort(data, pos + size);
                    size += 2;
                    field_8_row2 = LittleEndian.getShort(data, pos + size);
                    size += 2;
                    field_9_dy2 = LittleEndian.getShort(data, pos + size);
                    size += 2;
                    shortRecord = false;
                } else {
                    shortRecord = true;
                }
            }
        }
        bytesRemaining -= size;
        remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos + size, remainingData, 0, bytesRemaining);
        return 8 + size + bytesRemaining;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);

        if (remainingData == null) remainingData = new byte[0];
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = remainingData.length + (shortRecord ? 8 : 18);
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        LittleEndian.putShort(data, offset + 8, field_1_flag);
        LittleEndian.putShort(data, offset + 10, field_2_col1);
        LittleEndian.putShort(data, offset + 12, field_3_dx1);
        LittleEndian.putShort(data, offset + 14, field_4_row1);
        if (!shortRecord) {
            LittleEndian.putShort(data, offset + 16, field_5_dy1);
            LittleEndian.putShort(data, offset + 18, field_6_col2);
            LittleEndian.putShort(data, offset + 20, field_7_dx2);
            LittleEndian.putShort(data, offset + 22, field_8_row2);
            LittleEndian.putShort(data, offset + 24, field_9_dy2);
        }
        System.arraycopy(remainingData, 0, data, offset + (shortRecord ? 16 : 26), remainingData.length);
        int pos = offset + 8 + (shortRecord ? 8 : 18) + remainingData.length;

        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return 8 + (shortRecord ? 8 : 18) + (remainingData == null ? 0 : remainingData.length);
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientAnchor";
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
                "  Flag: " + field_1_flag + nl +
                "  Col1: " + field_2_col1 + nl +
                "  DX1: " + field_3_dx1 + nl +
                "  Row1: " + field_4_row1 + nl +
                "  DY1: " + field_5_dy1 + nl +
                "  Col2: " + field_6_col2 + nl +
                "  DX2: " + field_7_dx2 + nl +
                "  Row2: " + field_8_row2 + nl +
                "  DY2: " + field_9_dy2 + nl +
                "  Extra Data:" + nl + extraData;

    }

    public short getFlag() {
        return field_1_flag;
    }

    public void setFlag(short field_1_flag) {
        this.field_1_flag = field_1_flag;
    }

    public short getCol1() {
        return field_2_col1;
    }

    public void setCol1(short field_2_col1) {
        this.field_2_col1 = field_2_col1;
    }

    public short getDx1() {
        return field_3_dx1;
    }

    public void setDx1(short field_3_dx1) {
        this.field_3_dx1 = field_3_dx1;
    }

    public short getRow1() {
        return field_4_row1;
    }

    public void setRow1(short field_4_row1) {
        this.field_4_row1 = field_4_row1;
    }

    public short getDy1() {
        return field_5_dy1;
    }

    public void setDy1(short field_5_dy1) {
        shortRecord = false;
        this.field_5_dy1 = field_5_dy1;
    }

    public short getCol2() {
        return field_6_col2;
    }

    public void setCol2(short field_6_col2) {
        shortRecord = false;
        this.field_6_col2 = field_6_col2;
    }

    public short getDx2() {
        return field_7_dx2;
    }

    public void setDx2(short field_7_dx2) {
        shortRecord = false;
        this.field_7_dx2 = field_7_dx2;
    }

    public short getRow2() {
        return field_8_row2;
    }

    public void setRow2(short field_8_row2) {
        shortRecord = false;
        this.field_8_row2 = field_8_row2;
    }

    public short getDy2() {
        return field_9_dy2;
    }

    public void setDy2(short field_9_dy2) {
        shortRecord = false;
        this.field_9_dy2 = field_9_dy2;
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
