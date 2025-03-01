package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class SeriesToChartGroupRecord extends StandardRecord {
    public final static short sid = 0x1045;
    private short field_1_chartGroupIndex;

    public SeriesToChartGroupRecord() {

    }

    public SeriesToChartGroupRecord(RecordInputStream in) {
        field_1_chartGroupIndex = in.readShort();
    }

    public String toString() {

        String buffer = "[SeriesToChartGroup]\n" +
                "    .chartGroupIndex      = " +
                "0x" + HexDump.toHex(getChartGroupIndex()) +
                " (" + getChartGroupIndex() + " )" +
                System.getProperty("line.separator") +
                "[/SeriesToChartGroup]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_chartGroupIndex);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SeriesToChartGroupRecord rec = new SeriesToChartGroupRecord();

        rec.field_1_chartGroupIndex = field_1_chartGroupIndex;
        return rec;
    }

    public short getChartGroupIndex() {
        return field_1_chartGroupIndex;
    }

    public void setChartGroupIndex(short field_1_chartGroupIndex) {
        this.field_1_chartGroupIndex = field_1_chartGroupIndex;
    }
}
