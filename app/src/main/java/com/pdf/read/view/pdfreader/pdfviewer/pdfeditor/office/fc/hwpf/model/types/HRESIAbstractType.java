package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public abstract class HRESIAbstractType {

    public final static byte HRES_NO = 0;
    public final static byte HRES_NORMAL = 1;
    public final static byte HRES_ADD_LETTER_BEFORE = 2;
    public final static byte HRES_CHANGE_LETTER_BEFORE = 3;
    public final static byte HRES_DELETE_LETTER_BEFORE = 4;
    public final static byte HRES_CHANGE_LETTER_AFTER = 5;
    public final static byte HRES_DELETE_BEFORE_CHANGE_BEFORE = 6;
    protected byte field_1_hres;
    protected byte field_2_chHres;

    protected HRESIAbstractType() {
    }

    public static int getSize() {
        return 4 + +1 + 1;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_hres = data[offset];
        field_2_chHres = data[0x1 + offset];
    }

    public void serialize(byte[] data, int offset) {
        data[offset] = field_1_hres;
        data[0x1 + offset] = field_2_chHres;
    }

    public String toString() {

        String builder = "[HRESI]\n" +
                "    .hres                 = " +
                " (" + getHres() + " )\n" +
                "    .chHres               = " +
                " (" + getChHres() + " )\n" +
                "[/HRESI]\n";
        return builder;
    }

    public byte getHres() {
        return field_1_hres;
    }

    public void setHres(byte field_1_hres) {
        this.field_1_hres = field_1_hres;
    }

    public byte getChHres() {
        return field_2_chHres;
    }

    public void setChHres(byte field_2_chHres) {
        this.field_2_chHres = field_2_chHres;
    }

}  