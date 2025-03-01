package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DefaultDataLabelTextPropertiesRecord extends StandardRecord {
    public final static short sid = 0x1024;
    public final static short CATEGORY_DATA_TYPE_ALL_TEXT_CHARACTERISTIC = 2;
    private short field_1_categoryDataType;

    public DefaultDataLabelTextPropertiesRecord() {

    }

    public DefaultDataLabelTextPropertiesRecord(RecordInputStream in) {
        field_1_categoryDataType = in.readShort();
    }

    public String toString() {

        String buffer = "[DEFAULTTEXT]\n" +
                "    .categoryDataType     = " +
                "0x" + HexDump.toHex(getCategoryDataType()) +
                " (" + getCategoryDataType() + " )" +
                System.getProperty("line.separator") +
                "[/DEFAULTTEXT]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_categoryDataType);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        DefaultDataLabelTextPropertiesRecord rec = new DefaultDataLabelTextPropertiesRecord();

        rec.field_1_categoryDataType = field_1_categoryDataType;
        return rec;
    }

    public short getCategoryDataType() {
        return field_1_categoryDataType;
    }

    public void setCategoryDataType(short field_1_categoryDataType) {
        this.field_1_categoryDataType = field_1_categoryDataType;
    }
}
