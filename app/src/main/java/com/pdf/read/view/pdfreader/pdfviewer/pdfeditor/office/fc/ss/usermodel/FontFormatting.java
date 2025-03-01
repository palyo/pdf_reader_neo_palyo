package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface FontFormatting {

    short SS_NONE = 0;

    short SS_SUPER = 1;

    short SS_SUB = 2;

    byte U_NONE = 0;

    byte U_SINGLE = 1;

    byte U_DOUBLE = 2;

    byte U_SINGLE_ACCOUNTING = 0x21;

    byte U_DOUBLE_ACCOUNTING = 0x22;

    short getEscapementType();

    void setEscapementType(short escapementType);

    short getFontColorIndex();

    void setFontColorIndex(short color);

    int getFontHeight();

    void setFontHeight(int height);

    short getUnderlineType();

    void setUnderlineType(short underlineType);

    boolean isBold();

    boolean isItalic();

    void setFontStyle(boolean italic, boolean bold);

    void resetFontStyle();
}
