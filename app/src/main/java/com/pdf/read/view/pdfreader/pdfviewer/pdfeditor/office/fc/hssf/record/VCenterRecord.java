package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class VCenterRecord extends StandardRecord {
    public final static short sid = 0x84;
    private int field_1_vcenter;

    public VCenterRecord() {
    }

    public VCenterRecord(RecordInputStream in) {
        field_1_vcenter = in.readShort();
    }

    public boolean getVCenter() {
        return (field_1_vcenter == 1);
    }

    public void setVCenter(boolean hc) {
        field_1_vcenter = hc ? 1 : 0;
    }

    public String toString() {

        String buffer = "[VCENTER]\n" +
                "    .vcenter        = " + getVCenter() +
                "\n" +
                "[/VCENTER]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_vcenter);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        VCenterRecord rec = new VCenterRecord();
        rec.field_1_vcenter = field_1_vcenter;
        return rec;
    }
}
