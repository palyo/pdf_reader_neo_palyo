package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FtCblsSubRecord extends SubRecord {
    public final static short sid = 0x0C;
    private static final int ENCODED_SIZE = 20;

    private byte[] reserved;

    public FtCblsSubRecord() {
        reserved = new byte[ENCODED_SIZE];
    }

    public FtCblsSubRecord(LittleEndianInput in, int size) {
        if (size != ENCODED_SIZE) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }

        byte[] buf = new byte[size];
        in.readFully(buf);
        reserved = buf;
    }

    public String toString() {

        String buffer = "[FtCbls ]" + "\n" +
                "  size     = " + getDataSize() + "\n" +
                "  reserved = " + HexDump.toHex(reserved) + "\n" +
                "[/FtCbls ]" + "\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(sid);
        out.writeShort(reserved.length);
        out.write(reserved);
    }

    protected int getDataSize() {
        return reserved.length;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        FtCblsSubRecord rec = new FtCblsSubRecord();
        byte[] recdata = new byte[reserved.length];
        System.arraycopy(reserved, 0, recdata, 0, recdata.length);
        rec.reserved = recdata;
        return rec;
    }

}
