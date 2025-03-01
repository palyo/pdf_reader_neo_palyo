package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CodepageRecord
        extends StandardRecord {
    public final static short sid = 0x42;

    private short field_1_codepage;

    public CodepageRecord() {
    }

    public CodepageRecord(RecordInputStream in) {
        field_1_codepage = in.readShort();
    }

    public short getCodepage() {
        return field_1_codepage;
    }

    public void setCodepage(short cp) {
        field_1_codepage = cp;
    }

    public String toString() {

        String buffer = "[CODEPAGE]\n" +
                "    .codepage        = " +
                Integer.toHexString(getCodepage()) + "\n" +
                "[/CODEPAGE]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCodepage());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
