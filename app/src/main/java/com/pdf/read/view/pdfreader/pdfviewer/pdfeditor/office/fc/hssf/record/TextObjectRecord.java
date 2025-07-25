package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.OperandPtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Ptg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecordOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFRichTextString;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;

public final class TextObjectRecord extends ContinuableRecord {
    public final static short sid = 0x01B6;
    public final static short HORIZONTAL_TEXT_ALIGNMENT_LEFT_ALIGNED = 1;
    public final static short HORIZONTAL_TEXT_ALIGNMENT_CENTERED = 2;
    public final static short HORIZONTAL_TEXT_ALIGNMENT_RIGHT_ALIGNED = 3;
    public final static short HORIZONTAL_TEXT_ALIGNMENT_JUSTIFIED = 4;
    public final static short VERTICAL_TEXT_ALIGNMENT_TOP = 1;
    public final static short VERTICAL_TEXT_ALIGNMENT_CENTER = 2;
    public final static short VERTICAL_TEXT_ALIGNMENT_BOTTOM = 3;
    public final static short VERTICAL_TEXT_ALIGNMENT_JUSTIFY = 4;
    public final static short TEXT_ORIENTATION_NONE = 0;
    public final static short TEXT_ORIENTATION_TOP_TO_BOTTOM = 1;
    public final static short TEXT_ORIENTATION_ROT_RIGHT = 2;
    public final static short TEXT_ORIENTATION_ROT_LEFT = 3;
    private static final int FORMAT_RUN_ENCODED_SIZE = 8;
    private static final BitField HorizontalTextAlignment = BitFieldFactory.getInstance(0x000E);
    private static final BitField VerticalTextAlignment = BitFieldFactory.getInstance(0x0070);
    private static final BitField textLocked = BitFieldFactory.getInstance(0x0200);
    private int field_1_options;
    private int field_2_textOrientation;
    private int field_3_reserved4;
    private int field_4_reserved5;
    private int field_5_reserved6;
    private int field_8_reserved7;

    private HSSFRichTextString _text;

    private int _unknownPreFormulaInt;

    private OperandPtg _linkRefPtg;

    private Byte _unknownPostFormulaByte;

    public TextObjectRecord() {

    }

    public TextObjectRecord(RecordInputStream in) {
        field_1_options = in.readUShort();
        field_2_textOrientation = in.readUShort();
        field_3_reserved4 = in.readUShort();
        field_4_reserved5 = in.readUShort();
        field_5_reserved6 = in.readUShort();
        int field_6_textLength = in.readUShort();
        int field_7_formattingDataLength = in.readUShort();
        field_8_reserved7 = in.readInt();

        if (in.remaining() > 0) {

            if (in.remaining() < 11) {
                throw new RecordFormatException("Not enough remaining data for a link formula");
            }
            int formulaSize = in.readUShort();
            _unknownPreFormulaInt = in.readInt();
            Ptg[] ptgs = Ptg.readTokens(formulaSize, in);
            if (ptgs.length != 1) {
                throw new RecordFormatException("Read " + ptgs.length
                        + " tokens but expected exactly 1");
            }
            _linkRefPtg = (OperandPtg) ptgs[0];
            if (in.remaining() > 0) {
                _unknownPostFormulaByte = Byte.valueOf(in.readByte());
            } else {
                _unknownPostFormulaByte = null;
            }
        } else {
            _linkRefPtg = null;
        }
        if (in.remaining() > 0) {
            throw new RecordFormatException("Unused " + in.remaining() + " bytes at end of record");
        }

        String text;
        if (field_6_textLength > 0) {
            text = readRawString(in, field_6_textLength);
        } else {
            text = "";
        }
        _text = new HSSFRichTextString(text);

        if (field_7_formattingDataLength > 0) {
            processFontRuns(in, _text, field_7_formattingDataLength);
        }
    }

    private static String readRawString(RecordInputStream in, int textLength) {
        byte compressByte = in.readByte();
        boolean isCompressed = (compressByte & 0x01) == 0;
        if (isCompressed) {
            return in.readCompressedUnicode(textLength);
        }
        return in.readUnicodeLEString(textLength);
    }

    private static void processFontRuns(RecordInputStream in, HSSFRichTextString str,
                                        int formattingRunDataLength) {
        if (formattingRunDataLength % FORMAT_RUN_ENCODED_SIZE != 0) {
            throw new RecordFormatException("Bad format run data length " + formattingRunDataLength
                    + ")");
        }
        int nRuns = formattingRunDataLength / FORMAT_RUN_ENCODED_SIZE;
        for (int i = 0; i < nRuns; i++) {
            short index = in.readShort();
            short iFont = in.readShort();
            in.readInt();
            str.applyFont(index, str.length(), iFont);
        }
    }

