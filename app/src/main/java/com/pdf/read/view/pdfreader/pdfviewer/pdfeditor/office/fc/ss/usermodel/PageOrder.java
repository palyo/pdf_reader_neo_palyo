package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public enum PageOrder {

    DOWN_THEN_OVER(1),

    OVER_THEN_DOWN(2);

    private static final PageOrder[] _table = new PageOrder[3];

    static {
        for (PageOrder c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private final int order;

    PageOrder(int order) {
        this.order = order;
    }

    public static PageOrder valueOf(int value) {
        return _table[value];
    }

    public int getValue() {
        return order;
    }
}
