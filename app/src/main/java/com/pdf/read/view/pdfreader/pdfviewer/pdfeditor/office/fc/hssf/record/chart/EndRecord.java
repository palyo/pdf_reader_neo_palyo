package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class EndRecord extends StandardRecord {
    public static final short sid = 0x1034;

    public EndRecord() {
    }

    public EndRecord(RecordInputStream in) {
    }

    public String toString() {

        String buffer = "[END]\n" +
                "[/END]\n";
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
        EndRecord er = new EndRecord();

        return er;
    }
}
