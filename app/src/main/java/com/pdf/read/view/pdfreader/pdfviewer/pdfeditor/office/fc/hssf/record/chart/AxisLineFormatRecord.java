package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class AxisLineFormatRecord extends StandardRecord {
    public final static short sid = 0x1021;
    public final static short AXIS_TYPE_MAJOR_GRID_LINE = 1;
    private short field_1_axisType;

    public AxisLineFormatRecord() {

    }

    public AxisLineFormatRecord(RecordInputStream in) {
        field_1_axisType = in.readShort();
    }

    public String toString() {

        String buffer = "[AXISLINEFORMAT]\n" +
                "    .axisType             = " +
                "0x" + HexDump.toHex(getAxisType()) +
                " (" + getAxisType() + " )" +
                System.getProperty("line.separator") +
                "[/AXISLINEFORMAT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_axisType);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        AxisLineFormatRecord rec = new AxisLineFormatRecord();

        rec.field_1_axisType = field_1_axisType;
        return rec;
    }

    public short getAxisType() {
        return field_1_axisType;
    }

    public void setAxisType(short field_1_axisType) {
        this.field_1_axisType = field_1_axisType;
    }
}
