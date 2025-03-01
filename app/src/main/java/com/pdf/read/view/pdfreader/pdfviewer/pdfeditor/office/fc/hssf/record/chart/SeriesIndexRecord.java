package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class SeriesIndexRecord extends StandardRecord {
    public final static short sid = 0x1065;
    private short field_1_index;

    public SeriesIndexRecord() {

    }

    public SeriesIndexRecord(RecordInputStream in) {
        field_1_index = in.readShort();
    }

    public String toString() {

        String buffer = "[SINDEX]\n" +
                "    .index                = " +
                "0x" + HexDump.toHex(getIndex()) +
                " (" + getIndex() + " )" +
                System.getProperty("line.separator") +
                "[/SINDEX]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_index);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SeriesIndexRecord rec = new SeriesIndexRecord();

        rec.field_1_index = field_1_index;
        return rec;
    }

    public short getIndex() {
        return field_1_index;
    }

    public void setIndex(short field_1_index) {
        this.field_1_index = field_1_index;
    }
}
