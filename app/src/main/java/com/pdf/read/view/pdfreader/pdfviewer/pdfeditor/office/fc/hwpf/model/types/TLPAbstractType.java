package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.HDFType;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public abstract class TLPAbstractType implements HDFType {

    private static final BitField fBorders = new BitField(0x0001);
    private static final BitField fShading = new BitField(0x0002);
    private static final BitField fFont = new BitField(0x0004);
    private static final BitField fColor = new BitField(0x0008);
    private static final BitField fBestFit = new BitField(0x0010);
    private static final BitField fHdrRows = new BitField(0x0020);
    private static final BitField fLastRow = new BitField(0x0040);
    protected short field_1_itl;
    protected byte field_2_tlp_flags;

    public TLPAbstractType() {

    }

    protected void fillFields(byte[] data, int offset) {
        field_1_itl = LittleEndian.getShort(data, offset);
        field_2_tlp_flags = data[0x2 + offset];
    }

    public void serialize(byte[] data, int offset) {
        LittleEndian.putShort(data, offset, field_1_itl);
        data[0x2 + offset] = field_2_tlp_flags;
    }

    public String toString() {

        String buffer = "[TLP]\n" +
                "    .itl                  = " +
                " (" + getItl() + " )\n" +
                "    .tlp_flags            = " +
                " (" + getTlp_flags() + " )\n" +
                "         .fBorders                 = " +
                isFBorders() + '\n' +
                "         .fShading                 = " +
                isFShading() + '\n' +
                "         .fFont                    = " +
                isFFont() + '\n' +
                "         .fColor                   = " +
                isFColor() + '\n' +
                "         .fBestFit                 = " +
                isFBestFit() + '\n' +
                "         .fHdrRows                 = " +
                isFHdrRows() + '\n' +
                "         .fLastRow                 = " +
                isFLastRow() + '\n' +
                "[/TLP]\n";
        return buffer;
    }

    public int getSize() {
        return 4 + +2 + 1;
    }

    public short getItl() {
        return field_1_itl;
    }

    public void setItl(short field_1_itl) {
        this.field_1_itl = field_1_itl;
    }

    public byte getTlp_flags() {
        return field_2_tlp_flags;
    }

    public void setTlp_flags(byte field_2_tlp_flags) {
        this.field_2_tlp_flags = field_2_tlp_flags;
    }

    public boolean isFBorders() {
        return fBorders.isSet(field_2_tlp_flags);

    }

    public void setFBorders(boolean value) {
        field_2_tlp_flags = (byte) fBorders.setBoolean(field_2_tlp_flags,
                value);

    }

    public boolean isFShading() {
        return fShading.isSet(field_2_tlp_flags);

    }

    public void setFShading(boolean value) {
        field_2_tlp_flags = (byte) fShading.setBoolean(field_2_tlp_flags,
                value);

    }

    public boolean isFFont() {
        return fFont.isSet(field_2_tlp_flags);

    }

    public void setFFont(boolean value) {
        field_2_tlp_flags = (byte) fFont.setBoolean(field_2_tlp_flags, value);

    }

    public boolean isFColor() {
        return fColor.isSet(field_2_tlp_flags);

    }

    public void setFColor(boolean value) {
        field_2_tlp_flags = (byte) fColor.setBoolean(field_2_tlp_flags, value);

    }

    public boolean isFBestFit() {
        return fBestFit.isSet(field_2_tlp_flags);

    }

    public void setFBestFit(boolean value) {
        field_2_tlp_flags = (byte) fBestFit.setBoolean(field_2_tlp_flags,
                value);

    }

    public boolean isFHdrRows() {
        return fHdrRows.isSet(field_2_tlp_flags);

    }

    public void setFHdrRows(boolean value) {
        field_2_tlp_flags = (byte) fHdrRows.setBoolean(field_2_tlp_flags,
                value);

    }

    public boolean isFLastRow() {
        return fLastRow.isSet(field_2_tlp_flags);

    }

    public void setFLastRow(boolean value) {
        field_2_tlp_flags = (byte) fLastRow.setBoolean(field_2_tlp_flags,
                value);

    }

} 
