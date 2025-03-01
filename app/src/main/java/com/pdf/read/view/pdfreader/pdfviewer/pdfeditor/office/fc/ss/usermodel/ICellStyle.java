package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface ICellStyle {

    short ALIGN_GENERAL = 0x0;

    short ALIGN_LEFT = 0x1;

    short ALIGN_CENTER = 0x2;

    short ALIGN_RIGHT = 0x3;

    short ALIGN_FILL = 0x4;

    short ALIGN_JUSTIFY = 0x5;

    short ALIGN_CENTER_SELECTION = 0x6;

    short VERTICAL_TOP = 0x0;

    short VERTICAL_CENTER = 0x1;

    short VERTICAL_BOTTOM = 0x2;

    short VERTICAL_JUSTIFY = 0x3;

    short BORDER_NONE = 0x0;

    short BORDER_THIN = 0x1;

    short BORDER_MEDIUM = 0x2;

    short BORDER_DASHED = 0x3;

    short BORDER_HAIR = 0x4;

    short BORDER_THICK = 0x5;

    short BORDER_DOUBLE = 0x6;

    short BORDER_DOTTED = 0x7;

    short BORDER_MEDIUM_DASHED = 0x8;

    short BORDER_DASH_DOT = 0x9;

    short BORDER_MEDIUM_DASH_DOT = 0xA;

    short BORDER_DASH_DOT_DOT = 0xB;

    short BORDER_MEDIUM_DASH_DOT_DOT = 0xC;

    short BORDER_SLANTED_DASH_DOT = 0xD;

    short NO_FILL = 0;

    short SOLID_FOREGROUND = 1;

    short FINE_DOTS = 2;

    short ALT_BARS = 3;

    short SPARSE_DOTS = 4;

    short THICK_HORZ_BANDS = 5;

    short THICK_VERT_BANDS = 6;

    short THICK_BACKWARD_DIAG = 7;

    short THICK_FORWARD_DIAG = 8;

    short BIG_SPOTS = 9;

    short BRICKS = 10;

    short THIN_HORZ_BANDS = 11;

    short THIN_VERT_BANDS = 12;

    short THIN_BACKWARD_DIAG = 13;

    short THIN_FORWARD_DIAG = 14;

    short SQUARES = 15;

    short DIAMONDS = 16;

    short LESS_DOTS = 17;

    short LEAST_DOTS = 18;

    short getIndex();

    short getDataFormat();

    void setDataFormat(short fmt);

    String getDataFormatString();

    void setFont(IFont font);

    short getFontIndex();

    boolean getHidden();

    void setHidden(boolean hidden);

    boolean getLocked();

    void setLocked(boolean locked);

    short getAlignment();

    void setAlignment(short align);

    boolean getWrapText();

    void setWrapText(boolean wrapped);

    short getVerticalAlignment();

    void setVerticalAlignment(short align);

    short getRotation();

    void setRotation(short rotation);

    short getIndention();

    void setIndention(short indent);

    short getBorderLeft();

    void setBorderLeft(short border);

    short getBorderRight();

    void setBorderRight(short border);

    short getBorderTop();

    void setBorderTop(short border);

    short getBorderBottom();

    void setBorderBottom(short border);

    short getLeftBorderColor();

    void setLeftBorderColor(short color);

    short getRightBorderColor();

    void setRightBorderColor(short color);

    short getTopBorderColor();

    void setTopBorderColor(short color);

    short getBottomBorderColor();

    void setBottomBorderColor(short color);

    short getFillPattern();

    void setFillPattern(short fp);

    short getFillBackgroundColor();

    void setFillBackgroundColor(short bg);

    Color getFillBackgroundColorColor();

    short getFillForegroundColor();

    void setFillForegroundColor(short bg);

    Color getFillForegroundColorColor();

    void cloneStyleFrom(ICellStyle source);
}
