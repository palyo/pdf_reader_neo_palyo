package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ProtectRecord extends StandardRecord {
    public final static short sid = 0x0012;

    private static final BitField protectFlag = BitFieldFactory.getInstance(0x0001);

    private int _options;

    private ProtectRecord(int options) {
        _options = options;
    }

    public ProtectRecord(boolean isProtected) {
        this(0);
        setProtect(isProtected);
    }

    public ProtectRecord(RecordInputStream in) {
        this(in.readShort());
    }

    public boolean getProtect() {
        return protectFlag.isSet(_options);
    }

    public void setProtect(boolean protect) {
        _options = protectFlag.setBoolean(_options, protect);
    }

    public String toString() {

        String buffer = "[PROTECT]\n" +
                "    .options = " + String.valueOf(HexDump.shortToHex(_options)) + "\n" +
                "[/PROTECT]\n";
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

    public Object clone() {
        return new ProtectRecord(_options);
    }
}
