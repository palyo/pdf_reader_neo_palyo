package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CalcCountRecord
        extends StandardRecord {
    public final static short sid = 0xC;
    private short field_1_iterations;

    public CalcCountRecord() {
    }

    public CalcCountRecord(RecordInputStream in) {
        field_1_iterations = in.readShort();
    }

    public short getIterations() {
        return field_1_iterations;
    }

    public void setIterations(short iterations) {
        field_1_iterations = iterations;
    }

    public String toString() {

        String buffer = "[CALCCOUNT]\n" +
                "    .iterations     = " +
                Integer.toHexString(getIterations()) + "\n" +
                "[/CALCCOUNT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getIterations());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        CalcCountRecord rec = new CalcCountRecord();
        rec.field_1_iterations = field_1_iterations;
        return rec;
    }
}
