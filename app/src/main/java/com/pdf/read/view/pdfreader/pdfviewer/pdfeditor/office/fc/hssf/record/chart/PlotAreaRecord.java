package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PlotAreaRecord extends StandardRecord {
    public final static short sid = 0x1035;

    public PlotAreaRecord() {

    }

    public PlotAreaRecord(RecordInputStream in) {

    }

    public String toString() {

        String buffer = "[PLOTAREA]\n" +
                "[/PLOTAREA]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PlotAreaRecord rec = new PlotAreaRecord();

        return rec;
    }
}
