package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.UnknownRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ObjectLinkRecord extends StandardRecord {
    public final static short sid = 0x1027;
    public final static short ANCHOR_ID_CHART_TITLE = 1;
    public final static short ANCHOR_ID_Y_AXIS = 2;
    public final static short ANCHOR_ID_X_AXIS = 3;
    public final static short ANCHOR_ID_SERIES_OR_POINT = 4;
    public final static short ANCHOR_ID_Z_AXIS = 7;
    private short field_1_anchorId;
    private short field_2_link1;
    private short field_3_link2;

    public ObjectLinkRecord() {

    }

    public ObjectLinkRecord(RecordInputStream in) {
        field_1_anchorId = in.readShort();
        field_2_link1 = in.readShort();
        field_3_link2 = in.readShort();

    }


    public ObjectLinkRecord(UnknownRecord unknownRecord) {
        if (unknownRecord.getSid() == ObjectLinkRecord.sid && unknownRecord.getData().length == getDataSize()) {
            byte[] data = unknownRecord.getData();
            field_1_anchorId = LittleEndian.getShort(data, 0);
            field_2_link1 = LittleEndian.getShort(data, 2);
            field_3_link2 = LittleEndian.getShort(data, 4);
        }
    }

    public String toString() {

        String buffer = "[OBJECTLINK]\n" +
                "    .anchorId             = " +
                "0x" + HexDump.toHex(getAnchorId()) +
                " (" + getAnchorId() + " )" +
                System.getProperty("line.separator") +
                "    .link1                = " +
                "0x" + HexDump.toHex(getLink1()) +
                " (" + getLink1() + " )" +
                System.getProperty("line.separator") +
                "    .link2                = " +
                "0x" + HexDump.toHex(getLink2()) +
                " (" + getLink2() + " )" +
                System.getProperty("line.separator") +
                "[/OBJECTLINK]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_anchorId);
        out.writeShort(field_2_link1);
        out.writeShort(field_3_link2);
    }

    protected int getDataSize() {
        return 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        ObjectLinkRecord rec = new ObjectLinkRecord();

        rec.field_1_anchorId = field_1_anchorId;
        rec.field_2_link1 = field_2_link1;
        rec.field_3_link2 = field_3_link2;
        return rec;
    }

    public short getAnchorId() {
        return field_1_anchorId;
    }

    public void setAnchorId(short field_1_anchorId) {
        this.field_1_anchorId = field_1_anchorId;
    }

    public short getLink1() {
        return field_2_link1;
    }

    public void setLink1(short field_2_link1) {
        this.field_2_link1 = field_2_link1;
    }

    public short getLink2() {
        return field_3_link2;
    }

    public void setLink2(short field_3_link2) {
        this.field_3_link2 = field_3_link2;
    }
}
