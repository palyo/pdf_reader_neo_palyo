package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ProtectionRev4Record extends StandardRecord {
    public final static short sid = 0x01AF;

    private static final BitField protectedFlag = BitFieldFactory.getInstance(0x0001);

    private int _options;

    private ProtectionRev4Record(int options) {
        _options = options;
    }

    public ProtectionRev4Record(boolean protect) {
        this(0);
        setProtect(protect);
    }

    public ProtectionRev4Record(RecordInputStream in) {
        this(in.readUShort());
    }

    public boolean getProtect() {
        return protectedFlag.isSet(_options);
    }

    public void setProtect(boolean protect) {
        _options = protectedFlag.setBoolean(_options, protect);
    }

    public String toString() {

        String buffer = "[PROT4REV]\n" +
                "    .options = " + String.valueOf(HexDump.shortToHex(_options)) + "\n" +
                "[/PROT4REV]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_options);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