    private static void writeFormatData(ContinuableRecordOutput out, HSSFRichTextString str) {
        int nRuns = str.numFormattingRuns();
        for (int i = 0; i < nRuns; i++) {
            out.writeShort(str.getIndexOfFormattingRun(i));
            int fontIndex = str.getFontOfFormattingRun(i);
            out.writeShort(fontIndex == HSSFRichTextString.NO_FONT ? 0 : fontIndex);
            out.writeInt(0);
        }
        out.writeShort(str.length());
        out.writeShort(0);
        out.writeInt(0);
    }

    public short getSid() {
        return sid;
    }

    private void serializeTXORecord(ContinuableRecordOutput out) {

        out.writeShort(field_1_options);
        out.writeShort(field_2_textOrientation);
        out.writeShort(field_3_reserved4);
        out.writeShort(field_4_reserved5);
        out.writeShort(field_5_reserved6);
        out.writeShort(_text.length());
        out.writeShort(getFormattingDataLength());
        out.writeInt(field_8_reserved7);

        if (_linkRefPtg != null) {
            int formulaSize = _linkRefPtg.getSize();
            out.writeShort(formulaSize);
            out.writeInt(_unknownPreFormulaInt);
            _linkRefPtg.write(out);
            if (_unknownPostFormulaByte != null) {
                out.writeByte(_unknownPostFormulaByte.byteValue());
            }
        }
    }

    private void serializeTrailingRecords(ContinuableRecordOutput out) {
        out.writeContinue();
        out.writeStringData(_text.getString());
        out.writeContinue();
        writeFormatData(out, _text);
    }

    protected void serialize(ContinuableRecordOutput out) {

        serializeTXORecord(out);
        if (_text.getString().length() > 0) {
            serializeTrailingRecords(out);
        }
    }

    private int getFormattingDataLength() {
        if (_text.length() < 1) {

            return 0;
        }
        return (_text.numFormattingRuns() + 1) * FORMAT_RUN_ENCODED_SIZE;
    }

    public int getHorizontalTextAlignment() {
        return HorizontalTextAlignment.getValue(field_1_options);
    }

    public void setHorizontalTextAlignment(int value) {
        field_1_options = HorizontalTextAlignment.setValue(field_1_options, value);
    }

    public int getVerticalTextAlignment() {
        return VerticalTextAlignment.getValue(field_1_options);
    }

    public void setVerticalTextAlignment(int value) {
        field_1_options = VerticalTextAlignment.setValue(field_1_options, value);
    }

    public boolean isTextLocked() {
        return textLocked.isSet(field_1_options);
    }

    public void setTextLocked(boolean value) {
        field_1_options = textLocked.setBoolean(field_1_options, value);
    }

    public int getTextOrientation() {
        return field_2_textOrientation;
    }

    public void setTextOrientation(int textOrientation) {
        this.field_2_textOrientation = textOrientation;
    }

    public HSSFRichTextString getStr() {
        return _text;
    }

    public void setStr(HSSFRichTextString str) {
        _text = str;
    }

    public Ptg getLinkRefPtg() {
        return _linkRefPtg;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("[TXO]\n");
        sb.append("    .options        = ").append(HexDump.shortToHex(field_1_options)).append("\n");
        sb.append("         .isHorizontal = ").append(getHorizontalTextAlignment()).append('\n');
        sb.append("         .isVertical   = ").append(getVerticalTextAlignment()).append('\n');
        sb.append("         .textLocked   = ").append(isTextLocked()).append('\n');
        sb.append("    .textOrientation= ").append(HexDump.shortToHex(getTextOrientation())).append("\n");
        sb.append("    .reserved4      = ").append(HexDump.shortToHex(field_3_reserved4)).append("\n");
        sb.append("    .reserved5      = ").append(HexDump.shortToHex(field_4_reserved5)).append("\n");
        sb.append("    .reserved6      = ").append(HexDump.shortToHex(field_5_reserved6)).append("\n");
        sb.append("    .textLength     = ").append(HexDump.shortToHex(_text.length())).append("\n");
        sb.append("    .reserved7      = ").append(HexDump.intToHex(field_8_reserved7)).append("\n");

        sb.append("    .string = ").append(_text).append('\n');

        for (int i = 0; i < _text.numFormattingRuns(); i++) {
            sb.append("    .textrun = ").append(_text.getFontOfFormattingRun(i)).append('\n');

        }
        sb.append("[/TXO]\n");
        return sb.toString();
    }

    public Object clone() {

        TextObjectRecord rec = new TextObjectRecord();
        rec._text = _text;

        rec.field_1_options = field_1_options;
        rec.field_2_textOrientation = field_2_textOrientation;
        rec.field_3_reserved4 = field_3_reserved4;
        rec.field_4_reserved5 = field_4_reserved5;
        rec.field_5_reserved6 = field_5_reserved6;
        rec.field_8_reserved7 = field_8_reserved7;

        rec._text = _text;

        if (_linkRefPtg != null) {
            rec._unknownPreFormulaInt = _unknownPreFormulaInt;
            rec._linkRefPtg = _linkRefPtg.copy();
            rec._unknownPostFormulaByte = _unknownPostFormulaByte;
        }
        return rec;
    }
}
