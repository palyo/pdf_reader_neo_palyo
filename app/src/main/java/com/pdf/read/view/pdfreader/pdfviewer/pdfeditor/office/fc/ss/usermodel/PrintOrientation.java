package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public enum PrintOrientation {

    DEFAULT(1),

    PORTRAIT(2),

    LANDSCAPE(3);

    private static final PrintOrientation[] _table = new PrintOrientation[4];

    static {
        for (PrintOrientation c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private final int orientation;

    PrintOrientation(int orientation) {
        this.orientation = orientation;
    }

    public static PrintOrientation valueOf(int value) {
        return _table[value];
    }

    public int getValue() {
        return orientation;
    }
}
