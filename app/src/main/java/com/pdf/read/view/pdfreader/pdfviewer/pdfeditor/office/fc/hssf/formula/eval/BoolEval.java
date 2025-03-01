package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

public final class BoolEval implements NumericValueEval, StringValueEval {

    public static final BoolEval FALSE = new BoolEval(false);
    public static final BoolEval TRUE = new BoolEval(true);
    private final boolean _value;

    private BoolEval(boolean value) {
        _value = value;
    }

    public static BoolEval valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public boolean getBooleanValue() {
        return _value;
    }

    public double getNumberValue() {
        return _value ? 1 : 0;
    }

    public String getStringValue() {
        return _value ? "TRUE" : "FALSE";
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                getStringValue() +
                "]";
        return sb;
    }
}
