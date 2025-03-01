package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public enum FontScheme {

    NONE(1),
    MAJOR(2),
    MINOR(3);

    private static final FontScheme[] _table = new FontScheme[4];

    static {
        for (FontScheme c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private final int value;

    FontScheme(int val) {
        value = val;
    }

    public static FontScheme valueOf(int value) {
        return _table[value];
    }

    public int getValue() {
        return value;
    }
}
