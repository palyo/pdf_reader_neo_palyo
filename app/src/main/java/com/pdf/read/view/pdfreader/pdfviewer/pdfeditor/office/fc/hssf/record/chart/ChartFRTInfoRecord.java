package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ChartFRTInfoRecord extends StandardRecord {
    public static final short sid = 0x850;

    private final short rt;
    private final short grbitFrt;
    private final byte verOriginator;
    private final byte verWriter;
    private final CFRTID[] rgCFRTID;

    public ChartFRTInfoRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        verOriginator = in.readByte();
        verWriter = in.readByte();
        int cCFRTID = in.readShort();

        rgCFRTID = new CFRTID[cCFRTID];
        for (int i = 0; i < cCFRTID; i++) {
            rgCFRTID[i] = new CFRTID(in);
        }
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 1 + 1 + 2 + rgCFRTID.length * CFRTID.ENCODED_SIZE;
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public void serialize(LittleEndianOutput out) {

        out.writeShort(rt);
        out.writeShort(grbitFrt);
        out.writeByte(verOriginator);
        out.writeByte(verWriter);
        int nCFRTIDs = rgCFRTID.length;
        out.writeShort(nCFRTIDs);

        for (int i = 0; i < nCFRTIDs; i++) {
            rgCFRTID[i].serialize(out);
        }
    }

    @Override
    public String toString() {

        String buffer = "[CHARTFRTINFO]\n" +
                "    .rt           =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt     =" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .verOriginator=" + String.valueOf(HexDump.byteToHex(verOriginator)) + '\n' +
                "    .verWriter    =" + String.valueOf(HexDump.byteToHex(verOriginator)) + '\n' +
                "    .nCFRTIDs     =" + String.valueOf(HexDump.shortToHex(rgCFRTID.length)) + '\n' +
                "[/CHARTFRTINFO]\n";
        return buffer;
    }

    private static final class CFRTID {
        public static final int ENCODED_SIZE = 4;
        private final int rtFirst;
        private final int rtLast;

        public CFRTID(LittleEndianInput in) {
            rtFirst = in.readShort();
            rtLast = in.readShort();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(rtFirst);
            out.writeShort(rtLast);
        }
    }
}
