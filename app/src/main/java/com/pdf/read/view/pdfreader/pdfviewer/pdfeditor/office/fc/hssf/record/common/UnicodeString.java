package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecordInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecordOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public class UnicodeString implements Comparable<UnicodeString> {
    private static final BitField highByte = BitFieldFactory.getInstance(0x1);

    private static final BitField extBit = BitFieldFactory.getInstance(0x4);
    private static final BitField richText = BitFieldFactory.getInstance(0x8);
    private static final POILogger _logger = POILogFactory.getLogger(UnicodeString.class);
    private short field_1_charCount;
    private byte field_2_optionflags;
    private String field_3_string;
    private List<FormatRun> field_4_format_runs;
    private ExtRst field_5_ext_rst;

    private UnicodeString() {
    }

    public UnicodeString(String str) {
        setString(str);
    }

    public UnicodeString(RecordInputStream in) {
        field_1_charCount = in.readShort();
        field_2_optionflags = in.readByte();

        int runCount = 0;
        int extensionLength = 0;
        if (isRichText()) {
            runCount = in.readShort();
        }
        if (isExtendedText()) {
            extensionLength = in.readInt();
        }

        boolean isCompressed = ((field_2_optionflags & 1) == 0);
        if (isCompressed) {
            field_3_string = in.readCompressedUnicode(getCharCount());
        } else {
            field_3_string = in.readUnicodeLEString(getCharCount());
        }

        if (isRichText() && (runCount > 0)) {
            field_4_format_runs = new ArrayList<FormatRun>(runCount);
            for (int i = 0; i < runCount; i++) {
                field_4_format_runs.add(new FormatRun(in));
            }
        }

        if (isExtendedText() && (extensionLength > 0)) {
            field_5_ext_rst = new ExtRst(new ContinuableRecordInput(in), extensionLength);
            if (field_5_ext_rst.getDataSize() + 4 != extensionLength) {
                _logger.log(POILogger.WARN, "ExtRst was supposed to be " + extensionLength + " bytes long, but seems to actually be " + (field_5_ext_rst.getDataSize() + 4));
            }
        }
    }

    public int hashCode() {
        int stringHash = 0;
        if (field_3_string != null)
            stringHash = field_3_string.hashCode();
        return field_1_charCount + stringHash;
    }

    public boolean equals(Object o) {
        if (!(o instanceof UnicodeString)) {
            return false;
        }
        UnicodeString other = (UnicodeString) o;

        boolean eq = ((field_1_charCount == other.field_1_charCount)
                && (field_2_optionflags == other.field_2_optionflags)
                && field_3_string.equals(other.field_3_string));
        if (!eq) return false;

        if ((field_4_format_runs == null) && (other.field_4_format_runs == null))

            return true;
        if (((field_4_format_runs == null) && (other.field_4_format_runs != null)) ||
                (field_4_format_runs != null) && (other.field_4_format_runs == null))

            return false;

        int size = field_4_format_runs.size();
        if (size != other.field_4_format_runs.size())
            return false;

        for (int i = 0; i < size; i++) {
            FormatRun run1 = field_4_format_runs.get(i);
            FormatRun run2 = other.field_4_format_runs.get(i);

            if (!run1.equals(run2))
                return false;
        }

        if (field_5_ext_rst == null && other.field_5_ext_rst == null) {

        } else if (field_5_ext_rst != null && other.field_5_ext_rst != null) {
            int extCmp = field_5_ext_rst.compareTo(other.field_5_ext_rst);
            if (extCmp == 0) {

            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public int getCharCount() {
        if (field_1_charCount < 0) {
            return field_1_charCount + 65536;
        }
        return field_1_charCount;
    }

    public void setCharCount(short cc) {
        field_1_charCount = cc;
    }

    public short getCharCountShort() {
        return field_1_charCount;
    }

    public byte getOptionFlags() {
        return field_2_optionflags;
    }

    public void setOptionFlags(byte of) {
        field_2_optionflags = of;
    }

    public String getString() {
        return field_3_string;
    }

    public void setString(String string) {
        field_3_string = string;
        setCharCount((short) field_3_string.length());

        boolean useUTF16 = false;
        int strlen = string.length();

        for (int j = 0; j < strlen; j++) {
            if (string.charAt(j) > 255) {
                useUTF16 = true;
                break;
            }
        }
        if (useUTF16)

            field_2_optionflags = highByte.setByte(field_2_optionflags);
        else field_2_optionflags = highByte.clearByte(field_2_optionflags);
    }

    public int getFormatRunCount() {
        if (field_4_format_runs == null)
            return 0;
        return field_4_format_runs.size();
    }

    public FormatRun getFormatRun(int index) {
        if (field_4_format_runs == null) {
            return null;
        }
        if (index < 0 || index >= field_4_format_runs.size()) {
            return null;
        }
        return field_4_format_runs.get(index);
    }

    private int findFormatRunAt(int characterPos) {
        int size = field_4_format_runs.size();
        for (int i = 0; i < size; i++) {
            FormatRun r = field_4_format_runs.get(i);
            if (r._character == characterPos)
                return i;
            else if (r._character > characterPos)
                return -1;
        }
        return -1;
    }

    public void addFormatRun(FormatRun r) {
        if (field_4_format_runs == null) {
            field_4_format_runs = new ArrayList<FormatRun>();
        }

        int index = findFormatRunAt(r._character);
        if (index != -1)
            field_4_format_runs.remove(index);

        field_4_format_runs.add(r);

        Collections.sort(field_4_format_runs);

        field_2_optionflags = richText.setByte(field_2_optionflags);
    }

    public Iterator<FormatRun> formatIterator() {
        if (field_4_format_runs != null) {
            return field_4_format_runs.iterator();
        }
        return null;
    }

    public void removeFormatRun(FormatRun r) {
        field_4_format_runs.remove(r);
        if (field_4_format_runs.size() == 0) {
            field_4_format_runs = null;
            field_2_optionflags = richText.clearByte(field_2_optionflags);
        }
    }

    public void clearFormatting() {
        field_4_format_runs = null;
        field_2_optionflags = richText.clearByte(field_2_optionflags);
    }

    public ExtRst getExtendedRst() {
        return this.field_5_ext_rst;
    }

    void setExtendedRst(ExtRst ext_rst) {
        if (ext_rst != null) {
            field_2_optionflags = extBit.setByte(field_2_optionflags);
        } else {
            field_2_optionflags = extBit.clearByte(field_2_optionflags);
        }
        this.field_5_ext_rst = ext_rst;
    }

    public void swapFontUse(short oldFontIndex, short newFontIndex) {
        for (FormatRun run : field_4_format_runs) {
            if (run._fontIndex == oldFontIndex) {
                run._fontIndex = newFontIndex;
            }
        }
    }

    public String toString() {
        return getString();
    }

    public String getDebugInfo() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[UNICODESTRING]\n");
        buffer.append("    .charcount       = ")
                .append(Integer.toHexString(getCharCount())).append("\n");
        buffer.append("    .optionflags     = ")
                .append(Integer.toHexString(getOptionFlags())).append("\n");
        buffer.append("    .string          = ").append(getString()).append("\n");
        if (field_4_format_runs != null) {
            for (int i = 0; i < field_4_format_runs.size(); i++) {
                FormatRun r = field_4_format_runs.get(i);
                buffer.append("      .format_run" + i + "          = ").append(r.toString()).append("\n");
            }
        }
        if (field_5_ext_rst != null) {
            buffer.append("    .field_5_ext_rst          = ").append("\n");
            buffer.append(field_5_ext_rst.toString()).append("\n");
        }
        buffer.append("[/UNICODESTRING]\n");
        return buffer.toString();
    }

    public void serialize(ContinuableRecordOutput out) {
        int numberOfRichTextRuns = 0;
        int extendedDataSize = 0;
        if (isRichText() && field_4_format_runs != null) {
            numberOfRichTextRuns = field_4_format_runs.size();
        }
        if (isExtendedText() && field_5_ext_rst != null) {
            extendedDataSize = 4 + field_5_ext_rst.getDataSize();
        }

        out.writeString(field_3_string, numberOfRichTextRuns, extendedDataSize);

        if (numberOfRichTextRuns > 0) {

            for (int i = 0; i < numberOfRichTextRuns; i++) {
                if (out.getAvailableSpace() < 4) {
                    out.writeContinue();
                }
                FormatRun r = field_4_format_runs.get(i);
                r.serialize(out);
            }
        }

        if (extendedDataSize > 0) {
            field_5_ext_rst.serialize(out);
        }
    }

    public int compareTo(UnicodeString str) {

        int result = getString().compareTo(str.getString());

        if (result != 0)
            return result;

        if ((field_4_format_runs == null) && (str.field_4_format_runs == null))

            return 0;

        if ((field_4_format_runs == null) && (str.field_4_format_runs != null))

            return 1;
        if ((field_4_format_runs != null) && (str.field_4_format_runs == null))

            return -1;

        int size = field_4_format_runs.size();
        if (size != str.field_4_format_runs.size())
            return size - str.field_4_format_runs.size();

        for (int i = 0; i < size; i++) {
            FormatRun run1 = field_4_format_runs.get(i);
            FormatRun run2 = str.field_4_format_runs.get(i);

            result = run1.compareTo(run2);
            if (result != 0)
                return result;
        }

        if ((field_5_ext_rst == null) && (str.field_5_ext_rst == null))
            return 0;
        if ((field_5_ext_rst == null) && (str.field_5_ext_rst != null))
            return 1;
        if ((field_5_ext_rst != null) && (str.field_5_ext_rst == null))
            return -1;

        result = field_5_ext_rst.compareTo(str.field_5_ext_rst);
        return result;

    }

    private boolean isRichText() {
        return richText.isSet(getOptionFlags());
    }

    private boolean isExtendedText() {
        return extBit.isSet(getOptionFlags());
    }

    public Object clone() {
        UnicodeString str = new UnicodeString();
        str.field_1_charCount = field_1_charCount;
        str.field_2_optionflags = field_2_optionflags;
        str.field_3_string = field_3_string;
        if (field_4_format_runs != null) {
            str.field_4_format_runs = new ArrayList<FormatRun>();
            for (FormatRun r : field_4_format_runs) {
                str.field_4_format_runs.add(new FormatRun(r._character, r._fontIndex));
            }
        }
        if (field_5_ext_rst != null) {
            str.field_5_ext_rst = field_5_ext_rst.clone();
        }

        return str;
    }

    public static class FormatRun implements Comparable<FormatRun> {
        final short _character;
        short _fontIndex;

        public FormatRun(short character, short fontIndex) {
            this._character = character;
            this._fontIndex = fontIndex;
        }

        public FormatRun(LittleEndianInput in) {
            this(in.readShort(), in.readShort());
        }

        public short getCharacterPos() {
            return _character;
        }

        public short getFontIndex() {
            return _fontIndex;
        }

        public boolean equals(Object o) {
            if (!(o instanceof FormatRun)) {
                return false;
            }
            FormatRun other = (FormatRun) o;

            return _character == other._character && _fontIndex == other._fontIndex;
        }

        public int compareTo(FormatRun r) {
            if (_character == r._character && _fontIndex == r._fontIndex) {
                return 0;
            }
            if (_character == r._character) {
                return _fontIndex - r._fontIndex;
            }
            return _character - r._character;
        }

        public String toString() {
            return "character=" + _character + ",fontIndex=" + _fontIndex;
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(_character);
            out.writeShort(_fontIndex);
        }
    }

    public static class ExtRst implements Comparable<ExtRst> {
        private short reserved;

        private short formattingFontIndex;
        private short formattingOptions;

        private int numberOfRuns;
        private String phoneticText;

        private PhRun[] phRuns;

        private byte[] extraData;

        protected ExtRst() {
            populateEmpty();
        }

        protected ExtRst(LittleEndianInput in, int expectedLength) {
            reserved = in.readShort();

            if (reserved == -1) {
                populateEmpty();
                return;
            }

            if (reserved != 1) {
                _logger.log(POILogger.WARN, "Warning - ExtRst has wrong magic marker, expecting 1 but found " + reserved + " - ignoring");

                for (int i = 0; i < expectedLength - 2; i++) {
                    in.readByte();
                }

                populateEmpty();
                return;
            }

            short stringDataSize = in.readShort();

            formattingFontIndex = in.readShort();
            formattingOptions = in.readShort();

            numberOfRuns = in.readUShort();
            short length1 = in.readShort();

            short length2 = in.readShort();

            if (length1 == 0 && length2 > 0) {
                length2 = 0;
            }
            if (length1 != length2) {
                throw new IllegalStateException(
                        "The two length fields of the Phonetic Text don't agree! " +
                                length1 + " vs " + length2
                );
            }
            phoneticText = StringUtil.readUnicodeLE(in, length1);

            int runData = stringDataSize - 4 - 6 - (2 * phoneticText.length());
            int numRuns = (runData / 6);
            phRuns = new PhRun[numRuns];
            for (int i = 0; i < phRuns.length; i++) {
                phRuns[i] = new PhRun(in);
            }

            int extraDataLength = runData - (numRuns * 6);
            if (extraDataLength < 0) {
                System.err.println("Warning - ExtRst overran by " + (-extraDataLength) + " bytes");
                extraDataLength = 0;
            }
            extraData = new byte[extraDataLength];
            for (int i = 0; i < extraData.length; i++) {
                extraData[i] = in.readByte();
            }
        }

        private void populateEmpty() {
            reserved = 1;
            phoneticText = "";
            phRuns = new PhRun[0];
            extraData = new byte[0];
        }

        protected int getDataSize() {
            return 4 + 6 + (2 * phoneticText.length()) +
                    (6 * phRuns.length) + extraData.length;
        }

        protected void serialize(ContinuableRecordOutput out) {
            int dataSize = getDataSize();

            out.writeContinueIfRequired(8);
            out.writeShort(reserved);
            out.writeShort(dataSize);
            out.writeShort(formattingFontIndex);
            out.writeShort(formattingOptions);

            out.writeContinueIfRequired(6);
            out.writeShort(numberOfRuns);
            out.writeShort(phoneticText.length());
            out.writeShort(phoneticText.length());

            out.writeContinueIfRequired(phoneticText.length() * 2);
            StringUtil.putUnicodeLE(phoneticText, out);

            for (int i = 0; i < phRuns.length; i++) {
                phRuns[i].serialize(out);
            }

            out.write(extraData);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ExtRst)) {
                return false;
            }
            ExtRst other = (ExtRst) obj;
            return (compareTo(other) == 0);
        }

        public int compareTo(ExtRst o) {
            int result;

            result = reserved - o.reserved;
            if (result != 0) return result;
            result = formattingFontIndex - o.formattingFontIndex;
            if (result != 0) return result;
            result = formattingOptions - o.formattingOptions;
            if (result != 0) return result;
            result = numberOfRuns - o.numberOfRuns;
            if (result != 0) return result;

            result = phoneticText.compareTo(o.phoneticText);
            if (result != 0) return result;

            result = phRuns.length - o.phRuns.length;
            if (result != 0) return result;
            for (int i = 0; i < phRuns.length; i++) {
                result = phRuns[i].phoneticTextFirstCharacterOffset - o.phRuns[i].phoneticTextFirstCharacterOffset;
                if (result != 0) return result;
                result = phRuns[i].realTextFirstCharacterOffset - o.phRuns[i].realTextFirstCharacterOffset;
                if (result != 0) return result;
                result = phRuns[i].realTextFirstCharacterOffset - o.phRuns[i].realTextLength;
                if (result != 0) return result;
            }

            result = extraData.length - o.extraData.length;
            return result;

        }

        protected ExtRst clone() {
            ExtRst ext = new ExtRst();
            ext.reserved = reserved;
            ext.formattingFontIndex = formattingFontIndex;
            ext.formattingOptions = formattingOptions;
            ext.numberOfRuns = numberOfRuns;
            ext.phoneticText = phoneticText;
            ext.phRuns = new PhRun[phRuns.length];
            for (int i = 0; i < ext.phRuns.length; i++) {
                ext.phRuns[i] = new PhRun(
                        phRuns[i].phoneticTextFirstCharacterOffset,
                        phRuns[i].realTextFirstCharacterOffset,
                        phRuns[i].realTextLength
                );
            }
            return ext;
        }

        public short getFormattingFontIndex() {
            return formattingFontIndex;
        }

        public short getFormattingOptions() {
            return formattingOptions;
        }

        public int getNumberOfRuns() {
            return numberOfRuns;
        }

        public String getPhoneticText() {
            return phoneticText;
        }

        public PhRun[] getPhRuns() {
            return phRuns;
        }
    }

    public static class PhRun {
        private final int phoneticTextFirstCharacterOffset;
        private final int realTextFirstCharacterOffset;
        private final int realTextLength;

        public PhRun(int phoneticTextFirstCharacterOffset,
                     int realTextFirstCharacterOffset, int realTextLength) {
            this.phoneticTextFirstCharacterOffset = phoneticTextFirstCharacterOffset;
            this.realTextFirstCharacterOffset = realTextFirstCharacterOffset;
            this.realTextLength = realTextLength;
        }

        private PhRun(LittleEndianInput in) {
            phoneticTextFirstCharacterOffset = in.readUShort();
            realTextFirstCharacterOffset = in.readUShort();
            realTextLength = in.readUShort();
        }

        private void serialize(ContinuableRecordOutput out) {
            out.writeContinueIfRequired(6);
            out.writeShort(phoneticTextFirstCharacterOffset);
            out.writeShort(realTextFirstCharacterOffset);
            out.writeShort(realTextLength);
        }
    }
}
