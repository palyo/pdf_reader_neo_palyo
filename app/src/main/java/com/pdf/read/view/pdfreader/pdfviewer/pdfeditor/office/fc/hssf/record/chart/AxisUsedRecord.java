package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class AxisUsedRecord extends StandardRecord {
    public final static short sid = 0x1046;
    private short field_1_numAxis;

    public AxisUsedRecord() {

    }

    public AxisUsedRecord(RecordInputStream in) {
        field_1_numAxis = in.readShort();
    }

    public String toString() {

        String buffer = "[AXISUSED]\n" +
                "    .numAxis              = " +
                "0x" + HexDump.toHex(getNumAxis()) +
                " (" + getNumAxis() + " )" +
                System.getProperty("line.separator") +
                "[/AXISUSED]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_numAxis);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        AxisUsedRecord rec = new AxisUsedRecord();

        rec.field_1_numAxis = field_1_numAxis;
        return rec;
    }

    public short getNumAxis() {
        return field_1_numAxis;
    }

    public void setNumAxis(short field_1_numAxis) {
        this.field_1_numAxis = field_1_numAxis;
    }
}
