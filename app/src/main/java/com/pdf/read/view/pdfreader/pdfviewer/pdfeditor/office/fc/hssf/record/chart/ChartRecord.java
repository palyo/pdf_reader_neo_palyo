package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ChartRecord extends StandardRecord {
    public final static short sid = 0x1002;
    private int field_1_x;
    private int field_2_y;
    private int field_3_width;
    private int field_4_height;

    public ChartRecord() {
    }

    public ChartRecord(RecordInputStream in) {
        field_1_x = in.readInt();
        field_2_y = in.readInt();
        field_3_width = in.readInt();
        field_4_height = in.readInt();
    }

    public String toString() {

        String sb = "[CHART]\n" +
                "    .x     = " + getX() + '\n' +
                "    .y     = " + getY() + '\n' +
                "    .width = " + getWidth() + '\n' +
                "    .height= " + getHeight() + '\n' +
                "[/CHART]\n";
        return sb;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(field_1_x);
        out.writeInt(field_2_y);
        out.writeInt(field_3_width);
        out.writeInt(field_4_height);
    }

    protected int getDataSize() {
        return 4 + 4 + 4 + 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        ChartRecord rec = new ChartRecord();

        rec.field_1_x = field_1_x;
        rec.field_2_y = field_2_y;
        rec.field_3_width = field_3_width;
        rec.field_4_height = field_4_height;
        return rec;
    }

    public int getX() {
        return field_1_x;
    }

    public void setX(int x) {
        field_1_x = x;
    }

    public int getY() {
        return field_2_y;
    }

    public void setY(int y) {
        field_2_y = y;
    }

    public int getWidth() {
        return field_3_width;
    }

    public void setWidth(int width) {
        field_3_width = width;
    }

    public int getHeight() {
        return field_4_height;
    }

    public void setHeight(int height) {
        field_4_height = height;
    }
}
