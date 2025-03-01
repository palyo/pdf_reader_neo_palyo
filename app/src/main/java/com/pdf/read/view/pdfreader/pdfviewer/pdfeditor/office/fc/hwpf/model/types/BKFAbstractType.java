package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public abstract class BKFAbstractType {

    private static final BitField itcFirst = new BitField(0x007F);
    private static final BitField fPub = new BitField(0x0080);
    private static final BitField itcLim = new BitField(0x7F00);
    private static final BitField fCol = new BitField(0x8000);
    protected short field_1_ibkl;
    protected short field_2_bkf_flags;

    protected BKFAbstractType() {
    }

    public static int getSize() {
        return 2 + 2;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_ibkl = LittleEndian.getShort(data, offset);
        field_2_bkf_flags = LittleEndian.getShort(data, 0x2 + offset);
    }

    public void serialize(byte[] data, int offset) {
        LittleEndian.putShort(data, offset, field_1_ibkl);
        LittleEndian.putShort(data, 0x2 + offset, field_2_bkf_flags);
    }

    public String toString() {

        String builder = "[BKF]\n" +
                "    .ibkl                 = " +
                " (" + getIbkl() + " )\n" +
                "    .bkf_flags            = " +
                " (" + getBkf_flags() + " )\n" +
                "         .itcFirst                 = " + getItcFirst() + '\n' +
                "         .fPub                     = " + isFPub() + '\n' +
                "         .itcLim                   = " + getItcLim() + '\n' +
                "         .fCol                     = " + isFCol() + '\n' +
                "[/BKF]\n";
        return builder;
    }

    public short getIbkl() {
        return field_1_ibkl;
    }

    public void setIbkl(short field_1_ibkl) {
        this.field_1_ibkl = field_1_ibkl;
    }

    public short getBkf_flags() {
        return field_2_bkf_flags;
    }

    public void setBkf_flags(short field_2_bkf_flags) {
        this.field_2_bkf_flags = field_2_bkf_flags;
    }

    public byte getItcFirst() {
        return (byte) itcFirst.getValue(field_2_bkf_flags);
    }

    public void setItcFirst(byte value) {
        field_2_bkf_flags = (short) itcFirst.setValue(field_2_bkf_flags, value);
    }

    public boolean isFPub() {
        return fPub.isSet(field_2_bkf_flags);
    }

    public void setFPub(boolean value) {
        field_2_bkf_flags = (short) fPub.setBoolean(field_2_bkf_flags, value);
    }

    public byte getItcLim() {
        return (byte) itcLim.getValue(field_2_bkf_flags);
    }

    public void setItcLim(byte value) {
        field_2_bkf_flags = (short) itcLim.setValue(field_2_bkf_flags, value);
    }

    public boolean isFCol() {
        return fCol.isSet(field_2_bkf_flags);
    }

    public void setFCol(boolean value) {
        field_2_bkf_flags = (short) fCol.setBoolean(field_2_bkf_flags, value);
    }

} 