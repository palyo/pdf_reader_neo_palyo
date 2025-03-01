package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.IntPtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NumberPtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Ptg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.NumberToTextConverter;

public final class NumberEval implements NumericValueEval, StringValueEval {

    public static final NumberEval ZERO = new NumberEval(0);

    private final double _value;
    private String _stringValue;

    public NumberEval(Ptg ptg) {
        if (ptg == null) {
            throw new IllegalArgumentException("ptg must not be null");
        }
        if (ptg instanceof IntPtg) {
            _value = ((IntPtg) ptg).getValue();
        } else if (ptg instanceof NumberPtg) {
            _value = ((NumberPtg) ptg).getValue();
        } else {
            throw new IllegalArgumentException("bad argument type (" + ptg.getClass().getName()
                    + ")");
        }
    }

    public NumberEval(double value) {
        _value = value;
    }

    public double getNumberValue() {
        return _value;
    }

    public String getStringValue() {
        if (_stringValue == null) {
            _stringValue = NumberToTextConverter.toText(_value);
        }
        return _stringValue;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                getStringValue() +
                "]";
        return sb;
    }
}
