package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class DataLabelExtensionRecord extends StandardRecord {
    public static final short sid = 0x086B;

    private static final BitField showSeriesName = BitFieldFactory.getInstance(0x0001);
    private static final BitField showCategoryName = BitFieldFactory.getInstance(0x0002);
    private static final BitField showValue = BitFieldFactory.getInstance(0x0004);
    private static final BitField showPercent = BitFieldFactory.getInstance(0x0008);
    private static final BitField showBubbleSizes = BitFieldFactory.getInstance(0x0010);

    private final int rt;
    private final int grbitFrt;
    private final byte[] unused = new byte[8];
    private final short grbit;
    private final short cchSep;
    private final String rgchSep;

    public DataLabelExtensionRecord(RecordInputStream in) {
        rt = in.readShort();
        grbitFrt = in.readShort();
        in.readFully(unused);

        grbit = in.readShort();
        cchSep = in.readShort();

        byte[] datas = new byte[in.available()];
        in.readFully(datas);

        rgchSep = StringUtil.getFromUnicodeLE(datas);

    }

    @Override
    protected int getDataSize() {
        return 2 + 2 + 8;
    }

    @Override
    public short getSid() {
        return sid;
    }

    public boolean isShowSeriesName() {
        return showSeriesName.isSet(grbit);
    }

    public boolean isShowCategoryName() {
        return showCategoryName.isSet(grbit);
    }

    public boolean isShowValue() {
        return showValue.isSet(grbit);
    }

    public boolean isShowPercent() {
        return showPercent.isSet(grbit);
    }

    public boolean isShowBubbleSizes() {
        return showBubbleSizes.isSet(grbit);
    }

    public String getSeparator() {
        return rgchSep;
    }

    @Override
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(rt);
        out.writeShort(grbitFrt);
        out.write(unused);
    }

    @Override
    public String toString() {

        String buffer = "[DATALABEXT]\n" +
                "    .rt      =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt=" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .unused  =" + HexDump.toHex(unused) + '\n' +
                "[/DATALABEXT]\n";
        return buffer;
    }
}
