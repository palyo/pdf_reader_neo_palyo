package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CategorySeriesAxisRecord extends StandardRecord {
    public final static short sid = 0x1020;

    private static final BitField valueAxisCrossing = BitFieldFactory.getInstance(0x1);
    private static final BitField crossesFarRight = BitFieldFactory.getInstance(0x2);
    private static final BitField reversed = BitFieldFactory.getInstance(0x4);

    private short field_1_crossingPoint;
    private short field_2_labelFrequency;
    private short field_3_tickMarkFrequency;
    private short field_4_options;

    public CategorySeriesAxisRecord() {

    }

    public CategorySeriesAxisRecord(RecordInputStream in) {
        field_1_crossingPoint = in.readShort();
        field_2_labelFrequency = in.readShort();
        field_3_tickMarkFrequency = in.readShort();
        field_4_options = in.readShort();
    }

    public String toString() {

        String buffer = "[CATSERRANGE]\n" +
                "    .crossingPoint        = " +
                "0x" + HexDump.toHex(getCrossingPoint()) +
                " (" + getCrossingPoint() + " )" +
                System.getProperty("line.separator") +
                "    .labelFrequency       = " +
                "0x" + HexDump.toHex(getLabelFrequency()) +
                " (" + getLabelFrequency() + " )" +
                System.getProperty("line.separator") +
                "    .tickMarkFrequency    = " +
                "0x" + HexDump.toHex(getTickMarkFrequency()) +
                " (" + getTickMarkFrequency() + " )" +
                System.getProperty("line.separator") +
                "    .options              = " +
                "0x" + HexDump.toHex(getOptions()) +
                " (" + getOptions() + " )" +
                System.getProperty("line.separator") +
                "         .valueAxisCrossing        = " + isValueAxisCrossing() + '\n' +
                "         .crossesFarRight          = " + isCrossesFarRight() + '\n' +
                "         .reversed                 = " + isReversed() + '\n' +
                "[/CATSERRANGE]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_crossingPoint);
        out.writeShort(field_2_labelFrequency);
        out.writeShort(field_3_tickMarkFrequency);
        out.writeShort(field_4_options);
    }

    protected int getDataSize() {
        return 2 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        CategorySeriesAxisRecord rec = new CategorySeriesAxisRecord();

        rec.field_1_crossingPoint = field_1_crossingPoint;
        rec.field_2_labelFrequency = field_2_labelFrequency;
        rec.field_3_tickMarkFrequency = field_3_tickMarkFrequency;
        rec.field_4_options = field_4_options;
        return rec;
    }

    public short getCrossingPoint() {
        return field_1_crossingPoint;
    }

    public void setCrossingPoint(short field_1_crossingPoint) {
        this.field_1_crossingPoint = field_1_crossingPoint;
    }

    public short getLabelFrequency() {
        return field_2_labelFrequency;
    }

    public void setLabelFrequency(short field_2_labelFrequency) {
        this.field_2_labelFrequency = field_2_labelFrequency;
    }

    public short getTickMarkFrequency() {
        return field_3_tickMarkFrequency;
    }

    public void setTickMarkFrequency(short field_3_tickMarkFrequency) {
        this.field_3_tickMarkFrequency = field_3_tickMarkFrequency;
    }

    public short getOptions() {
        return field_4_options;
    }

    public void setOptions(short field_4_options) {
        this.field_4_options = field_4_options;
    }

    public boolean isValueAxisCrossing() {
        return valueAxisCrossing.isSet(field_4_options);
    }

    public void setValueAxisCrossing(boolean value) {
        field_4_options = valueAxisCrossing.setShortBoolean(field_4_options, value);
    }

    public boolean isCrossesFarRight() {
        return crossesFarRight.isSet(field_4_options);
    }

    public void setCrossesFarRight(boolean value) {
        field_4_options = crossesFarRight.setShortBoolean(field_4_options, value);
    }

    public boolean isReversed() {
        return reversed.isSet(field_4_options);
    }

    public void setReversed(boolean value) {
        field_4_options = reversed.setShortBoolean(field_4_options, value);
    }
}
