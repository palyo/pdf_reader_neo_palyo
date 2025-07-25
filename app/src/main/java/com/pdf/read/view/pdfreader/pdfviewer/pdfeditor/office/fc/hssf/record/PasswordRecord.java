package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PasswordRecord extends StandardRecord {
    public final static short sid = 0x0013;
    private int field_1_password;

    public PasswordRecord(int password) {
        field_1_password = password;
    }

    public PasswordRecord(RecordInputStream in) {
        field_1_password = in.readShort();
    }

    public static short hashPassword(String password) {
        byte[] passwordCharacters = password.getBytes();
        int hash = 0;
        if (passwordCharacters.length > 0) {
            int charIndex = passwordCharacters.length;
            while (charIndex-- > 0) {
                hash = ((hash >> 14) & 0x01) | ((hash << 1) & 0x7fff);
                hash ^= passwordCharacters[charIndex];
            }

            hash = ((hash >> 14) & 0x01) | ((hash << 1) & 0x7fff);
            hash ^= passwordCharacters.length;
            hash ^= (0x8000 | ('N' << 8) | 'K');
        }
        return (short) hash;
    }

    public int getPassword() {
        return field_1_password;
    }

    public void setPassword(int password) {
        field_1_password = password;
    }

    public String toString() {

        String buffer = "[PASSWORD]\n" +
                "    .password = " + String.valueOf(HexDump.shortToHex(field_1_password)) + "\n" +
                "[/PASSWORD]\n";
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

    public Object clone() {
        return new PasswordRecord(field_1_password);
    }
}
