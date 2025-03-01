package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class WSBoolRecord extends StandardRecord {
    public final static short sid = 0x0081;

    private static final BitField autobreaks = BitFieldFactory.getInstance(0x01);

    private static final BitField dialog = BitFieldFactory.getInstance(0x10);
    private static final BitField applystyles = BitFieldFactory.getInstance(0x20);
    private static final BitField rowsumsbelow = BitFieldFactory.getInstance(0x40);
    private static final BitField rowsumsright = BitFieldFactory.getInstance(0x80);
    private static final BitField fittopage = BitFieldFactory.getInstance(0x01);

    private static final BitField displayguts = BitFieldFactory.getInstance(0x06);

    private static final BitField alternateexpression = BitFieldFactory.getInstance(0x40);
    private static final BitField alternateformula = BitFieldFactory.getInstance(0x80);
    private byte field_1_wsbool;
    private byte field_2_wsbool;

    public WSBoolRecord() {
    }

    public WSBoolRecord(RecordInputStream in) {
        byte[] data = in.readRemainder();
        field_1_wsbool =
                data[1];
        field_2_wsbool =
                data[0];
    }

    public byte getWSBool1() {
        return field_1_wsbool;
    }

    public void setWSBool1(byte bool1) {
        field_1_wsbool = bool1;
    }

    public boolean getAutobreaks() {
        return autobreaks.isSet(field_1_wsbool);
    }

    public void setAutobreaks(boolean ab) {
        field_1_wsbool = autobreaks.setByteBoolean(field_1_wsbool, ab);
    }

    public boolean getDialog() {
        return dialog.isSet(field_1_wsbool);
    }

    public void setDialog(boolean isDialog) {
        field_1_wsbool = dialog.setByteBoolean(field_1_wsbool, isDialog);
    }

    public boolean getRowSumsBelow() {
        return rowsumsbelow.isSet(field_1_wsbool);
    }

    public void setRowSumsBelow(boolean below) {
        field_1_wsbool = rowsumsbelow.setByteBoolean(field_1_wsbool, below);
    }

    public boolean getRowSumsRight() {
        return rowsumsright.isSet(field_1_wsbool);
    }

    public void setRowSumsRight(boolean right) {
        field_1_wsbool = rowsumsright.setByteBoolean(field_1_wsbool, right);
    }

    public byte getWSBool2() {
        return field_2_wsbool;
    }

    public void setWSBool2(byte bool2) {
        field_2_wsbool = bool2;
    }

    public boolean getFitToPage() {
        return fittopage.isSet(field_2_wsbool);
    }

    public void setFitToPage(boolean fit2page) {
        field_2_wsbool = fittopage.setByteBoolean(field_2_wsbool, fit2page);
    }

    public boolean getDisplayGuts() {
        return displayguts.isSet(field_2_wsbool);
    }

    public void setDisplayGuts(boolean guts) {
        field_2_wsbool = displayguts.setByteBoolean(field_2_wsbool, guts);
    }

    public boolean getAlternateExpression() {
        return alternateexpression.isSet(field_2_wsbool);
    }

    public void setAlternateExpression(boolean altexp) {
        field_2_wsbool = alternateexpression.setByteBoolean(field_2_wsbool,
                altexp);
    }

    public boolean getAlternateFormula() {
        return alternateformula.isSet(field_2_wsbool);
    }

    public void setAlternateFormula(boolean formula) {
        field_2_wsbool = alternateformula.setByteBoolean(field_2_wsbool,
                formula);
    }

    public String toString() {

        String buffer = "[WSBOOL]\n" +
                "    .wsbool1        = " +
                Integer.toHexString(getWSBool1()) + "\n" +
                "        .autobreaks = " + getAutobreaks() +
                "\n" +
                "        .dialog     = " + getDialog() +
                "\n" +
                "        .rowsumsbelw= " + getRowSumsBelow() +
                "\n" +
                "        .rowsumsrigt= " + getRowSumsRight() +
                "\n" +
                "    .wsbool2        = " +
                Integer.toHexString(getWSBool2()) + "\n" +
                "        .fittopage  = " + getFitToPage() +
                "\n" +
                "        .displayguts= " + getDisplayGuts() +
                "\n" +
                "        .alternateex= " +
                getAlternateExpression() + "\n" +
                "        .alternatefo= " + getAlternateFormula() +
                "\n" +
                "[/WSBOOL]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(getWSBool2());
        out.writeByte(getWSBool1());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        WSBoolRecord rec = new WSBoolRecord();
        rec.field_1_wsbool = field_1_wsbool;
        rec.field_2_wsbool = field_2_wsbool;
        return rec;
    }
}
