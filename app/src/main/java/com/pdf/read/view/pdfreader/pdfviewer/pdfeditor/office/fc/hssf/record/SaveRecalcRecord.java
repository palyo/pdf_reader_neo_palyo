package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class SaveRecalcRecord
        extends StandardRecord {
    public final static short sid = 0x5f;
    private short field_1_recalc;

    public SaveRecalcRecord() {
    }

    public SaveRecalcRecord(RecordInputStream in) {
        field_1_recalc = in.readShort();
    }

    public boolean getRecalc() {
        return (field_1_recalc == 1);
    }

    public void setRecalc(boolean recalc) {
        field_1_recalc = (short) ((recalc) ? 1
                : 0);
    }

    public String toString() {

        String buffer = "[SAVERECALC]\n" +
                "    .recalc         = " + getRecalc() +
                "\n" +
                "[/SAVERECALC]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_recalc);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SaveRecalcRecord rec = new SaveRecalcRecord();
        rec.field_1_recalc = field_1_recalc;
        return rec;
    }
}
