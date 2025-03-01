package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CalcModeRecord extends StandardRecord {
    public final static short sid = 0xD;

    private short field_1_calcmode;

    public CalcModeRecord() {
    }

    public CalcModeRecord(RecordInputStream in) {
        field_1_calcmode = in.readShort();
    }

    public short getCalcMode() {
        return field_1_calcmode;
    }

    public void setCalcMode(short calcmode) {
        field_1_calcmode = calcmode;
    }

    public String toString() {

        String buffer = "[CALCMODE]\n" +
                "    .calcmode       = " +
                Integer.toHexString(getCalcMode()) + "\n" +
                "[/CALCMODE]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCalcMode());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        CalcModeRecord rec = new CalcModeRecord();
        rec.field_1_calcmode = field_1_calcmode;
        return rec;
    }
}
