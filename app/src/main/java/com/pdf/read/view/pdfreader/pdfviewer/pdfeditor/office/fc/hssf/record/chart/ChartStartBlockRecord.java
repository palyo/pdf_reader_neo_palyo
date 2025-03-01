package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ChartStartBlockRecord extends StandardRecord {
    public static final short sid = 0x0852;

    private short rt;
    private short grbitFrt;
    private short iObjectKind;
    private short iObjectContext;
    private short iObjectInstance1;
    private short iObjectInstance2;

    public ChartStartBlockRecord() {
    }

    public ChartStartBlockRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        iObjectKind = in.readShort();
        iObjectContext = in.readShort();
        iObjectInstance1 = in.readShort();
        iObjectInstance2 = in.readShort();
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 2 + 2 + 2 + 2;
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
        out.writeShort(iObjectContext);
        out.writeShort(iObjectInstance1);
        out.writeShort(iObjectInstance2);
    }

    public String toString() {

        String buffer = "[STARTBLOCK]\n" +
                "    .rt              =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt        =" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .iObjectKind     =" + String.valueOf(HexDump.shortToHex(iObjectKind)) + '\n' +
                "    .iObjectContext  =" + String.valueOf(HexDump.shortToHex(iObjectContext)) + '\n' +
                "    .iObjectInstance1=" + String.valueOf(HexDump.shortToHex(iObjectInstance1)) + '\n' +
                "    .iObjectInstance2=" + String.valueOf(HexDump.shortToHex(iObjectInstance2)) + '\n' +
                "[/STARTBLOCK]\n";
        return buffer;
    }

    @Override
    public ChartStartBlockRecord clone() {
        ChartStartBlockRecord record = new ChartStartBlockRecord();

        record.rt = rt;
        record.grbitFrt = grbitFrt;
        record.iObjectKind = iObjectKind;
        record.iObjectContext = iObjectContext;
        record.iObjectInstance1 = iObjectInstance1;
        record.iObjectInstance2 = iObjectInstance2;

        return record;
    }
}
