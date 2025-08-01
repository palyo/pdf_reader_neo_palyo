package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.WorkbookUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class BoundSheetRecord extends StandardRecord {
    public final static short sid = 0x0085;

    private static final BitField hiddenFlag = BitFieldFactory.getInstance(0x01);
    private static final BitField veryHiddenFlag = BitFieldFactory.getInstance(0x02);
    private static final Comparator<BoundSheetRecord> BOFComparator = new Comparator<BoundSheetRecord>() {

        public int compare(BoundSheetRecord bsr1, BoundSheetRecord bsr2) {
            return bsr1.getPositionOfBof() - bsr2.getPositionOfBof();
        }
    };
    private int field_1_position_of_BOF;
    private int field_2_option_flags;
    private int field_4_isMultibyteUnicode;
    private String field_5_sheetname;

    public BoundSheetRecord(String sheetname) {
        field_2_option_flags = 0;
        setSheetname(sheetname);
    }

    public BoundSheetRecord(RecordInputStream in) {
        field_1_position_of_BOF = in.readInt();
        field_2_option_flags = in.readUShort();
        int field_3_sheetname_length = in.readUByte();
        field_4_isMultibyteUnicode = in.readByte();

        if (isMultibyte()) {
            field_5_sheetname = in.readUnicodeLEString(field_3_sheetname_length);
        } else {
            field_5_sheetname = in.readCompressedUnicode(field_3_sheetname_length);
        }
    }

    public static BoundSheetRecord[] orderByBofPosition(List<BoundSheetRecord> boundSheetRecords) {
        BoundSheetRecord[] bsrs = new BoundSheetRecord[boundSheetRecords.size()];
        boundSheetRecords.toArray(bsrs);
        Arrays.sort(bsrs, BOFComparator);
        return bsrs;
    }

    public int getPositionOfBof() {
        return field_1_position_of_BOF;
    }

    public void setPositionOfBof(int pos) {
        field_1_position_of_BOF = pos;
    }

    private boolean isMultibyte() {
        return (field_4_isMultibyteUnicode & 0x01) != 0;
    }

    public String getSheetname() {
        return field_5_sheetname;
    }

    public void setSheetname(String sheetName) {

        WorkbookUtil.validateSheetName(sheetName);
        field_5_sheetname = sheetName;
        field_4_isMultibyteUnicode = StringUtil.hasMultibyte(sheetName) ? 1 : 0;
    }

    public String toString() {

        String buffer = "[BOUNDSHEET]\n" +
                "    .bof        = " + String.valueOf(HexDump.intToHex(getPositionOfBof())) + "\n" +
                "    .options    = " + String.valueOf(HexDump.shortToHex(field_2_option_flags)) + "\n" +
                "    .unicodeflag= " + String.valueOf(HexDump.byteToHex(field_4_isMultibyteUnicode)) + "\n" +
                "    .sheetname  = " + field_5_sheetname + "\n" +
                "[/BOUNDSHEET]\n";
        return buffer;
    }

    protected int getDataSize() {
        return 8 + field_5_sheetname.length() * (isMultibyte() ? 2 : 1);
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(getPositionOfBof());
        out.writeShort(field_2_option_flags);

        String name = field_5_sheetname;
        out.writeByte(name.length());
        out.writeByte(field_4_isMultibyteUnicode);

        if (isMultibyte()) {
            StringUtil.putUnicodeLE(name, out);
        } else {
            StringUtil.putCompressedUnicode(name, out);
        }
    }

    public short getSid() {
        return sid;
    }

    public boolean isHidden() {
        return hiddenFlag.isSet(field_2_option_flags);
    }

    public void setHidden(boolean hidden) {
        field_2_option_flags = hiddenFlag.setBoolean(field_2_option_flags, hidden);
    }

    public boolean isVeryHidden() {
        return veryHiddenFlag.isSet(field_2_option_flags);
    }

    public void setVeryHidden(boolean veryHidden) {
        field_2_option_flags = veryHiddenFlag.setBoolean(field_2_option_flags, veryHidden);
    }
}
