package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BeginRecord extends StandardRecord {
    public static final short sid = 0x1033;

    public BeginRecord() {
    }

    public BeginRecord(RecordInputStream in) {
    }

    public String toString() {

        String buffer = "[BEGIN]\n" +
                "[/BEGIN]\n";
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
        BeginRecord br = new BeginRecord();

        return br;
    }
}
