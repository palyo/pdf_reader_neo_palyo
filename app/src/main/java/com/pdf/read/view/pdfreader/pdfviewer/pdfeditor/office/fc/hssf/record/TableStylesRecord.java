package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class TableStylesRecord extends StandardRecord {
    public static final short sid = 0x088E;

    private final int rt;
    private final int grbitFrt;
    private final byte[] unused = new byte[8];
    private final int cts;

    private final String rgchDefListStyle;
    private final String rgchDefPivotStyle;

    public TableStylesRecord(RecordInputStream in) {
        rt = in.readUShort();
        grbitFrt = in.readUShort();
        in.readFully(unused);
        cts = in.readInt();
        int cchDefListStyle = in.readUShort();
        int cchDefPivotStyle = in.readUShort();

        rgchDefListStyle = in.readUnicodeLEString(cchDefListStyle);
        rgchDefPivotStyle = in.readUnicodeLEString(cchDefPivotStyle);
    }

    @Override
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(rt);
        out.writeShort(grbitFrt);
        out.write(unused);
        out.writeInt(cts);

        out.writeShort(rgchDefListStyle.length());
        out.writeShort(rgchDefPivotStyle.length());

        StringUtil.putUnicodeLE(rgchDefListStyle, out);
        StringUtil.putUnicodeLE(rgchDefPivotStyle, out);
    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 8 + 4 + 2 + 2
                + (2 * rgchDefListStyle.length()) + (2 * rgchDefPivotStyle.length());
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public String toString() {

        String buffer = "[TABLESTYLES]\n" +
                "    .rt      =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt=" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .unused  =" + HexDump.toHex(unused) + '\n' +
                "    .cts=" + String.valueOf(HexDump.intToHex(cts)) + '\n' +
                "    .rgchDefListStyle=" + rgchDefListStyle + '\n' +
                "    .rgchDefPivotStyle=" + rgchDefPivotStyle + '\n' +
                "[/TABLESTYLES]\n";
        return buffer;
    }
}
