package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PasswordRev4Record extends StandardRecord {
    public final static short sid = 0x01BC;
    private int field_1_password;

    public PasswordRev4Record(int pw) {
        field_1_password = pw;
    }

    public PasswordRev4Record(RecordInputStream in) {
        field_1_password = in.readShort();
    }

    public void setPassword(short pw) {
        field_1_password = pw;
    }

    public String toString() {

        String buffer = "[PROT4REVPASSWORD]\n" +
                "    .password = " + String.valueOf(HexDump.shortToHex(field_1_password)) + "\n" +
                "[/PROT4REVPASSWORD]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_password);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
