package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class NameCommentRecord extends StandardRecord {
    public final static short sid = 0x0894;

    private final short field_1_record_type;
    private final short field_2_frt_cell_ref_flag;
    private final long field_3_reserved;

    private String field_6_name_text;
    private String field_7_comment_text;

    public NameCommentRecord(final String name, final String comment) {
        field_1_record_type = 0;
        field_2_frt_cell_ref_flag = 0;
        field_3_reserved = 0;
        field_6_name_text = name;
        field_7_comment_text = comment;
    }

    public NameCommentRecord(final RecordInputStream ris) {
        final LittleEndianInput in = ris;
        field_1_record_type = in.readShort();
        field_2_frt_cell_ref_flag = in.readShort();
        field_3_reserved = in.readLong();
        final int field_4_name_length = in.readShort();
        final int field_5_comment_length = in.readShort();

        in.readByte();
        field_6_name_text = StringUtil.readCompressedUnicode(in, field_4_name_length);
        in.readByte();
        field_7_comment_text = StringUtil.readCompressedUnicode(in, field_5_comment_length);
    }

    @Override
    public void serialize(final LittleEndianOutput out) {
        final int field_4_name_length = field_6_name_text.length();
        final int field_5_comment_length = field_7_comment_text.length();

        out.writeShort(field_1_record_type);
        out.writeShort(field_2_frt_cell_ref_flag);
        out.writeLong(field_3_reserved);
        out.writeShort(field_4_name_length);
        out.writeShort(field_5_comment_length);

        out.writeByte(0);
        StringUtil.putCompressedUnicode(field_6_name_text, out);
        out.writeByte(0);
        StringUtil.putCompressedUnicode(field_7_comment_text, out);
    }

    @Override
    protected int getDataSize() {
        return 18
                + field_6_name_text.length()
                + field_7_comment_text.length();
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public String toString() {

        String sb = "[NAMECMT]\n" +
                "    .record type            = " + String.valueOf(HexDump.shortToHex(field_1_record_type)) + "\n" +
                "    .frt cell ref flag      = " + String.valueOf(HexDump.byteToHex(field_2_frt_cell_ref_flag)) + "\n" +
                "    .reserved               = " + field_3_reserved + "\n" +
                "    .name length            = " + field_6_name_text.length() + "\n" +
                "    .comment length         = " + field_7_comment_text.length() + "\n" +
                "    .name                   = " + field_6_name_text + "\n" +
                "    .comment                = " + field_7_comment_text + "\n" +
                "[/NAMECMT]\n";

        return sb;
    }

    public String getNameText() {
        return field_6_name_text;
    }

    public void setNameText(String newName) {
        field_6_name_text = newName;
    }

    public String getCommentText() {
        return field_7_comment_text;
    }

    public void setCommentText(String comment) {
        field_7_comment_text = comment;
    }

    public short getRecordType() {
        return field_1_record_type;
    }

}
