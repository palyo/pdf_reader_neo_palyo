package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.HDFType;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public abstract class FLDAbstractType implements HDFType {

    private static final BitField ch = new BitField(0x1f);
    private static final BitField reserved = new BitField(0xe0);
    private static final BitField fDiffer = new BitField(0x01);
    private static final BitField fZombieEmbed = new BitField(0x02);
    private static final BitField fResultDirty = new BitField(0x04);
    private static final BitField fResultEdited = new BitField(0x08);
    private static final BitField fLocked = new BitField(0x10);
    private static final BitField fPrivateResult = new BitField(0x20);
    private static final BitField fNested = new BitField(0x40);
    private static final BitField fHasSep = new BitField(0x40);
    protected byte field_1_chHolder;
    protected byte field_2_flt;

    public FLDAbstractType() {

    }

    public static int getSize() {
        return 4 + +1 + 1;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_chHolder = data[offset];
        field_2_flt = data[0x1 + offset];
    }

    public void serialize(byte[] data, int offset) {
        data[offset] = field_1_chHolder;
        data[0x1 + offset] = field_2_flt;
    }

    public String toString() {

        String buffer = "[FLD]\n" +
                "    .chHolder             = " +
                " (" + getChHolder() + " )\n" +
                "         .ch                       = " +
                getCh() + '\n' +
                "         .reserved                 = " +
                getReserved() + '\n' +
                "    .flt                  = " +
                " (" + getFlt() + " )\n" +
                "         .fDiffer                  = " +
                isFDiffer() + '\n' +
                "         .fZombieEmbed             = " +
                isFZombieEmbed() + '\n' +
                "         .fResultDirty             = " +
                isFResultDirty() + '\n' +
                "         .fResultEdited            = " +
                isFResultEdited() + '\n' +
                "         .fLocked                  = " +
                isFLocked() + '\n' +
                "         .fPrivateResult           = " +
                isFPrivateResult() + '\n' +
                "         .fNested                  = " +
                isFNested() + '\n' +
                "         .fHasSep                  = " +
                isFHasSep() + '\n' +
                "[/FLD]\n";
        return buffer;
    }

    public byte getChHolder() {
        return field_1_chHolder;
    }

    public void setChHolder(byte field_1_chHolder) {
        this.field_1_chHolder = field_1_chHolder;
    }

    public byte getFlt() {
        return field_2_flt;
    }

    public void setFlt(byte field_2_flt) {
        this.field_2_flt = field_2_flt;
    }

    public byte getCh() {
        return (byte) ch.getValue(field_1_chHolder);

    }

    public void setCh(byte value) {
        field_1_chHolder = (byte) ch.setValue(field_1_chHolder, value);

    }

    public byte getReserved() {
        return (byte) reserved.getValue(field_1_chHolder);

    }

    public void setReserved(byte value) {
        field_1_chHolder = (byte) reserved.setValue(field_1_chHolder, value);

    }

    public boolean isFDiffer() {
        return fDiffer.isSet(field_2_flt);

    }

    public void setFDiffer(boolean value) {
        field_2_flt = (byte) fDiffer.setBoolean(field_2_flt, value);

    }

    public boolean isFZombieEmbed() {
        return fZombieEmbed.isSet(field_2_flt);

    }

    public void setFZombieEmbed(boolean value) {
        field_2_flt = (byte) fZombieEmbed.setBoolean(field_2_flt, value);

    }

    public boolean isFResultDirty() {
        return fResultDirty.isSet(field_2_flt);

    }

    public void setFResultDirty(boolean value) {
        field_2_flt = (byte) fResultDirty.setBoolean(field_2_flt, value);

    }

    public boolean isFResultEdited() {
        return fResultEdited.isSet(field_2_flt);

    }

    public void setFResultEdited(boolean value) {
        field_2_flt = (byte) fResultEdited.setBoolean(field_2_flt, value);

    }

    public boolean isFLocked() {
        return fLocked.isSet(field_2_flt);

    }

    public void setFLocked(boolean value) {
        field_2_flt = (byte) fLocked.setBoolean(field_2_flt, value);

    }

    public boolean isFPrivateResult() {
        return fPrivateResult.isSet(field_2_flt);

    }

    public void setFPrivateResult(boolean value) {
        field_2_flt = (byte) fPrivateResult.setBoolean(field_2_flt, value);

    }

    public boolean isFNested() {
        return fNested.isSet(field_2_flt);

    }

    public void setFNested(boolean value) {
        field_2_flt = (byte) fNested.setBoolean(field_2_flt, value);

    }

    public boolean isFHasSep() {
        return fHasSep.isSet(field_2_flt);

    }

    public void setFHasSep(boolean value) {
        field_2_flt = (byte) fHasSep.setBoolean(field_2_flt, value);

    }
} 
