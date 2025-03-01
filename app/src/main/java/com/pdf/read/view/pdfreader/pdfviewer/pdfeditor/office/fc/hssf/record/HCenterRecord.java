package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class HCenterRecord extends StandardRecord {
    public final static short sid = 0x0083;
    private short field_1_hcenter;

    public HCenterRecord() {
    }

    public HCenterRecord(RecordInputStream in) {
        field_1_hcenter = in.readShort();
    }

    public boolean getHCenter() {
        return (field_1_hcenter == 1);
    }

    public void setHCenter(boolean hc) {
        if (hc) {
            field_1_hcenter = 1;
        } else {
            field_1_hcenter = 0;
        }
    }

    public String toString() {

        String buffer = "[HCENTER]\n" +
                "    .hcenter        = " + getHCenter() +
                "\n" +
                "[/HCENTER]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_hcenter);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        HCenterRecord rec = new HCenterRecord();
        rec.field_1_hcenter = field_1_hcenter;
        return rec;
    }
}
