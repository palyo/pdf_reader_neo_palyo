package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public abstract class LFOAbstractType {

    private static final BitField fHtmlChecked = new BitField(0x01);
    private static final BitField fHtmlUnsupported = new BitField(0x02);
    private static final BitField fHtmlListTextNotSharpDot = new BitField(0x04);
    private static final BitField fHtmlNotPeriod = new BitField(0x08);
    private static final BitField fHtmlFirstLineMismatch = new BitField(0x10);
    private static final BitField fHtmlTabLeftIndentMismatch = new BitField(
            0x20);
    private static final BitField fHtmlHangingIndentBeneathNumber = new BitField(
            0x40);
    private static final BitField fHtmlBuiltInBullet = new BitField(0x80);
    protected int field_1_lsid;
    protected int field_2_reserved1;
    protected int field_3_reserved2;
    protected byte field_4_clfolvl;
    protected byte field_5_ibstFltAutoNum;
    protected byte field_6_grfhic;
    protected byte field_7_reserved3;

    protected LFOAbstractType() {
    }

    public static int getSize() {
        return 4 + 4 + 4 + 1 + 1 + 1 + 1;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_lsid = LittleEndian.getInt(data, offset);
        field_2_reserved1 = LittleEndian.getInt(data, 0x4 + offset);
        field_3_reserved2 = LittleEndian.getInt(data, 0x8 + offset);
        field_4_clfolvl = data[0xc + offset];
        field_5_ibstFltAutoNum = data[0xd + offset];
        field_6_grfhic = data[0xe + offset];
        field_7_reserved3 = data[0xf + offset];
    }

    public void serialize(byte[] data, int offset) {
        LittleEndian.putInt(data, offset, field_1_lsid);
        LittleEndian.putInt(data, 0x4 + offset, field_2_reserved1);
        LittleEndian.putInt(data, 0x8 + offset, field_3_reserved2);
        data[0xc + offset] = field_4_clfolvl;
        data[0xd + offset] = field_5_ibstFltAutoNum;
        data[0xe + offset] = field_6_grfhic;
        data[0xf + offset] = field_7_reserved3;
    }

    public String toString() {

        String builder = "[LFO]\n" +
                "    .lsid                 = " +
                " (" + getLsid() + " )\n" +
                "    .reserved1            = " +
                " (" + getReserved1() + " )\n" +
                "    .reserved2            = " +
                " (" + getReserved2() + " )\n" +
                "    .clfolvl              = " +
                " (" + getClfolvl() + " )\n" +
                "    .ibstFltAutoNum       = " +
                " (" + getIbstFltAutoNum() + " )\n" +
                "    .grfhic               = " +
                " (" + getGrfhic() + " )\n" +
                "         .fHtmlChecked             = " +
                isFHtmlChecked() + '\n' +
                "         .fHtmlUnsupported         = " +
                isFHtmlUnsupported() + '\n' +
                "         .fHtmlListTextNotSharpDot     = " +
                isFHtmlListTextNotSharpDot() + '\n' +
                "         .fHtmlNotPeriod           = " +
                isFHtmlNotPeriod() + '\n' +
                "         .fHtmlFirstLineMismatch     = " +
                isFHtmlFirstLineMismatch() + '\n' +
                "         .fHtmlTabLeftIndentMismatch     = " +
                isFHtmlTabLeftIndentMismatch() + '\n' +
                "         .fHtmlHangingIndentBeneathNumber     = " +
                isFHtmlHangingIndentBeneathNumber() + '\n' +
                "         .fHtmlBuiltInBullet       = " +
                isFHtmlBuiltInBullet() + '\n' +
                "    .reserved3            = " +
                " (" + getReserved3() + " )\n" +
                "[/LFO]\n";
        return builder;
    }

    public int getLsid() {
        return field_1_lsid;
    }

    public void setLsid(int field_1_lsid) {
        this.field_1_lsid = field_1_lsid;
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

    public byte getClfolvl() {
        return field_4_clfolvl;
    }

    public void setClfolvl(byte field_4_clfolvl) {
        this.field_4_clfolvl = field_4_clfolvl;
    }

    public byte getIbstFltAutoNum() {
        return field_5_ibstFltAutoNum;
    }

    public void setIbstFltAutoNum(byte field_5_ibstFltAutoNum) {
        this.field_5_ibstFltAutoNum = field_5_ibstFltAutoNum;
    }

    public byte getGrfhic() {
        return field_6_grfhic;
    }

    public void setGrfhic(byte field_6_grfhic) {
        this.field_6_grfhic = field_6_grfhic;
    }

    public byte getReserved3() {
        return field_7_reserved3;
    }

    public void setReserved3(byte field_7_reserved3) {
        this.field_7_reserved3 = field_7_reserved3;
    }

    public boolean isFHtmlChecked() {
        return fHtmlChecked.isSet(field_6_grfhic);
    }

    public void setFHtmlChecked(boolean value) {
        field_6_grfhic = (byte) fHtmlChecked.setBoolean(field_6_grfhic, value);
    }

    public boolean isFHtmlUnsupported() {
        return fHtmlUnsupported.isSet(field_6_grfhic);
    }

    public void setFHtmlUnsupported(boolean value) {
        field_6_grfhic = (byte) fHtmlUnsupported.setBoolean(field_6_grfhic,
                value);
    }

    public boolean isFHtmlListTextNotSharpDot() {
        return fHtmlListTextNotSharpDot.isSet(field_6_grfhic);
    }

    public void setFHtmlListTextNotSharpDot(boolean value) {
        field_6_grfhic = (byte) fHtmlListTextNotSharpDot.setBoolean(
                field_6_grfhic, value);
    }

    public boolean isFHtmlNotPeriod() {
        return fHtmlNotPeriod.isSet(field_6_grfhic);
    }

    public void setFHtmlNotPeriod(boolean value) {
        field_6_grfhic = (byte) fHtmlNotPeriod.setBoolean(field_6_grfhic,
                value);
    }

    public boolean isFHtmlFirstLineMismatch() {
        return fHtmlFirstLineMismatch.isSet(field_6_grfhic);
    }

    public void setFHtmlFirstLineMismatch(boolean value) {
        field_6_grfhic = (byte) fHtmlFirstLineMismatch.setBoolean(
                field_6_grfhic, value);
    }

    public boolean isFHtmlTabLeftIndentMismatch() {
        return fHtmlTabLeftIndentMismatch.isSet(field_6_grfhic);
    }

    public void setFHtmlTabLeftIndentMismatch(boolean value) {
        field_6_grfhic = (byte) fHtmlTabLeftIndentMismatch.setBoolean(
                field_6_grfhic, value);
    }

    public boolean isFHtmlHangingIndentBeneathNumber() {
        return fHtmlHangingIndentBeneathNumber.isSet(field_6_grfhic);
    }

    public void setFHtmlHangingIndentBeneathNumber(boolean value) {
        field_6_grfhic = (byte) fHtmlHangingIndentBeneathNumber.setBoolean(
                field_6_grfhic, value);
    }

    public boolean isFHtmlBuiltInBullet() {
        return fHtmlBuiltInBullet.isSet(field_6_grfhic);
    }

    public void setFHtmlBuiltInBullet(boolean value) {
        field_6_grfhic = (byte) fHtmlBuiltInBullet.setBoolean(field_6_grfhic,
                value);
    }

} 