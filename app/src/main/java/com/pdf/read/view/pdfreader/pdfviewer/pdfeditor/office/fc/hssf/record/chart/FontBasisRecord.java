package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FontBasisRecord extends StandardRecord {
    public final static short sid = 0x1060;
    private short field_1_xBasis;
    private short field_2_yBasis;
    private short field_3_heightBasis;
    private short field_4_scale;
    private short field_5_indexToFontTable;

    public FontBasisRecord() {

    }

    public FontBasisRecord(RecordInputStream in) {
        field_1_xBasis = in.readShort();
        field_2_yBasis = in.readShort();
        field_3_heightBasis = in.readShort();
        field_4_scale = in.readShort();
        field_5_indexToFontTable = in.readShort();
    }

    public String toString() {

        String buffer = "[FBI]\n" +
                "    .xBasis               = " +
                "0x" + HexDump.toHex(getXBasis()) +
                " (" + getXBasis() + " )" +
                System.getProperty("line.separator") +
                "    .yBasis               = " +
                "0x" + HexDump.toHex(getYBasis()) +
                " (" + getYBasis() + " )" +
                System.getProperty("line.separator") +
                "    .heightBasis          = " +
                "0x" + HexDump.toHex(getHeightBasis()) +
                " (" + getHeightBasis() + " )" +
                System.getProperty("line.separator") +
                "    .scale                = " +
                "0x" + HexDump.toHex(getScale()) +
                " (" + getScale() + " )" +
                System.getProperty("line.separator") +
                "    .indexToFontTable     = " +
                "0x" + HexDump.toHex(getIndexToFontTable()) +
                " (" + getIndexToFontTable() + " )" +
                System.getProperty("line.separator") +
                "[/FBI]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_xBasis);
        out.writeShort(field_2_yBasis);
        out.writeShort(field_3_heightBasis);
        out.writeShort(field_4_scale);
        out.writeShort(field_5_indexToFontTable);
    }

    protected int getDataSize() {
        return 2 + 2 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        FontBasisRecord rec = new FontBasisRecord();

        rec.field_1_xBasis = field_1_xBasis;
        rec.field_2_yBasis = field_2_yBasis;
        rec.field_3_heightBasis = field_3_heightBasis;
        rec.field_4_scale = field_4_scale;
        rec.field_5_indexToFontTable = field_5_indexToFontTable;
        return rec;
    }

    public short getXBasis() {
        return field_1_xBasis;
    }

    public void setXBasis(short field_1_xBasis) {
        this.field_1_xBasis = field_1_xBasis;
    }

    public short getYBasis() {
        return field_2_yBasis;
    }

    public void setYBasis(short field_2_yBasis) {
        this.field_2_yBasis = field_2_yBasis;
    }

    public short getHeightBasis() {
        return field_3_heightBasis;
    }

    public void setHeightBasis(short field_3_heightBasis) {
        this.field_3_heightBasis = field_3_heightBasis;
    }

    public short getScale() {
        return field_4_scale;
    }

    public void setScale(short field_4_scale) {
        this.field_4_scale = field_4_scale;
    }

    public short getIndexToFontTable() {
        return field_5_indexToFontTable;
    }

    public void setIndexToFontTable(short field_5_indexToFontTable) {
        this.field_5_indexToFontTable = field_5_indexToFontTable;
    }
}
