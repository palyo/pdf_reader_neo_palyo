package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface BorderFormatting {

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

    short getBorderBottom();

    void setBorderBottom(short border);

    short getBorderDiagonal();

    void setBorderDiagonal(short border);

    short getBorderLeft();

    void setBorderLeft(short border);

    short getBorderRight();

    void setBorderRight(short border);

    short getBorderTop();

    void setBorderTop(short border);

    short getBottomBorderColor();

    void setBottomBorderColor(short color);

    short getDiagonalBorderColor();

    void setDiagonalBorderColor(short color);

    short getLeftBorderColor();

    void setLeftBorderColor(short color);

    short getRightBorderColor();

    void setRightBorderColor(short color);

    short getTopBorderColor();

    void setTopBorderColor(short color);
}
