package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FontIndexRecord extends StandardRecord {
    public final static short sid = 0x1026;
    private short field_1_fontIndex;

    public FontIndexRecord() {

    }

    public FontIndexRecord(RecordInputStream in) {
        field_1_fontIndex = in.readShort();
    }

    public String toString() {

        String buffer = "[FONTX]\n" +
                "    .fontIndex            = " +
                "0x" + HexDump.toHex(getFontIndex()) +
                " (" + getFontIndex() + " )" +
                System.getProperty("line.separator") +
                "[/FONTX]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_fontIndex);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        FontIndexRecord rec = new FontIndexRecord();

        rec.field_1_fontIndex = field_1_fontIndex;
        return rec;
    }

    public short getFontIndex() {
        return field_1_fontIndex;
    }

    public void setFontIndex(short field_1_fontIndex) {
        this.field_1_fontIndex = field_1_fontIndex;
    }
}
