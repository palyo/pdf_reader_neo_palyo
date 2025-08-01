package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class LineFormatRecord extends StandardRecord {
    public final static short sid = 0x1007;
    public final static short LINE_PATTERN_SOLID = 0;
    public final static short LINE_PATTERN_DASH = 1;
    public final static short LINE_PATTERN_DOT = 2;
    public final static short LINE_PATTERN_DASH_DOT = 3;
    public final static short LINE_PATTERN_DASH_DOT_DOT = 4;
    public final static short LINE_PATTERN_NONE = 5;
    public final static short LINE_PATTERN_DARK_GRAY_PATTERN = 6;
    public final static short LINE_PATTERN_MEDIUM_GRAY_PATTERN = 7;
    public final static short LINE_PATTERN_LIGHT_GRAY_PATTERN = 8;
    public final static short WEIGHT_HAIRLINE = -1;
    public final static short WEIGHT_NARROW = 0;
    public final static short WEIGHT_MEDIUM = 1;
    public final static short WEIGHT_WIDE = 2;
    private static final BitField auto = BitFieldFactory.getInstance(0x1);
    private static final BitField drawTicks = BitFieldFactory.getInstance(0x4);
    private static final BitField unknown = BitFieldFactory.getInstance(0x4);
    private int field_1_lineColor;
    private short field_2_linePattern;
    private short field_3_weight;
    private short field_4_format;
    private short field_5_colourPaletteIndex;

    public LineFormatRecord() {

    }

    public LineFormatRecord(RecordInputStream in) {
        field_1_lineColor = in.readInt();
        field_2_linePattern = in.readShort();
        field_3_weight = in.readShort();
        field_4_format = in.readShort();
        field_5_colourPaletteIndex = in.readShort();
    }

    public String toString() {

        String buffer = "[LINEFORMAT]\n" +
                "    .lineColor            = " +
                "0x" + HexDump.toHex(getLineColor()) +
                " (" + getLineColor() + " )" +
                System.getProperty("line.separator") +
                "    .linePattern          = " +
                "0x" + HexDump.toHex(getLinePattern()) +
                " (" + getLinePattern() + " )" +
                System.getProperty("line.separator") +
                "    .weight               = " +
                "0x" + HexDump.toHex(getWeight()) +
                " (" + getWeight() + " )" +
                System.getProperty("line.separator") +
                "    .format               = " +
                "0x" + HexDump.toHex(getFormat()) +
                " (" + getFormat() + " )" +
                System.getProperty("line.separator") +
                "         .auto                     = " + isAuto() + '\n' +
                "         .drawTicks                = " + isDrawTicks() + '\n' +
                "         .unknown                  = " + isUnknown() + '\n' +
                "    .colourPaletteIndex   = " +
                "0x" + HexDump.toHex(getColourPaletteIndex()) +
                " (" + getColourPaletteIndex() + " )" +
                System.getProperty("line.separator") +
                "[/LINEFORMAT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(field_1_lineColor);
        out.writeShort(field_2_linePattern);
        out.writeShort(field_3_weight);
        out.writeShort(field_4_format);
        out.writeShort(field_5_colourPaletteIndex);
    }

    protected int getDataSize() {
        return 4 + 2 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        LineFormatRecord rec = new LineFormatRecord();

        rec.field_1_lineColor = field_1_lineColor;
        rec.field_2_linePattern = field_2_linePattern;
        rec.field_3_weight = field_3_weight;
        rec.field_4_format = field_4_format;
        rec.field_5_colourPaletteIndex = field_5_colourPaletteIndex;
        return rec;
    }

    public int getLineColor() {
        return field_1_lineColor;
    }

    public void setLineColor(int field_1_lineColor) {
        this.field_1_lineColor = field_1_lineColor;
    }

    public short getLinePattern() {
        return field_2_linePattern;
    }

    public void setLinePattern(short field_2_linePattern) {
        this.field_2_linePattern = field_2_linePattern;
    }

    public short getWeight() {
        return field_3_weight;
    }

    public void setWeight(short field_3_weight) {
        this.field_3_weight = field_3_weight;
    }

    public short getFormat() {
        return field_4_format;
    }

    public void setFormat(short field_4_format) {
        this.field_4_format = field_4_format;
    }

    public short getColourPaletteIndex() {
        return field_5_colourPaletteIndex;
    }

    public void setColourPaletteIndex(short field_5_colourPaletteIndex) {
        this.field_5_colourPaletteIndex = field_5_colourPaletteIndex;
    }

    public boolean isAuto() {
        return auto.isSet(field_4_format);
    }

    public void setAuto(boolean value) {
        field_4_format = auto.setShortBoolean(field_4_format, value);
    }

    public boolean isDrawTicks() {
        return drawTicks.isSet(field_4_format);
    }

    public void setDrawTicks(boolean value) {
        field_4_format = drawTicks.setShortBoolean(field_4_format, value);
    }

    public boolean isUnknown() {
        return unknown.isSet(field_4_format);
    }

    public void setUnknown(boolean value) {
        field_4_format = unknown.setShortBoolean(field_4_format, value);
    }
}
