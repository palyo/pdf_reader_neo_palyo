package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface ConditionalFormattingRule {

    byte CONDITION_TYPE_CELL_VALUE_IS = 1;

    byte CONDITION_TYPE_FORMULA = 2;

    BorderFormatting createBorderFormatting();

    BorderFormatting getBorderFormatting();

    FontFormatting createFontFormatting();

    FontFormatting getFontFormatting();

    PatternFormatting createPatternFormatting();

    PatternFormatting getPatternFormatting();

    byte getConditionType();

    byte getComparisonOperation();

    String getFormula1();

    String getFormula2();
}
