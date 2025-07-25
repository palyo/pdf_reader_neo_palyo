package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DVALRecord extends StandardRecord {
    public final static short sid = 0x01B2;

    private short field_1_options;
    private int field_2_horiz_pos;
    private int field_3_vert_pos;

    private int field_cbo_id;

    private int field_5_dv_no;

    public DVALRecord() {
        field_cbo_id = 0xFFFFFFFF;
        field_5_dv_no = 0x00000000;
    }

    public DVALRecord(RecordInputStream in) {
        field_1_options = in.readShort();
        field_2_horiz_pos = in.readInt();
        field_3_vert_pos = in.readInt();
        field_cbo_id = in.readInt();
        field_5_dv_no = in.readInt();
    }

    public short getOptions() {
        return field_1_options;
    }

    public void setOptions(short options) {
        field_1_options = options;
    }

    public int getHorizontalPos() {
        return field_2_horiz_pos;
    }

    public void setHorizontalPos(int horiz_pos) {
        field_2_horiz_pos = horiz_pos;
    }

    public int getVerticalPos() {
        return field_3_vert_pos;
    }

    public void setVerticalPos(int vert_pos) {
        field_3_vert_pos = vert_pos;
    }

    public int getObjectID() {
        return field_cbo_id;
    }

    public void setObjectID(int cboID) {
        field_cbo_id = cboID;
    }

    public int getDVRecNo() {
        return field_5_dv_no;
    }

    public void setDVRecNo(int dvNo) {
        field_5_dv_no = dvNo;
    }

    public String toString() {

        String buffer = "[DVAL]\n" +
                "    .options      = " + getOptions() + '\n' +
                "    .horizPos     = " + getHorizontalPos() + '\n' +
                "    .vertPos      = " + getVerticalPos() + '\n' +
                "    .comboObjectID   = " + Integer.toHexString(getObjectID()) + "\n" +
                "    .DVRecordsNumber = " + Integer.toHexString(getDVRecNo()) + "\n" +
                "[/DVAL]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {

        out.writeShort(getOptions());
        out.writeInt(getHorizontalPos());
        out.writeInt(getVerticalPos());
        out.writeInt(getObjectID());
        out.writeInt(getDVRecNo());
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DVALRecord rec = new DVALRecord();
        rec.field_1_options = field_1_options;
        rec.field_2_horiz_pos = field_2_horiz_pos;
        rec.field_3_vert_pos = field_3_vert_pos;
        rec.field_cbo_id = field_cbo_id;
        rec.field_5_dv_no = field_5_dv_no;
        return rec;
    }
}
