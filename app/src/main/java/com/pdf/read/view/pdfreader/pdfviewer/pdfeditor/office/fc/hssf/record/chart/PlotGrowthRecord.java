package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PlotGrowthRecord extends StandardRecord {
    public final static short sid = 0x1064;
    private int field_1_horizontalScale;
    private int field_2_verticalScale;

    public PlotGrowthRecord() {

    }

    public PlotGrowthRecord(RecordInputStream in) {
        field_1_horizontalScale = in.readInt();
        field_2_verticalScale = in.readInt();

    }

    public String toString() {

        String buffer = "[PLOTGROWTH]\n" +
                "    .horizontalScale      = " +
                "0x" + HexDump.toHex(getHorizontalScale()) +
                " (" + getHorizontalScale() + " )" +
                System.getProperty("line.separator") +
                "    .verticalScale        = " +
                "0x" + HexDump.toHex(getVerticalScale()) +
                " (" + getVerticalScale() + " )" +
                System.getProperty("line.separator") +
                "[/PLOTGROWTH]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(field_1_horizontalScale);
        out.writeInt(field_2_verticalScale);
    }

    protected int getDataSize() {
        return 4 + 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PlotGrowthRecord rec = new PlotGrowthRecord();

        rec.field_1_horizontalScale = field_1_horizontalScale;
        rec.field_2_verticalScale = field_2_verticalScale;
        return rec;
    }

    public int getHorizontalScale() {
        return field_1_horizontalScale;
    }

    public void setHorizontalScale(int field_1_horizontalScale) {
        this.field_1_horizontalScale = field_1_horizontalScale;
    }

    public int getVerticalScale() {
        return field_2_verticalScale;
    }

    public void setVerticalScale(int field_2_verticalScale) {
        this.field_2_verticalScale = field_2_verticalScale;
    }
}
