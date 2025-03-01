package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PatternFormatting implements Cloneable {
    private static final BitField fillPatternStyle = BitFieldFactory.getInstance(0xFC00);
    private static final BitField patternColorIndex = BitFieldFactory.getInstance(0x007F);
    private static final BitField patternBackgroundColorIndex = BitFieldFactory.getInstance(0x3F80);

    private int field_15_pattern_style;
    private int field_16_pattern_color_indexes;

    public PatternFormatting() {
        field_15_pattern_style = 0;
        field_16_pattern_color_indexes = 0;
    }

    public PatternFormatting(LittleEndianInput in) {
        field_15_pattern_style = in.readUShort();
        field_16_pattern_color_indexes = in.readUShort();
    }

    public int getFillPattern() {
        return fillPatternStyle.getValue(field_15_pattern_style);
    }

    public void setFillPattern(int fp) {
        field_15_pattern_style = fillPatternStyle.setValue(field_15_pattern_style, fp);
    }

    public int getFillBackgroundColor() {
        return patternBackgroundColorIndex.getValue(field_16_pattern_color_indexes);
    }

    public void setFillBackgroundColor(int bg) {
        field_16_pattern_color_indexes = patternBackgroundColorIndex.setValue(field_16_pattern_color_indexes, bg);
    }

    public int getFillForegroundColor() {
        return patternColorIndex.getValue(field_16_pattern_color_indexes);
    }

    public void setFillForegroundColor(int fg) {
        field_16_pattern_color_indexes = patternColorIndex.setValue(field_16_pattern_color_indexes, fg);
    }

    public String toString() {
        String buffer = "    [Pattern Formatting]\n" +
                "          .fillpattern= " + Integer.toHexString(getFillPattern()) + "\n" +
                "          .fgcoloridx= " + Integer.toHexString(getFillForegroundColor()) + "\n" +
                "          .bgcoloridx= " + Integer.toHexString(getFillBackgroundColor()) + "\n" +
                "    [/Pattern Formatting]\n";
        return buffer;
    }

    public Object clone() {
        PatternFormatting rec = new PatternFormatting();
        rec.field_15_pattern_style = field_15_pattern_style;
        rec.field_16_pattern_color_indexes = field_16_pattern_color_indexes;
        return rec;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_15_pattern_style);
        out.writeShort(field_16_pattern_color_indexes);
    }
}
