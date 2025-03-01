package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ChartEndBlockRecord extends StandardRecord {
    public static final short sid = 0x0853;

    private short rt;
    private short grbitFrt;
    private short iObjectKind;
    private byte[] unused;

    public ChartEndBlockRecord() {
    }

    public ChartEndBlockRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        iObjectKind = in.readShort();

        // Often, but not always has 6 unused bytes at the end
        if (in.available() == 0) {
            unused = new byte[0];
        } else {
            unused = new byte[6];
            in.readFully(unused);
        }
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 2 + unused.length;
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

        out.write(unused);
    }

    @Override
    public String toString() {

        String buffer = "[ENDBLOCK]\n" +
                "    .rt         =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt   =" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .iObjectKind=" + String.valueOf(HexDump.shortToHex(iObjectKind)) + '\n' +
                "    .unused     =" + HexDump.toHex(unused) + '\n' +
                "[/ENDBLOCK]\n";
        return buffer;
    }

    @Override
    public ChartEndBlockRecord clone() {
        ChartEndBlockRecord record = new ChartEndBlockRecord();

        record.rt = rt;
        record.grbitFrt = grbitFrt;
        record.iObjectKind = iObjectKind;
        record.unused = unused.clone();

        return record;
    }
}
