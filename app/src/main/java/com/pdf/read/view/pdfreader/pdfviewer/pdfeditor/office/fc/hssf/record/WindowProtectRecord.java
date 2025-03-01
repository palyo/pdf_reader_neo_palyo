package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class WindowProtectRecord extends StandardRecord {
    public final static short sid = 0x0019;

    private static final BitField settingsProtectedFlag = BitFieldFactory.getInstance(0x0001);

    private int _options;

    public WindowProtectRecord(int options) {
        _options = options;
    }

    public WindowProtectRecord(RecordInputStream in) {
        this(in.readUShort());
    }

    public WindowProtectRecord(boolean protect) {
        this(0);
        setProtect(protect);
    }

    public boolean getProtect() {
        return settingsProtectedFlag.isSet(_options);
    }

    public void setProtect(boolean protect) {
        _options = settingsProtectedFlag.setBoolean(_options, protect);
    }

    public String toString() {

        String buffer = "[WINDOWPROTECT]\n" +
                "    .options = " + String.valueOf(HexDump.shortToHex(_options)) + "\n" +
                "[/WINDOWPROTECT]\n";
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

    @Override
    public Object clone() {
        return new WindowProtectRecord(_options);
    }
}
