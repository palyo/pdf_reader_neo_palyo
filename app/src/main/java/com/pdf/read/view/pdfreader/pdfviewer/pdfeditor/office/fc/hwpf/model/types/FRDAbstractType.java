package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public abstract class FRDAbstractType {

    protected short field_1_nAuto;

    protected FRDAbstractType() {
    }

    public static int getSize() {
        return 2;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_nAuto = LittleEndian.getShort(data, offset);
    }

    public void serialize(byte[] data, int offset) {
        LittleEndian.putShort(data, offset, field_1_nAuto);
    }

    public String toString() {

        String builder = "[FRD]\n" +
                "    .nAuto                = " +
                " (" + getNAuto() + " )\n" +
                "[/FRD]\n";
        return builder;
    }

    public short getNAuto() {
        return field_1_nAuto;
    }

    public void setNAuto(short field_1_nAuto) {
        this.field_1_nAuto = field_1_nAuto;
    }

} 