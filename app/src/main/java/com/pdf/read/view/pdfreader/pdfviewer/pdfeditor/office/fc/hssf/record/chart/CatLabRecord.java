package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CatLabRecord extends StandardRecord {
    public static final short sid = 0x0856;

    private final short rt;
    private final short grbitFrt;
    private final short wOffset;
    private final short at;
    private final short grbit;
    private final Short unused;

    public CatLabRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        wOffset = in.readShort();
        at = in.readShort();
        grbit = in.readShort();

        if (in.available() == 0) {
            unused = null;
        } else {
            unused = in.readShort();
        }
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 2 + 2 + 2 + (unused == null ? 0 : 2);
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public void serialize(LittleEndianOutput out) {
        out.writeShort(rt);
        out.writeShort(grbitFrt);
        out.writeShort(wOffset);
        out.writeShort(at);
        out.writeShort(grbit);
        if (unused != null)
            out.writeShort(unused);
    }

    @Override
    public String toString() {

        String buffer = "[CATLAB]\n" +
                "    .rt      =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt=" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .wOffset =" + String.valueOf(HexDump.shortToHex(wOffset)) + '\n' +
                "    .at      =" + String.valueOf(HexDump.shortToHex(at)) + '\n' +
                "    .grbit   =" + String.valueOf(HexDump.shortToHex(grbit)) + '\n' +
                "    .unused  =" + String.valueOf(HexDump.shortToHex(unused)) + '\n' +
                "[/CATLAB]\n";
        return buffer;
    }
}
