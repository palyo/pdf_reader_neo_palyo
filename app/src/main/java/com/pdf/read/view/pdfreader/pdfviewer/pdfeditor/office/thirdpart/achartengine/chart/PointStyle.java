package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart;

public enum PointStyle {
    X("x"), CIRCLE("circle"), TRIANGLE("triangle"), SQUARE("square"), DIAMOND("diamond"), POINT(
            "point");

    private final String mName;

    PointStyle(String name) {
        mName = name;
    }

    public static PointStyle getPointStyleForName(String name) {
        PointStyle pointStyle = null;
        PointStyle[] styles = values();
        int length = styles.length;
        for (int i = 0; i < length && pointStyle == null; i++) {
            if (styles[i].mName.equals(name)) {
                pointStyle = styles[i];
            }
        }
        return pointStyle;
    }

    public static int getIndexForName(String name) {
        int index = -1;
        PointStyle[] styles = values();
        int length = styles.length;
        for (int i = 0; i < length && index < 0; i++) {
            if (styles[i].mName.equals(name)) {
                index = i;
            }
        }
        return Math.max(0, index);
    }

    public String getName() {
        return mName;
    }

    public String toString() {
        return getName();
    }

}
