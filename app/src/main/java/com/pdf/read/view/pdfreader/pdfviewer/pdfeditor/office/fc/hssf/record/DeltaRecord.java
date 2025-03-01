package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DeltaRecord extends StandardRecord {
    public final static short sid = 0x0010;
    public final static double DEFAULT_VALUE = 0.0010;
    private final double field_1_max_change;

    public DeltaRecord(double maxChange) {
        field_1_max_change = maxChange;
    }

    public DeltaRecord(RecordInputStream in) {
        field_1_max_change = in.readDouble();
    }

    public double getMaxChange() {
        return field_1_max_change;
    }

    public String toString() {

        String buffer = "[DELTA]\n" +
                "    .maxchange = " + getMaxChange() + "\n" +
                "[/DELTA]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeDouble(getMaxChange());
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {

        return this;
    }
}
