package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class SCLRecord extends StandardRecord {
    public final static short sid = 0x00A0;
    private short field_1_numerator;
    private short field_2_denominator;

    public SCLRecord() {

    }

    public SCLRecord(RecordInputStream in) {
        field_1_numerator = in.readShort();
        field_2_denominator = in.readShort();
    }

    public String toString() {

        String buffer = "[SCL]\n" +
                "    .numerator            = " +
                "0x" + HexDump.toHex(getNumerator()) +
                " (" + getNumerator() + " )" +
                System.getProperty("line.separator") +
                "    .denominator          = " +
                "0x" + HexDump.toHex(getDenominator()) +
                " (" + getDenominator() + " )" +
                System.getProperty("line.separator") +
                "[/SCL]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_numerator);
        out.writeShort(field_2_denominator);
    }

    protected int getDataSize() {
        return 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SCLRecord rec = new SCLRecord();

        rec.field_1_numerator = field_1_numerator;
        rec.field_2_denominator = field_2_denominator;
        return rec;
    }

    public short getNumerator() {
        return field_1_numerator;
    }

    public void setNumerator(short field_1_numerator) {
        this.field_1_numerator = field_1_numerator;
    }

    public short getDenominator() {
        return field_2_denominator;
    }

    public void setDenominator(short field_2_denominator) {
        this.field_2_denominator = field_2_denominator;
    }
}
