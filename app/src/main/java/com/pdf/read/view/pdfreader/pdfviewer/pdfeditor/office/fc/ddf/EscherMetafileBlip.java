package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Dimension;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public final class EscherMetafileBlip extends EscherBlipRecord {
    public static final short RECORD_ID_EMF = (short) 0xF018 + 2;
    public static final short RECORD_ID_WMF = (short) 0xF018 + 3;
    public static final short RECORD_ID_PICT = (short) 0xF018 + 4;
    public static final short SIGNATURE_EMF = 0x3D40;
    public static final short SIGNATURE_WMF = 0x2160;
    public static final short SIGNATURE_PICT = 0x5420;
    private static final POILogger log = POILogFactory.getLogger(EscherMetafileBlip.class);
    private static final int HEADER_SIZE = 8;
    private byte[] field_1_UID;
    private byte[] field_2_UID;
    private int field_2_cb;
    private int field_3_rcBounds_x1;
    private int field_3_rcBounds_y1;
    private int field_3_rcBounds_x2;
    private int field_3_rcBounds_y2;
    private int field_4_ptSize_w;
    private int field_4_ptSize_h;
    private int field_5_cbSave;
    private byte field_6_fCompression;
    private byte field_7_fFilter;

    private byte[] raw_pictureData;
    private byte[] remainingData;

    private static byte[] inflatePictureData(byte[] data) {
        try {
            InflaterInputStream in = new InflaterInputStream(
                    new ByteArrayInputStream(data));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int readBytes;
            while ((readBytes = in.read(buf)) > 0) {
                out.write(buf, 0, readBytes);
            }
            return out.toByteArray();
        } catch (IOException e) {
            log.log(POILogger.WARN, "Possibly corrupt compression or non-compressed data", e);
            return data;
        }
    }

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + HEADER_SIZE;
        field_1_UID = new byte[16];
        System.arraycopy(data, pos, field_1_UID, 0, 16);
        pos += 16;

        if ((getOptions() ^ getSignature()) == 0x10) {
            field_2_UID = new byte[16];
            System.arraycopy(data, pos, field_2_UID, 0, 16);
            pos += 16;
        }

        field_2_cb = LittleEndian.getInt(data, pos);
        pos += 4;
        field_3_rcBounds_x1 = LittleEndian.getInt(data, pos);
        pos += 4;
        field_3_rcBounds_y1 = LittleEndian.getInt(data, pos);
        pos += 4;
        field_3_rcBounds_x2 = LittleEndian.getInt(data, pos);
        pos += 4;
        field_3_rcBounds_y2 = LittleEndian.getInt(data, pos);
        pos += 4;
        field_4_ptSize_w = LittleEndian.getInt(data, pos);
        pos += 4;
        field_4_ptSize_h = LittleEndian.getInt(data, pos);
        pos += 4;
        field_5_cbSave = LittleEndian.getInt(data, pos);
        pos += 4;
        field_6_fCompression = data[pos];
        pos++;
        field_7_fFilter = data[pos];
        pos++;

        raw_pictureData = new byte[field_5_cbSave];
        System.arraycopy(data, pos, raw_pictureData, 0, field_5_cbSave);
        pos += field_5_cbSave;

        if (field_6_fCompression == 0) {
            field_pictureData = inflatePictureData(raw_pictureData);
        } else {
            field_pictureData = raw_pictureData;
        }

        int remaining = bytesAfterHeader - pos + offset + HEADER_SIZE;
        if (remaining > 0) {
            remainingData = new byte[remaining];
            System.arraycopy(data, pos, remainingData, 0, remaining);
        }
        return bytesAfterHeader + HEADER_SIZE;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);

        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        pos += 2;
        LittleEndian.putShort(data, pos, getRecordId());
        pos += 2;
        LittleEndian.putInt(data, pos, getRecordSize() - HEADER_SIZE);
        pos += 4;

        System.arraycopy(field_1_UID, 0, data, pos, field_1_UID.length);
        pos += field_1_UID.length;
        if ((getOptions() ^ getSignature()) == 0x10) {
            System.arraycopy(field_2_UID, 0, data, pos, field_2_UID.length);
            pos += field_2_UID.length;
        }
        LittleEndian.putInt(data, pos, field_2_cb);
        pos += 4;
        LittleEndian.putInt(data, pos, field_3_rcBounds_x1);
        pos += 4;
        LittleEndian.putInt(data, pos, field_3_rcBounds_y1);
        pos += 4;
        LittleEndian.putInt(data, pos, field_3_rcBounds_x2);
        pos += 4;
        LittleEndian.putInt(data, pos, field_3_rcBounds_y2);
        pos += 4;
        LittleEndian.putInt(data, pos, field_4_ptSize_w);
        pos += 4;
        LittleEndian.putInt(data, pos, field_4_ptSize_h);
        pos += 4;
        LittleEndian.putInt(data, pos, field_5_cbSave);
        pos += 4;
        data[pos] = field_6_fCompression;
        pos++;
        data[pos] = field_7_fFilter;
        pos++;

        System.arraycopy(raw_pictureData, 0, data, pos, raw_pictureData.length);
        pos += raw_pictureData.length;
        if (remainingData != null) {
            System.arraycopy(remainingData, 0, data, pos, remainingData.length);
            pos += remainingData.length;
        }

        listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    public int getRecordSize() {
        int size = 8 + 50 + raw_pictureData.length;
        if (remainingData != null) size += remainingData.length;
        if ((getOptions() ^ getSignature()) == 0x10) {
            size += field_2_UID.length;
        }
        return size;
    }

    public byte[] getUID() {
        return field_1_UID;
    }

    public void setUID(byte[] uid) {
        field_1_UID = uid;
    }

    public byte[] getPrimaryUID() {
        return field_2_UID;
    }

    public void setPrimaryUID(byte[] primaryUID) {
        field_2_UID = primaryUID;
    }

    public int getUncompressedSize() {
        return field_2_cb;
    }

    public void setUncompressedSize(int uncompressedSize) {
        field_2_cb = uncompressedSize;
    }

    public Rectangle getBounds() {
        return new Rectangle(field_3_rcBounds_x1,
                field_3_rcBounds_y1,
                field_3_rcBounds_x2 - field_3_rcBounds_x1,
                field_3_rcBounds_y2 - field_3_rcBounds_y1);
    }

    public void setBounds(Rectangle bounds) {
        field_3_rcBounds_x1 = bounds.x;
        field_3_rcBounds_y1 = bounds.y;
        field_3_rcBounds_x2 = bounds.x + bounds.width;
        field_3_rcBounds_y2 = bounds.y + bounds.height;
    }

    public Dimension getSizeEMU() {
        return new Dimension(field_4_ptSize_w, field_4_ptSize_h);
    }

    public void setSizeEMU(Dimension sizeEMU) {
        field_4_ptSize_w = sizeEMU.width;
        field_4_ptSize_h = sizeEMU.height;
    }

    public int getCompressedSize() {
        return field_5_cbSave;
    }

    public void setCompressedSize(int compressedSize) {
        field_5_cbSave = compressedSize;
    }

    public boolean isCompressed() {
        return (field_6_fCompression == 0);
    }

    public void setCompressed(boolean compressed) {
        field_6_fCompression = compressed ? 0 : (byte) 0xFE;
    }

    public byte[] getRemainingData() {
        return remainingData;
    }

    public String toString() {
        String extraData = "";
        return getClass().getName() + ":" + '\n' +
                "  RecordId: 0x" + HexDump.toHex(getRecordId()) + '\n' +
                "  Options: 0x" + HexDump.toHex(getOptions()) + '\n' +
                "  UID: 0x" + HexDump.toHex(field_1_UID) + '\n' +
                (field_2_UID == null ? "" : ("  UID2: 0x" + HexDump.toHex(field_2_UID) + '\n')) +
                "  Uncompressed Size: " + HexDump.toHex(field_2_cb) + '\n' +
                "  Bounds: " + getBounds() + '\n' +
                "  Size in EMU: " + getSizeEMU() + '\n' +
                "  Compressed Size: " + HexDump.toHex(field_5_cbSave) + '\n' +
                "  Compression: " + HexDump.toHex(field_6_fCompression) + '\n' +
                "  Filter: " + HexDump.toHex(field_7_fFilter) + '\n' +
                "  Extra Data:" + '\n' + extraData +
                (remainingData == null ? null : ("\n" +
                        " Remaining Data: " + HexDump.toHex(remainingData, 32)));
    }

    public short getSignature() {
        switch (getRecordId()) {
            case RECORD_ID_EMF:
                return SIGNATURE_EMF;
            case RECORD_ID_WMF:
                return SIGNATURE_WMF;
            case RECORD_ID_PICT:
                return SIGNATURE_PICT;
        }
        log.log(POILogger.WARN, "Unknown metafile: " + getRecordId());
        return 0;
    }
}
