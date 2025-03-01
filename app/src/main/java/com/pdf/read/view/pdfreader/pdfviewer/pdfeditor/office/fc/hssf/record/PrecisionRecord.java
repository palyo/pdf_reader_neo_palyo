package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PrecisionRecord
        extends StandardRecord {
    public final static short sid = 0xE;
    public short field_1_precision;

    public PrecisionRecord() {
    }

    public PrecisionRecord(RecordInputStream in) {
        field_1_precision = in.readShort();
    }

    public boolean getFullPrecision() {
        return (field_1_precision == 1);
    }

    public void setFullPrecision(boolean fullprecision) {
        if (fullprecision) {
            field_1_precision = 1;
        } else {
            field_1_precision = 0;
        }
    }

    public String toString() {

        String buffer = "[PRECISION]\n" +
                "    .precision       = " + getFullPrecision() +
                "\n" +
                "[/PRECISION]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_precision);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
