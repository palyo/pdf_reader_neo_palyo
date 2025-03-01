package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class GridsetRecord
        extends StandardRecord {
    public final static short sid = 0x82;
    public short field_1_gridset_flag;

    public GridsetRecord() {
    }

    public GridsetRecord(RecordInputStream in) {
        field_1_gridset_flag = in.readShort();
    }

    public boolean getGridset() {
        return (field_1_gridset_flag == 1);
    }

    public void setGridset(boolean gridset) {
        if (gridset) {
            field_1_gridset_flag = 1;
        } else {
            field_1_gridset_flag = 0;
        }
    }

    public String toString() {

        String buffer = "[GRIDSET]\n" +
                "    .gridset        = " + getGridset() +
                "\n" +
                "[/GRIDSET]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_gridset_flag);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        GridsetRecord rec = new GridsetRecord();
        rec.field_1_gridset_flag = field_1_gridset_flag;
        return rec;
    }
}
