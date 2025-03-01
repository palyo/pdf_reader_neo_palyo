package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BarRecord extends StandardRecord {
    public final static short sid = 0x1017;

    private static final BitField horizontal = BitFieldFactory.getInstance(0x1);
    private static final BitField stacked = BitFieldFactory.getInstance(0x2);
    private static final BitField displayAsPercentage = BitFieldFactory.getInstance(0x4);
    private static final BitField shadow = BitFieldFactory.getInstance(0x8);

    private short field_1_barSpace;
    private short field_2_categorySpace;
    private short field_3_formatFlags;

    public BarRecord() {

    }

    public BarRecord(RecordInputStream in) {
        field_1_barSpace = in.readShort();
        field_2_categorySpace = in.readShort();
        field_3_formatFlags = in.readShort();
    }

    public String toString() {

        String buffer = "[BAR]\n" +
                "    .barSpace             = " +
                "0x" + HexDump.toHex(getBarSpace()) +
                " (" + getBarSpace() + " )" +
                System.getProperty("line.separator") +
                "    .categorySpace        = " +
                "0x" + HexDump.toHex(getCategorySpace()) +
                " (" + getCategorySpace() + " )" +
                System.getProperty("line.separator") +
                "    .formatFlags          = " +
                "0x" + HexDump.toHex(getFormatFlags()) +
                " (" + getFormatFlags() + " )" +
                System.getProperty("line.separator") +
                "         .horizontal               = " + isHorizontal() + '\n' +
                "         .stacked                  = " + isStacked() + '\n' +
                "         .displayAsPercentage      = " + isDisplayAsPercentage() + '\n' +
                "         .shadow                   = " + isShadow() + '\n' +
                "[/BAR]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_barSpace);
        out.writeShort(field_2_categorySpace);
        out.writeShort(field_3_formatFlags);
    }

    protected int getDataSize() {
        return 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        BarRecord rec = new BarRecord();

        rec.field_1_barSpace = field_1_barSpace;
        rec.field_2_categorySpace = field_2_categorySpace;
        rec.field_3_formatFlags = field_3_formatFlags;
        return rec;
    }

    public short getBarSpace() {
        return field_1_barSpace;
    }

    public void setBarSpace(short field_1_barSpace) {
        this.field_1_barSpace = field_1_barSpace;
    }

    public short getCategorySpace() {
        return field_2_categorySpace;
    }

    public void setCategorySpace(short field_2_categorySpace) {
        this.field_2_categorySpace = field_2_categorySpace;
    }

    public short getFormatFlags() {
        return field_3_formatFlags;
    }

    public void setFormatFlags(short field_3_formatFlags) {
        this.field_3_formatFlags = field_3_formatFlags;
    }

    public boolean isHorizontal() {
        return horizontal.isSet(field_3_formatFlags);
    }

    public void setHorizontal(boolean value) {
        field_3_formatFlags = horizontal.setShortBoolean(field_3_formatFlags, value);
    }

    public boolean isStacked() {
        return stacked.isSet(field_3_formatFlags);
    }

    public void setStacked(boolean value) {
        field_3_formatFlags = stacked.setShortBoolean(field_3_formatFlags, value);
    }

    public boolean isDisplayAsPercentage() {
        return displayAsPercentage.isSet(field_3_formatFlags);
    }

    public void setDisplayAsPercentage(boolean value) {
        field_3_formatFlags = displayAsPercentage.setShortBoolean(field_3_formatFlags, value);
    }

    public boolean isShadow() {
        return shadow.isSet(field_3_formatFlags);
    }

    public void setShadow(boolean value) {
        field_3_formatFlags = shadow.setShortBoolean(field_3_formatFlags, value);
    }
}
