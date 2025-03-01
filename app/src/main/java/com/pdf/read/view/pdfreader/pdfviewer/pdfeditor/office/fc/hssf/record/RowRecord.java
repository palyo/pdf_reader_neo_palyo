package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class RowRecord extends StandardRecord {
    public final static short sid = 0x0208;

    public static final int ENCODED_SIZE = 20;

    private static final int OPTION_BITS_ALWAYS_SET = 0x0100;
    private static final int DEFAULT_HEIGHT_BIT = 0x8000;
    private static final BitField outlineLevel = BitFieldFactory.getInstance(0x07);

    private static final BitField colapsed = BitFieldFactory.getInstance(0x10);
    private static final BitField zeroHeight = BitFieldFactory.getInstance(0x20);
    private static final BitField badFontHeight = BitFieldFactory.getInstance(0x40);
    private static final BitField formatted = BitFieldFactory.getInstance(0x80);
    private int field_1_row_number;
    private int field_2_first_col;
    private int field_3_last_col;
    private short field_4_height;
    private short field_5_optimize;

    private short field_6_reserved;

    private int field_7_option_flags;
    private short field_8_xf_index;

    public RowRecord(int rowNumber) {
        field_1_row_number = rowNumber;
        field_4_height = (short) 0xFF;
        field_5_optimize = (short) 0;
        field_6_reserved = (short) 0;
        field_7_option_flags = OPTION_BITS_ALWAYS_SET;

        field_8_xf_index = (short) 0xf;
        setEmpty();
    }

    public RowRecord(RecordInputStream in) {
        field_1_row_number = in.readUShort();
        field_2_first_col = in.readShort();
        field_3_last_col = in.readShort();
        field_4_height = in.readShort();
        field_5_optimize = in.readShort();
        field_6_reserved = in.readShort();
        field_7_option_flags = in.readShort();
        field_8_xf_index = in.readShort();
    }

    public void setEmpty() {
        field_2_first_col = 0;
        field_3_last_col = 0;
    }

    public boolean isEmpty() {
        return (field_2_first_col | field_3_last_col) == 0;
    }

    public int getRowNumber() {
        return field_1_row_number;
    }

    public void setRowNumber(int row) {
        field_1_row_number = row;
    }

    public int getFirstCol() {
        return field_2_first_col;
    }

    public void setFirstCol(int col) {
        field_2_first_col = col;
    }

    public int getLastCol() {
        return field_3_last_col;
    }

    public void setLastCol(int col) {
        field_3_last_col = col;
    }

    public short getHeight() {
        return field_4_height;
    }

    public void setHeight(short height) {
        field_4_height = height;
    }

    public short getOptimize() {
        return field_5_optimize;
    }

    public void setOptimize(short optimize) {
        field_5_optimize = optimize;
    }

    public short getOptionFlags() {
        return (short) field_7_option_flags;
    }

    public short getOutlineLevel() {
        return (short) outlineLevel.getValue(field_7_option_flags);
    }

    public void setOutlineLevel(short ol) {
        field_7_option_flags = outlineLevel.setValue(field_7_option_flags, ol);
    }

    public boolean getColapsed() {
        return (colapsed.isSet(field_7_option_flags));
    }

    public void setColapsed(boolean c) {
        field_7_option_flags = colapsed.setBoolean(field_7_option_flags, c);
    }

    public boolean getZeroHeight() {
        return zeroHeight.isSet(field_7_option_flags);
    }

    public void setZeroHeight(boolean z) {
        field_7_option_flags = zeroHeight.setBoolean(field_7_option_flags, z);
    }

    public boolean getBadFontHeight() {
        return badFontHeight.isSet(field_7_option_flags);
    }

    public void setBadFontHeight(boolean f) {
        field_7_option_flags = badFontHeight.setBoolean(field_7_option_flags, f);
    }

    public boolean getFormatted() {
        return formatted.isSet(field_7_option_flags);
    }

    public void setFormatted(boolean f) {
        field_7_option_flags = formatted.setBoolean(field_7_option_flags, f);
    }

    public short getXFIndex() {
        return field_8_xf_index;
    }

    public void setXFIndex(short index) {
        field_8_xf_index = index;
    }

    public String toString() {

        String sb = "[ROW]\n" +
                "    .rownumber      = " + Integer.toHexString(getRowNumber()) +
                "\n" +
                "    .firstcol       = " + String.valueOf(HexDump.shortToHex(getFirstCol())) + "\n" +
                "    .lastcol        = " + String.valueOf(HexDump.shortToHex(getLastCol())) + "\n" +
                "    .height         = " + String.valueOf(HexDump.shortToHex(getHeight())) + "\n" +
                "    .optimize       = " + String.valueOf(HexDump.shortToHex(getOptimize())) + "\n" +
                "    .reserved       = " + String.valueOf(HexDump.shortToHex(field_6_reserved)) + "\n" +
                "    .optionflags    = " + String.valueOf(HexDump.shortToHex(getOptionFlags())) + "\n" +
                "        .outlinelvl = " + Integer.toHexString(getOutlineLevel()) + "\n" +
                "        .colapsed   = " + getColapsed() + "\n" +
                "        .zeroheight = " + getZeroHeight() + "\n" +
                "        .badfontheig= " + getBadFontHeight() + "\n" +
                "        .formatted  = " + getFormatted() + "\n" +
                "    .xfindex        = " + Integer.toHexString(getXFIndex()) + "\n" +
                "[/ROW]\n";
        return sb;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getRowNumber());
        out.writeShort(getFirstCol() == -1 ? (short) 0 : getFirstCol());
        out.writeShort(getLastCol() == -1 ? (short) 0 : getLastCol());
        out.writeShort(getHeight());
        out.writeShort(getOptimize());
        out.writeShort(field_6_reserved);
        out.writeShort(getOptionFlags());
        out.writeShort(getXFIndex());
    }

    protected int getDataSize() {
        return ENCODED_SIZE - 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        RowRecord rec = new RowRecord(field_1_row_number);
        rec.field_2_first_col = field_2_first_col;
        rec.field_3_last_col = field_3_last_col;
        rec.field_4_height = field_4_height;
        rec.field_5_optimize = field_5_optimize;
        rec.field_6_reserved = field_6_reserved;
        rec.field_7_option_flags = field_7_option_flags;
        rec.field_8_xf_index = field_8_xf_index;
        return rec;
    }
}
