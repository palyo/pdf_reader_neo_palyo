package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class SeriesTextRecord extends StandardRecord {
    public final static short sid = 0x100D;

    private static final int MAX_LEN = 0xFF;
    private int field_1_id;
    private boolean is16bit;
    private String field_4_text;

    public SeriesTextRecord() {
        field_4_text = "";
        is16bit = false;
    }

    public SeriesTextRecord(RecordInputStream in) {
        field_1_id = in.readUShort();
        int field_2_textLength = in.readUByte();
        is16bit = (in.readUByte() & 0x01) != 0;
        if (is16bit) {
            field_4_text = in.readUnicodeLEString(field_2_textLength);
        } else {
            field_4_text = in.readCompressedUnicode(field_2_textLength);
        }
    }

    public String toString() {

        String sb = "[SERIESTEXT]\n" +
                "  .id     =" + String.valueOf(HexDump.shortToHex(getId())) + '\n' +
                "  .textLen=" + field_4_text.length() + '\n' +
                "  .is16bit=" + is16bit + '\n' +
                "  .text   =" + " (" + getText() + " )" + '\n' +
                "[/SERIESTEXT]\n";
        return sb;
    }

    public void serialize(LittleEndianOutput out) {

        out.writeShort(field_1_id);
        out.writeByte(field_4_text.length());
        if (is16bit) {

            out.writeByte(0x01);
            StringUtil.putUnicodeLE(field_4_text, out);
        } else {

            out.writeByte(0x00);
            StringUtil.putCompressedUnicode(field_4_text, out);
        }
    }

    protected int getDataSize() {
        return 2 + 1 + 1 + field_4_text.length() * (is16bit ? 2 : 1);
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SeriesTextRecord rec = new SeriesTextRecord();

        rec.field_1_id = field_1_id;
        rec.is16bit = is16bit;
        rec.field_4_text = field_4_text;
        return rec;
    }

    public int getId() {
        return field_1_id;
    }

    public void setId(int id) {
        field_1_id = id;
    }

    public String getText() {
        return field_4_text;
    }

    public void setText(String text) {
        if (text.length() > MAX_LEN) {
            throw new IllegalArgumentException("Text is too long ("
                    + text.length() + ">" + MAX_LEN + ")");
        }
        field_4_text = text;
        is16bit = StringUtil.hasMultibyte(text);
    }
}
