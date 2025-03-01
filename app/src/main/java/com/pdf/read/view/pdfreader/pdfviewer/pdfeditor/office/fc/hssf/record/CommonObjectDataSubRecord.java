package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CommonObjectDataSubRecord extends SubRecord {
    public final static short sid = 0x0015;
    public final static short OBJECT_TYPE_GROUP = 0;
    public final static short OBJECT_TYPE_CHART = 5;
    public final static short OBJECT_TYPE_COMMENT = 25;
    private static final BitField locked = BitFieldFactory.getInstance(0x0001);
    private static final BitField printable = BitFieldFactory.getInstance(0x0010);
    private static final BitField autofill = BitFieldFactory.getInstance(0x2000);
    private static final BitField autoline = BitFieldFactory.getInstance(0x4000);
    private short field_1_objectType;
    private int field_2_objectId;
    private short field_3_option;
    private int field_4_reserved1;
    private int field_5_reserved2;
    private int field_6_reserved3;

    public CommonObjectDataSubRecord() {

    }

    public CommonObjectDataSubRecord(LittleEndianInput in, int size) {
        if (size != 18) {
            throw new RecordFormatException("Expected size 18 but got (" + size + ")");
        }
        field_1_objectType = in.readShort();
        field_2_objectId = in.readUShort();
        field_3_option = in.readShort();
        field_4_reserved1 = in.readInt();
        field_5_reserved2 = in.readInt();
        field_6_reserved3 = in.readInt();
    }

    public String toString() {

        String buffer = "[ftCmo]\n" + "    .objectType           = " + "0x" + HexDump.toHex(getObjectType()) + " (" + getObjectType() + " )" + System.getProperty("line.separator") + "    .objectId             = " + "0x" + HexDump.toHex(getObjectId()) + " (" + getObjectId() + " )" + System.getProperty("line.separator") + "    .option               = " + "0x" + HexDump.toHex(getOption()) + " (" + getOption() + " )" + System.getProperty("line.separator") + "         .locked                   = " + isLocked() + '\n' + "         .printable                = " + isPrintable() + '\n' + "         .autofill                 = " + isAutofill() + '\n' + "         .autoline                 = " + isAutoline() + '\n' + "    .reserved1            = " + "0x" + HexDump.toHex(getReserved1()) + " (" + getReserved1() + " )" + System.getProperty("line.separator") + "    .reserved2            = " + "0x" + HexDump.toHex(getReserved2()) + " (" + getReserved2() + " )" + System.getProperty("line.separator") + "    .reserved3            = " + "0x" + HexDump.toHex(getReserved3()) + " (" + getReserved3() + " )" + System.getProperty("line.separator") + "[/ftCmo]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {

        out.writeShort(sid);
        out.writeShort(getDataSize());

        out.writeShort(field_1_objectType);
        out.writeShort(field_2_objectId);
        out.writeShort(field_3_option);
        out.writeInt(field_4_reserved1);
        out.writeInt(field_5_reserved2);
        out.writeInt(field_6_reserved3);
    }

    protected int getDataSize() {
        return 2 + 2 + 2 + 4 + 4 + 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        CommonObjectDataSubRecord rec = new CommonObjectDataSubRecord();

        rec.field_1_objectType = field_1_objectType;
        rec.field_2_objectId = field_2_objectId;
        rec.field_3_option = field_3_option;
        rec.field_4_reserved1 = field_4_reserved1;
        rec.field_5_reserved2 = field_5_reserved2;
        rec.field_6_reserved3 = field_6_reserved3;
        return rec;
    }

    public short getObjectType() {
        return field_1_objectType;
    }

    public void setObjectType(short field_1_objectType) {
        this.field_1_objectType = field_1_objectType;
    }

    public int getObjectId() {
        return field_2_objectId;
    }

    public void setObjectId(int field_2_objectId) {
        this.field_2_objectId = field_2_objectId;
    }

    public short getOption() {
        return field_3_option;
    }

    public void setOption(short field_3_option) {
        this.field_3_option = field_3_option;
    }

    public int getReserved1() {
        return field_4_reserved1;
    }

    public void setReserved1(int field_4_reserved1) {
        this.field_4_reserved1 = field_4_reserved1;
    }

    public int getReserved2() {
        return field_5_reserved2;
    }

    public void setReserved2(int field_5_reserved2) {
        this.field_5_reserved2 = field_5_reserved2;
    }

    public int getReserved3() {
        return field_6_reserved3;
    }

    public void setReserved3(int field_6_reserved3) {
        this.field_6_reserved3 = field_6_reserved3;
    }

    public boolean isLocked() {
        return locked.isSet(field_3_option);
    }

    public void setLocked(boolean value) {
        field_3_option = locked.setShortBoolean(field_3_option, value);
    }

    public boolean isPrintable() {
        return printable.isSet(field_3_option);
    }

    public void setPrintable(boolean value) {
        field_3_option = printable.setShortBoolean(field_3_option, value);
    }

    public boolean isAutofill() {
        return autofill.isSet(field_3_option);
    }

    public void setAutofill(boolean value) {
        field_3_option = autofill.setShortBoolean(field_3_option, value);
    }

    public boolean isAutoline() {
        return autoline.isSet(field_3_option);
    }

    public void setAutoline(boolean value) {
        field_3_option = autoline.setShortBoolean(field_3_option, value);
    }
}
