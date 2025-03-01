package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class LeftMarginRecord extends StandardRecord implements Margin {
    public final static short sid = 0x0026;
    private double field_1_margin;

    public LeftMarginRecord() {
    }

    public LeftMarginRecord(RecordInputStream in) {
        field_1_margin = in.readDouble();
    }

    public String toString() {
        String buffer = "[LeftMargin]\n" +
                "    .margin               = " + " (" + getMargin() + " )\n" +
                "[/LeftMargin]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeDouble(field_1_margin);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }

    public double getMargin() {
        return field_1_margin;
    }

    public void setMargin(double field_1_margin) {
        this.field_1_margin = field_1_margin;
    }

    public Object clone() {
        LeftMarginRecord rec = new LeftMarginRecord();
        rec.field_1_margin = this.field_1_margin;
        return rec;
    }
}  
