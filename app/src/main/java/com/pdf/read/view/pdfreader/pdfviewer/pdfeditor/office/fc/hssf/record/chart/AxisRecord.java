package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class AxisRecord extends StandardRecord {
    public final static short sid = 0x101d;
    public final static short AXIS_TYPE_CATEGORY_OR_X_AXIS = 0;
    public final static short AXIS_TYPE_VALUE_AXIS = 1;
    private short field_1_axisType;
    private int field_2_reserved1;
    private int field_3_reserved2;
    private int field_4_reserved3;
    private int field_5_reserved4;

    public AxisRecord() {

    }

    public AxisRecord(RecordInputStream in) {
        field_1_axisType = in.readShort();
        field_2_reserved1 = in.readInt();
        field_3_reserved2 = in.readInt();
        field_4_reserved3 = in.readInt();
        field_5_reserved4 = in.readInt();
    }

    public String toString() {

        String buffer = "[AXIS]\n" +
                "    .axisType             = " +
                "0x" + HexDump.toHex(getAxisType()) +
                " (" + getAxisType() + " )" +
                System.getProperty("line.separator") +
                "    .reserved1            = " +
                "0x" + HexDump.toHex(getReserved1()) +
                " (" + getReserved1() + " )" +
                System.getProperty("line.separator") +
                "    .reserved2            = " +
                "0x" + HexDump.toHex(getReserved2()) +
                " (" + getReserved2() + " )" +
                System.getProperty("line.separator") +
                "    .reserved3            = " +
                "0x" + HexDump.toHex(getReserved3()) +
                " (" + getReserved3() + " )" +
                System.getProperty("line.separator") +
                "    .reserved4            = " +
                "0x" + HexDump.toHex(getReserved4()) +
                " (" + getReserved4() + " )" +
                System.getProperty("line.separator") +
                "[/AXIS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_axisType);
        out.writeInt(field_2_reserved1);
        out.writeInt(field_3_reserved2);
        out.writeInt(field_4_reserved3);
        out.writeInt(field_5_reserved4);
    }

    protected int getDataSize() {
        return 2 + 4 + 4 + 4 + 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        AxisRecord rec = new AxisRecord();

        rec.field_1_axisType = field_1_axisType;
        rec.field_2_reserved1 = field_2_reserved1;
        rec.field_3_reserved2 = field_3_reserved2;
        rec.field_4_reserved3 = field_4_reserved3;
        rec.field_5_reserved4 = field_5_reserved4;
        return rec;
    }

    public short getAxisType() {
        return field_1_axisType;
    }

    public void setAxisType(short field_1_axisType) {
        this.field_1_axisType = field_1_axisType;
    }

    public int getReserved1() {
        return field_2_reserved1;
    }

    public void setReserved1(int field_2_reserved1) {
        this.field_2_reserved1 = field_2_reserved1;
    }

    public int getReserved2() {
        return field_3_reserved2;
    }

    public void setReserved2(int field_3_reserved2) {
        this.field_3_reserved2 = field_3_reserved2;
    }

    public int getReserved3() {
        return field_4_reserved3;
    }

    public void setReserved3(int field_4_reserved3) {
        this.field_4_reserved3 = field_4_reserved3;
    }

    public int getReserved4() {
        return field_5_reserved4;
    }

    public void setReserved4(int field_5_reserved4) {
        this.field_5_reserved4 = field_5_reserved4;
    }
}
