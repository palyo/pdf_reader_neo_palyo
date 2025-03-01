package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.pivottable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class DataItemRecord extends StandardRecord {
    public static final short sid = 0x00C5;

    private final int isxvdData;
    private final int iiftab;
    private final int df;
    private final int isxvd;
    private final int isxvi;
    private final int ifmt;
    private final String name;

    public DataItemRecord(RecordInputStream in) {
        isxvdData = in.readUShort();
        iiftab = in.readUShort();
        df = in.readUShort();
        isxvd = in.readUShort();
        isxvi = in.readUShort();
        ifmt = in.readUShort();

        name = in.readString();
    }

    @Override
    protected void serialize(LittleEndianOutput out) {

        out.writeShort(isxvdData);
        out.writeShort(iiftab);
        out.writeShort(df);
        out.writeShort(isxvd);
        out.writeShort(isxvi);
        out.writeShort(ifmt);

        StringUtil.writeUnicodeString(out, name);
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 2 + 2 + 2 + 2 + StringUtil.getEncodedSize(name);
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public String toString() {

        String buffer = "[SXDI]\n" +
                "  .isxvdData = " + String.valueOf(HexDump.shortToHex(isxvdData)) + "\n" +
                "  .iiftab = " + String.valueOf(HexDump.shortToHex(iiftab)) + "\n" +
                "  .df = " + String.valueOf(HexDump.shortToHex(df)) + "\n" +
                "  .isxvd = " + String.valueOf(HexDump.shortToHex(isxvd)) + "\n" +
                "  .isxvi = " + String.valueOf(HexDump.shortToHex(isxvi)) + "\n" +
                "  .ifmt = " + String.valueOf(HexDump.shortToHex(ifmt)) + "\n" +
                "[/SXDI]\n";
        return buffer;
    }
}
