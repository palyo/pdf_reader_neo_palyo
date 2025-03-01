package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class TickRecord extends StandardRecord {
    public final static short sid = 0x101E;

    private static final BitField autoTextColor = BitFieldFactory.getInstance(0x1);
    private static final BitField autoTextBackground = BitFieldFactory.getInstance(0x2);
    private static final BitField rotation = BitFieldFactory.getInstance(0x1c);
    private static final BitField autorotate = BitFieldFactory.getInstance(0x20);

    private byte field_1_majorTickType;
    private byte field_2_minorTickType;
    private byte field_3_labelPosition;
    private byte field_4_background;
    private int field_5_labelColorRgb;
    private int field_6_zero1;
    private int field_7_zero2;
    private int field_8_zero3;
    private int field_9_zero4;
    private short field_10_options;
    private short field_11_tickColor;
    private short field_12_zero5;

    public TickRecord() {

    }

    public TickRecord(RecordInputStream in) {

        field_1_majorTickType = in.readByte();
        field_2_minorTickType = in.readByte();
        field_3_labelPosition = in.readByte();
        field_4_background = in.readByte();
        field_5_labelColorRgb = in.readInt();
        field_6_zero1 = in.readInt();
        field_7_zero2 = in.readInt();
        field_8_zero3 = in.readInt();
        field_9_zero4 = in.readInt();

        field_10_options = in.readShort();
        field_11_tickColor = in.readShort();
        field_12_zero5 = in.readShort();
    }

    public String toString() {

        String buffer = "[TICK]\n" +
                "    .majorTickType        = " +
                "0x" + HexDump.toHex(getMajorTickType()) +
                " (" + getMajorTickType() + " )" +
                System.getProperty("line.separator") +
                "    .minorTickType        = " +
                "0x" + HexDump.toHex(getMinorTickType()) +
                " (" + getMinorTickType() + " )" +
                System.getProperty("line.separator") +
                "    .labelPosition        = " +
                "0x" + HexDump.toHex(getLabelPosition()) +
                " (" + getLabelPosition() + " )" +
                System.getProperty("line.separator") +
                "    .background           = " +
                "0x" + HexDump.toHex(getBackground()) +
                " (" + getBackground() + " )" +
                System.getProperty("line.separator") +
                "    .labelColorRgb        = " +
                "0x" + HexDump.toHex(getLabelColorRgb()) +
                " (" + getLabelColorRgb() + " )" +
                System.getProperty("line.separator") +
                "    .zero1                = " +
                "0x" + HexDump.toHex(getZero1()) +
                " (" + getZero1() + " )" +
                System.getProperty("line.separator") +
                "    .zero2                = " +
                "0x" + HexDump.toHex(getZero2()) +
                " (" + getZero2() + " )" +
                System.getProperty("line.separator") +
                "    .options              = " +
                "0x" + HexDump.toHex(getOptions()) +
                " (" + getOptions() + " )" +
                System.getProperty("line.separator") +
                "         .autoTextColor            = " + isAutoTextColor() + '\n' +
                "         .autoTextBackground       = " + isAutoTextBackground() + '\n' +
                "         .rotation                 = " + getRotation() + '\n' +
                "         .autorotate               = " + isAutorotate() + '\n' +
                "    .tickColor            = " +
                "0x" + HexDump.toHex(getTickColor()) +
                " (" + getTickColor() + " )" +
                System.getProperty("line.separator") +
                "    .zero3                = " +
                "0x" + HexDump.toHex(getZero3()) +
                " (" + getZero3() + " )" +
                System.getProperty("line.separator") +
                "[/TICK]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(field_1_majorTickType);
        out.writeByte(field_2_minorTickType);
        out.writeByte(field_3_labelPosition);
        out.writeByte(field_4_background);
        out.writeInt(field_5_labelColorRgb);
        out.writeInt(field_6_zero1);
        out.writeInt(field_7_zero2);
        out.writeInt(field_8_zero3);
        out.writeInt(field_9_zero4);
        out.writeShort(field_10_options);
        out.writeShort(field_11_tickColor);
        out.writeShort(field_12_zero5);
    }

    protected int getDataSize() {
        return 1 + 1 + 1 + 1 + 4 + 8 + 8 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        TickRecord rec = new TickRecord();

        rec.field_1_majorTickType = field_1_majorTickType;
        rec.field_2_minorTickType = field_2_minorTickType;
        rec.field_3_labelPosition = field_3_labelPosition;
        rec.field_4_background = field_4_background;
        rec.field_5_labelColorRgb = field_5_labelColorRgb;
        rec.field_6_zero1 = field_6_zero1;
        rec.field_7_zero2 = field_7_zero2;
        rec.field_8_zero3 = field_8_zero3;
        rec.field_9_zero4 = field_9_zero4;
        rec.field_10_options = field_10_options;
        rec.field_11_tickColor = field_11_tickColor;
        rec.field_12_zero5 = field_12_zero5;
        return rec;
    }

    public byte getMajorTickType() {
        return field_1_majorTickType;
    }

    public void setMajorTickType(byte field_1_majorTickType) {
        this.field_1_majorTickType = field_1_majorTickType;
    }

    public byte getMinorTickType() {
        return field_2_minorTickType;
    }

    public void setMinorTickType(byte field_2_minorTickType) {
        this.field_2_minorTickType = field_2_minorTickType;
    }

    public byte getLabelPosition() {
        return field_3_labelPosition;
    }

    public void setLabelPosition(byte field_3_labelPosition) {
        this.field_3_labelPosition = field_3_labelPosition;
    }

    public byte getBackground() {
        return field_4_background;
    }

    public void setBackground(byte field_4_background) {
        this.field_4_background = field_4_background;
    }

    public int getLabelColorRgb() {
        return field_5_labelColorRgb;
    }

    public void setLabelColorRgb(int field_5_labelColorRgb) {
        this.field_5_labelColorRgb = field_5_labelColorRgb;
    }

    public int getZero1() {
        return field_6_zero1;
    }

    public void setZero1(int field_6_zero1) {
        this.field_6_zero1 = field_6_zero1;
    }

    public int getZero2() {
        return field_7_zero2;
    }

    public void setZero2(int field_7_zero2) {
        this.field_7_zero2 = field_7_zero2;
    }

    public short getOptions() {
        return field_10_options;
    }

    public void setOptions(short field_10_options) {
        this.field_10_options = field_10_options;
    }

    public short getTickColor() {
        return field_11_tickColor;
    }

    public void setTickColor(short field_11_tickColor) {
        this.field_11_tickColor = field_11_tickColor;
    }

    public short getZero3() {
        return field_12_zero5;
    }

    public void setZero3(short field_12_zero3) {
        this.field_12_zero5 = field_12_zero3;
    }

    public boolean isAutoTextColor() {
        return autoTextColor.isSet(field_10_options);
    }

    public void setAutoTextColor(boolean value) {
        field_10_options = autoTextColor.setShortBoolean(field_10_options, value);
    }

    public boolean isAutoTextBackground() {
        return autoTextBackground.isSet(field_10_options);
    }

    public void setAutoTextBackground(boolean value) {
        field_10_options = autoTextBackground.setShortBoolean(field_10_options, value);
    }

    public short getRotation() {
        return rotation.getShortValue(field_10_options);
    }

    public void setRotation(short value) {
        field_10_options = rotation.setShortValue(field_10_options, value);
    }

    public boolean isAutorotate() {
        return autorotate.isSet(field_10_options);
    }

    public void setAutorotate(boolean value) {
        field_10_options = autorotate.setShortBoolean(field_10_options, value);
    }
}
