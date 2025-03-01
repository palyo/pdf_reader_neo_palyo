package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ChartEndObjectRecord extends StandardRecord {
    public static final short sid = 0x0855;

    private final short rt;
    private final short grbitFrt;
    private final short iObjectKind;
    private final byte[] reserved;

    public ChartEndObjectRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        iObjectKind = in.readShort();

        reserved = new byte[6];
        if (in.available() == 0) {

        } else {

            in.readFully(reserved);
        }
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 2 + 6;
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public void serialize(LittleEndianOutput out) {
        out.writeShort(rt);
        out.writeShort(grbitFrt);
        out.writeShort(iObjectKind);

        out.write(reserved);
    }

    @Override
    public String toString() {

        String buffer = "[ENDOBJECT]\n" +
                "    .rt         =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt   =" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .iObjectKind=" + String.valueOf(HexDump.shortToHex(iObjectKind)) + '\n' +
                "    .reserved   =" + HexDump.toHex(reserved) + '\n' +
                "[/ENDOBJECT]\n";
        return buffer;
    }
}
