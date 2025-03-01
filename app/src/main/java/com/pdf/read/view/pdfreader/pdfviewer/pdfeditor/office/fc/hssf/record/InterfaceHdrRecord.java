package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class InterfaceHdrRecord extends StandardRecord {
    public final static short sid = 0x00E1;

    public final static int CODEPAGE = 0x04B0;
    private final int _codepage;

    public InterfaceHdrRecord(int codePage) {
        _codepage = codePage;
    }

    public InterfaceHdrRecord(RecordInputStream in) {
        _codepage = in.readShort();
    }

    public String toString() {

        String buffer = "[INTERFACEHDR]\n" +
                "    .codepage = " + String.valueOf(HexDump.shortToHex(_codepage)) + "\n" +
                "[/INTERFACEHDR]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_codepage);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
