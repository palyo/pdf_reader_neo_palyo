package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.pivottable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class StreamIDRecord extends StandardRecord {
    public static final short sid = 0x00D5;

    private final int idstm;

    public StreamIDRecord(RecordInputStream in) {
        idstm = in.readShort();
    }

    @Override
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(idstm);
    }

    @Override
    protected int getDataSize() {
        return 2;
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public String toString() {

        String buffer = "[SXIDSTM]\n" +
                "    .idstm      =" + String.valueOf(HexDump.shortToHex(idstm)) + '\n' +
                "[/SXIDSTM]\n";
        return buffer;
    }
}
