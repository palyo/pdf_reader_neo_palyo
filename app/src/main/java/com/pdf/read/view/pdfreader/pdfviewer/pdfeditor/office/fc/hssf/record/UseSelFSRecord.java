package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class UseSelFSRecord extends StandardRecord {
    public final static short sid = 0x0160;

    private static final BitField useNaturalLanguageFormulasFlag = BitFieldFactory.getInstance(0x0001);

    private int _options;

    private UseSelFSRecord(int options) {
        _options = options;
    }

    public UseSelFSRecord(RecordInputStream in) {
        this(in.readUShort());
    }

    public UseSelFSRecord(boolean b) {
        this(0);
        _options = useNaturalLanguageFormulasFlag.setBoolean(_options, b);
    }

    public String toString() {

        String buffer = "[USESELFS]\n" +
                "    .options = " + String.valueOf(HexDump.shortToHex(_options)) + "\n" +
                "[/USESELFS]\n";
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
        return new UseSelFSRecord(_options);
    }
}
