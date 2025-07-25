package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Fixed2ArgFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Function;

public final class ConcatEval extends Fixed2ArgFunction {

    public static final Function instance = new ConcatEval();

    private ConcatEval() {

    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        ValueEval ve0;
        ValueEval ve1;
        try {
            ve0 = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
            ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
        String sb = String.valueOf(getText(ve0)) +
                getText(ve1);
        return new StringEval(sb);
    }

    private Object getText(ValueEval ve) {
        if (ve instanceof StringValueEval) {
            StringValueEval sve = (StringValueEval) ve;
            return sve.getStringValue();
        }
        if (ve == BlankEval.instance) {
            return "";
        }
        throw new IllegalAccessError("Unexpected value type ("
                + ve.getClass().getName() + ")");
    }
}
