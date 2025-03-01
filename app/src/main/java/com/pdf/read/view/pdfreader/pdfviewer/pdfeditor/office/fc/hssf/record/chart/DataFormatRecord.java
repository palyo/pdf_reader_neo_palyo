package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DataFormatRecord extends StandardRecord {
    public final static short sid = 0x1006;

    private static final BitField useExcel4Colors = BitFieldFactory.getInstance(0x1);

    private short field_1_pointNumber;
    private short field_2_seriesIndex;
    private short field_3_seriesNumber;
    private short field_4_formatFlags;

    public DataFormatRecord() {

    }

    public DataFormatRecord(RecordInputStream in) {
        field_1_pointNumber = in.readShort();
        field_2_seriesIndex = in.readShort();
        field_3_seriesNumber = in.readShort();
        field_4_formatFlags = in.readShort();
    }

    public String toString() {

        String buffer = "[DATAFORMAT]\n" +
                "    .pointNumber          = " +
                "0x" + HexDump.toHex(getPointNumber()) +
                " (" + getPointNumber() + " )" +
                System.getProperty("line.separator") +
                "    .seriesIndex          = " +
                "0x" + HexDump.toHex(getSeriesIndex()) +
                " (" + getSeriesIndex() + " )" +
                System.getProperty("line.separator") +
                "    .seriesNumber         = " +
                "0x" + HexDump.toHex(getSeriesNumber()) +
                " (" + getSeriesNumber() + " )" +
                System.getProperty("line.separator") +
                "    .formatFlags          = " +
                "0x" + HexDump.toHex(getFormatFlags()) +
                " (" + getFormatFlags() + " )" +
                System.getProperty("line.separator") +
                "         .useExcel4Colors          = " + isUseExcel4Colors() + '\n' +
                "[/DATAFORMAT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_pointNumber);
        out.writeShort(field_2_seriesIndex);
        out.writeShort(field_3_seriesNumber);
        out.writeShort(field_4_formatFlags);
    }

    protected int getDataSize() {
        return 2 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DataFormatRecord rec = new DataFormatRecord();

        rec.field_1_pointNumber = field_1_pointNumber;
        rec.field_2_seriesIndex = field_2_seriesIndex;
        rec.field_3_seriesNumber = field_3_seriesNumber;
        rec.field_4_formatFlags = field_4_formatFlags;
        return rec;
    }

    public short getPointNumber() {
        return field_1_pointNumber;
    }

    public void setPointNumber(short field_1_pointNumber) {
        this.field_1_pointNumber = field_1_pointNumber;
    }

    public short getSeriesIndex() {
        return field_2_seriesIndex;
    }

    public void setSeriesIndex(short field_2_seriesIndex) {
        this.field_2_seriesIndex = field_2_seriesIndex;
    }

    public short getSeriesNumber() {
        return field_3_seriesNumber;
    }

    public void setSeriesNumber(short field_3_seriesNumber) {
        this.field_3_seriesNumber = field_3_seriesNumber;
    }

    public short getFormatFlags() {
        return field_4_formatFlags;
    }

    public void setFormatFlags(short field_4_formatFlags) {
        this.field_4_formatFlags = field_4_formatFlags;
    }

    public boolean isUseExcel4Colors() {
        return useExcel4Colors.isSet(field_4_formatFlags);
    }

    public void setUseExcel4Colors(boolean value) {
        field_4_formatFlags = useExcel4Colors.setShortBoolean(field_4_formatFlags, value);
    }
}
