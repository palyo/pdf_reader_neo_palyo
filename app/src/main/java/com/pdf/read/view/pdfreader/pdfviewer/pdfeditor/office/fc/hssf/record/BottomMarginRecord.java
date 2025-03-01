package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BottomMarginRecord extends StandardRecord implements Margin {
    public final static short sid = 0x29;
    private double field_1_margin;

    public BottomMarginRecord() {

    }

    public BottomMarginRecord(RecordInputStream in) {
        field_1_margin = in.readDouble();
    }

    public String toString() {
        String buffer = "[BottomMargin]\n" +
                "    .margin               = " +
                " (" + getMargin() + " )\n" +
                "[/BottomMargin]\n";
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
        BottomMarginRecord rec = new BottomMarginRecord();
        rec.field_1_margin = this.field_1_margin;
        return rec;
    }

}
