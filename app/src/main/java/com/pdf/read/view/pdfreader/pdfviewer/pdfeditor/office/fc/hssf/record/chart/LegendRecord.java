package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class LegendRecord extends StandardRecord {
    public final static short sid = 0x1015;
    public final static byte TYPE_RIGHT = 3;
    public final static byte SPACING_MEDIUM = 1;
    private static final BitField autoPosition = BitFieldFactory.getInstance(0x01);
    private static final BitField autoSeries = BitFieldFactory.getInstance(0x02);
    private static final BitField autoXPositioning = BitFieldFactory.getInstance(0x04);
    private static final BitField autoYPositioning = BitFieldFactory.getInstance(0x08);
    private static final BitField vertical = BitFieldFactory.getInstance(0x10);
    private static final BitField dataTable = BitFieldFactory.getInstance(0x20);
    private int field_1_xAxisUpperLeft;
    private int field_2_yAxisUpperLeft;
    private int field_3_xSize;
    private int field_4_ySize;
    private byte field_5_type;
    private byte field_6_spacing;
    private short field_7_options;

    public LegendRecord() {

    }

    public LegendRecord(RecordInputStream in) {
        field_1_xAxisUpperLeft = in.readInt();
        field_2_yAxisUpperLeft = in.readInt();
        field_3_xSize = in.readInt();
        field_4_ySize = in.readInt();
        field_5_type = in.readByte();
        field_6_spacing = in.readByte();
        field_7_options = in.readShort();
    }

    public String toString() {

        String buffer = "[LEGEND]\n" +
                "    .xAxisUpperLeft       = " +
                "0x" + HexDump.toHex(getXAxisUpperLeft()) +
                " (" + getXAxisUpperLeft() + " )" +
                System.getProperty("line.separator") +
                "    .yAxisUpperLeft       = " +
                "0x" + HexDump.toHex(getYAxisUpperLeft()) +
                " (" + getYAxisUpperLeft() + " )" +
                System.getProperty("line.separator") +
                "    .xSize                = " +
                "0x" + HexDump.toHex(getXSize()) +
                " (" + getXSize() + " )" +
                System.getProperty("line.separator") +
                "    .ySize                = " +
                "0x" + HexDump.toHex(getYSize()) +
                " (" + getYSize() + " )" +
                System.getProperty("line.separator") +
                "    .type                 = " +
                "0x" + HexDump.toHex(getType()) +
                " (" + getType() + " )" +
                System.getProperty("line.separator") +
                "    .spacing              = " +
                "0x" + HexDump.toHex(getSpacing()) +
                " (" + getSpacing() + " )" +
                System.getProperty("line.separator") +
                "    .options              = " +
                "0x" + HexDump.toHex(getOptions()) +
                " (" + getOptions() + " )" +
                System.getProperty("line.separator") +
                "         .autoPosition             = " + isAutoPosition() + '\n' +
                "         .autoSeries               = " + isAutoSeries() + '\n' +
                "         .autoXPositioning         = " + isAutoXPositioning() + '\n' +
                "         .autoYPositioning         = " + isAutoYPositioning() + '\n' +
                "         .vertical                 = " + isVertical() + '\n' +
                "         .dataTable                = " + isDataTable() + '\n' +
                "[/LEGEND]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(field_1_xAxisUpperLeft);
        out.writeInt(field_2_yAxisUpperLeft);
        out.writeInt(field_3_xSize);
        out.writeInt(field_4_ySize);
        out.writeByte(field_5_type);
        out.writeByte(field_6_spacing);
        out.writeShort(field_7_options);
    }

    protected int getDataSize() {
        return 4 + 4 + 4 + 4 + 1 + 1 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        LegendRecord rec = new LegendRecord();

        rec.field_1_xAxisUpperLeft = field_1_xAxisUpperLeft;
        rec.field_2_yAxisUpperLeft = field_2_yAxisUpperLeft;
        rec.field_3_xSize = field_3_xSize;
        rec.field_4_ySize = field_4_ySize;
        rec.field_5_type = field_5_type;
        rec.field_6_spacing = field_6_spacing;
        rec.field_7_options = field_7_options;
        return rec;
    }

    public int getXAxisUpperLeft() {
        return field_1_xAxisUpperLeft;
    }

    public void setXAxisUpperLeft(int field_1_xAxisUpperLeft) {
        this.field_1_xAxisUpperLeft = field_1_xAxisUpperLeft;
    }

    public int getYAxisUpperLeft() {
        return field_2_yAxisUpperLeft;
    }

    public void setYAxisUpperLeft(int field_2_yAxisUpperLeft) {
        this.field_2_yAxisUpperLeft = field_2_yAxisUpperLeft;
    }

    public int getXSize() {
        return field_3_xSize;
    }

    public void setXSize(int field_3_xSize) {
        this.field_3_xSize = field_3_xSize;
    }

    public int getYSize() {
        return field_4_ySize;
    }

    public void setYSize(int field_4_ySize) {
        this.field_4_ySize = field_4_ySize;
    }

    public byte getType() {
        return field_5_type;
    }

    public void setType(byte field_5_type) {
        this.field_5_type = field_5_type;
    }

    public byte getSpacing() {
        return field_6_spacing;
    }

    public void setSpacing(byte field_6_spacing) {
        this.field_6_spacing = field_6_spacing;
    }

    public short getOptions() {
        return field_7_options;
    }

    public void setOptions(short field_7_options) {
        this.field_7_options = field_7_options;
    }

    public boolean isAutoPosition() {
        return autoPosition.isSet(field_7_options);
    }

    public void setAutoPosition(boolean value) {
        field_7_options = autoPosition.setShortBoolean(field_7_options, value);
    }

    public boolean isAutoSeries() {
        return autoSeries.isSet(field_7_options);
    }

    public void setAutoSeries(boolean value) {
        field_7_options = autoSeries.setShortBoolean(field_7_options, value);
    }

    public boolean isAutoXPositioning() {
        return autoXPositioning.isSet(field_7_options);
    }

    public void setAutoXPositioning(boolean value) {
        field_7_options = autoXPositioning.setShortBoolean(field_7_options, value);
    }

    public boolean isAutoYPositioning() {
        return autoYPositioning.isSet(field_7_options);
    }

    public void setAutoYPositioning(boolean value) {
        field_7_options = autoYPositioning.setShortBoolean(field_7_options, value);
    }

    public boolean isVertical() {
        return vertical.isSet(field_7_options);
    }

    public void setVertical(boolean value) {
        field_7_options = vertical.setShortBoolean(field_7_options, value);
    }

    public boolean isDataTable() {
        return dataTable.isSet(field_7_options);
    }

    public void setDataTable(boolean value) {
        field_7_options = dataTable.setShortBoolean(field_7_options, value);
    }
}
