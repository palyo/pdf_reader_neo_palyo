package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UnitsRecord extends StandardRecord {
    public final static short sid = 0x1001;
    private short field_1_units;

    public UnitsRecord() {

    }

    public UnitsRecord(RecordInputStream in) {
        field_1_units = in.readShort();
    }

    public String toString() {
        String buffer = "[UNITS]\n" +
                "    .units                = " +
                "0x" + HexDump.toHex(getUnits()) +
                " (" + getUnits() + " )" +
                System.getProperty("line.separator") +
                "[/UNITS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_units);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        UnitsRecord rec = new UnitsRecord();

        rec.field_1_units = field_1_units;
        return rec;
    }

    public short getUnits() {
        return field_1_units;
    }

    public void setUnits(short field_1_units) {
        this.field_1_units = field_1_units;
    }
}
