package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface PatternFormatting {

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

    short getFillBackgroundColor();

    void setFillBackgroundColor(short bg);

    short getFillForegroundColor();

    void setFillForegroundColor(short fg);

    short getFillPattern();

    void setFillPattern(short fp);
}
