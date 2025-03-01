package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Ptg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.StringPtg;

public final class StringEval implements StringValueEval {

    public static final StringEval EMPTY_INSTANCE = new StringEval("");

    private final String _value;

    public StringEval(Ptg ptg) {
        this(((StringPtg) ptg).getValue());
    }

    public StringEval(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        _value = value;
    }

    public String getStringValue() {
        return _value;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                _value +
                "]";
        return sb;
    }
}
