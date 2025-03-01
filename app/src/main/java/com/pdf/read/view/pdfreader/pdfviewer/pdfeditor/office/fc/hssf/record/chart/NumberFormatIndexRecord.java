package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class NumberFormatIndexRecord extends StandardRecord {
    public final static short sid = 0x104E;
    private short field_1_formatIndex;

    public NumberFormatIndexRecord() {

    }

    public NumberFormatIndexRecord(RecordInputStream in) {
        field_1_formatIndex = in.readShort();
    }

    public String toString() {

        String buffer = "[IFMT]\n" +
                "    .formatIndex          = " +
                "0x" + HexDump.toHex(getFormatIndex()) +
                " (" + getFormatIndex() + " )" +
                System.getProperty("line.separator") +
                "[/IFMT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_formatIndex);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        NumberFormatIndexRecord rec = new NumberFormatIndexRecord();

        rec.field_1_formatIndex = field_1_formatIndex;
        return rec;
    }

    public short getFormatIndex() {
        return field_1_formatIndex;
    }

    public void setFormatIndex(short field_1_formatIndex) {
        this.field_1_formatIndex = field_1_formatIndex;
    }
}
