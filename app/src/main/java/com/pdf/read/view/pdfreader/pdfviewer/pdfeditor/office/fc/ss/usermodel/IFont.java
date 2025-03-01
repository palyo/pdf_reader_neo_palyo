package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface IFont {

    short BOLDWEIGHT_NORMAL = 0x190;

    short BOLDWEIGHT_BOLD = 0x2bc;

    short COLOR_NORMAL = 0x7fff;

    short COLOR_RED = 0xa;

    short SS_NONE = 0;

    short SS_SUPER = 1;

    short SS_SUB = 2;

    byte U_NONE = 0;

    byte U_SINGLE = 1;

    byte U_DOUBLE = 2;

    byte U_SINGLE_ACCOUNTING = 0x21;

    byte U_DOUBLE_ACCOUNTING = 0x22;

    byte ANSI_CHARSET = 0;

    byte DEFAULT_CHARSET = 1;

    byte SYMBOL_CHARSET = 2;

    String getFontName();

    void setFontName(String name);

    short getFontHeight();

    void setFontHeight(short height);

    short getFontHeightInPoints();

    void setFontHeightInPoints(short height);

    boolean getItalic();

    void setItalic(boolean italic);

    boolean getStrikeout();

    void setStrikeout(boolean strikeout);

    short getColor();

    void setColor(short color);

    short getTypeOffset();

    void setTypeOffset(short offset);

    byte getUnderline();

    void setUnderline(byte underline);

    int getCharSet();

    void setCharSet(byte charset);

    void setCharSet(int charset);

    short getIndex();

    short getBoldweight();

    void setBoldweight(short boldweight);

}
