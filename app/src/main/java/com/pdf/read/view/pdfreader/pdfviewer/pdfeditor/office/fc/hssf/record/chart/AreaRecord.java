package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class AreaRecord extends StandardRecord {
    public final static short sid = 0x101A;
    private static final BitField stacked = BitFieldFactory.getInstance(0x1);
    private static final BitField displayAsPercentage = BitFieldFactory.getInstance(0x2);
    private static final BitField shadow = BitFieldFactory.getInstance(0x4);
    private short field_1_formatFlags;

    public AreaRecord() {

    }

    public AreaRecord(RecordInputStream in) {

        field_1_formatFlags = in.readShort();
    }

    public String toString() {

        String buffer = "[AREA]\n" +
                "    .formatFlags          = " +
                "0x" + HexDump.toHex(getFormatFlags()) +
                " (" + getFormatFlags() + " )" +
                System.getProperty("line.separator") +
                "         .stacked                  = " + isStacked() + '\n' +
                "         .displayAsPercentage      = " + isDisplayAsPercentage() + '\n' +
                "         .shadow                   = " + isShadow() + '\n' +
                "[/AREA]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_formatFlags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        AreaRecord rec = new AreaRecord();

        rec.field_1_formatFlags = field_1_formatFlags;
        return rec;
    }

    public short getFormatFlags() {
        return field_1_formatFlags;
    }

    public void setFormatFlags(short field_1_formatFlags) {
        this.field_1_formatFlags = field_1_formatFlags;
    }

    public boolean isStacked() {
        return stacked.isSet(field_1_formatFlags);
    }

    public void setStacked(boolean value) {
        field_1_formatFlags = stacked.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isDisplayAsPercentage() {
        return displayAsPercentage.isSet(field_1_formatFlags);
    }

    public void setDisplayAsPercentage(boolean value) {
        field_1_formatFlags = displayAsPercentage.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isShadow() {
        return shadow.isSet(field_1_formatFlags);
    }

    public void setShadow(boolean value) {
        field_1_formatFlags = shadow.setShortBoolean(field_1_formatFlags, value);
    }
}
