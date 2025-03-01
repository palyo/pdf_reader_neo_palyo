package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class BackupRecord
        extends StandardRecord {
    public final static short sid = 0x40;
    private short field_1_backup;

    public BackupRecord() {
    }

    public BackupRecord(RecordInputStream in) {
        field_1_backup = in.readShort();
    }

    public short getBackup() {
        return field_1_backup;
    }

    public void setBackup(short backup) {
        field_1_backup = backup;
    }

    public String toString() {

        String buffer = "[BACKUP]\n" +
                "    .backup          = " +
                Integer.toHexString(getBackup()) + "\n" +
                "[/BACKUP]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getBackup());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
