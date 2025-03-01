package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DatRecord extends StandardRecord {
    public final static short sid = 0x1063;

    private static final BitField horizontalBorder = BitFieldFactory.getInstance(0x1);
    private static final BitField verticalBorder = BitFieldFactory.getInstance(0x2);
    private static final BitField border = BitFieldFactory.getInstance(0x4);
    private static final BitField showSeriesKey = BitFieldFactory.getInstance(0x8);

    private short field_1_options;

    public DatRecord() {

    }

    public DatRecord(RecordInputStream in) {
        field_1_options = in.readShort();
    }

    public String toString() {
        String buffer = "[DAT]\n" +
                "    .options              = " +
                "0x" + HexDump.toHex(getOptions()) +
                " (" + getOptions() + " )" +
                System.getProperty("line.separator") +
                "         .horizontalBorder         = " + isHorizontalBorder() + '\n' +
                "         .verticalBorder           = " + isVerticalBorder() + '\n' +
                "         .border                   = " + isBorder() + '\n' +
                "         .showSeriesKey            = " + isShowSeriesKey() + '\n' +
                "[/DAT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_options);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DatRecord rec = new DatRecord();

        rec.field_1_options = field_1_options;
        return rec;
    }

    public short getOptions() {
        return field_1_options;
    }

    public void setOptions(short field_1_options) {
        this.field_1_options = field_1_options;
    }

    public boolean isHorizontalBorder() {
        return horizontalBorder.isSet(field_1_options);
    }

    public void setHorizontalBorder(boolean value) {
        field_1_options = horizontalBorder.setShortBoolean(field_1_options, value);
    }

    public boolean isVerticalBorder() {
        return verticalBorder.isSet(field_1_options);
    }

    public void setVerticalBorder(boolean value) {
        field_1_options = verticalBorder.setShortBoolean(field_1_options, value);
    }

    public boolean isBorder() {
        return border.isSet(field_1_options);
    }

    public void setBorder(boolean value) {
        field_1_options = border.setShortBoolean(field_1_options, value);
    }

    public boolean isShowSeriesKey() {
        return showSeriesKey.isSet(field_1_options);
    }

    public void setShowSeriesKey(boolean value) {
        field_1_options = showSeriesKey.setShortBoolean(field_1_options, value);
    }
}
