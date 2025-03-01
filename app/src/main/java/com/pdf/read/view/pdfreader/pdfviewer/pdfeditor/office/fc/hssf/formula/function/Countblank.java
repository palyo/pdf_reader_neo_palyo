package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.TwoDEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.BlankEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.RefEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.CountUtils.I_MatchPredicate;

public final class Countblank extends Fixed1ArgFunction {

    private static final I_MatchPredicate predicate = new I_MatchPredicate() {

        public boolean matches(ValueEval valueEval) {

            return valueEval == BlankEval.instance;
        }
    };

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {

        double result;
        if (arg0 instanceof RefEval) {
            result = CountUtils.countMatchingCell((RefEval) arg0, predicate);
        } else if (arg0 instanceof TwoDEval) {
            result = CountUtils.countMatchingCellsInArea((TwoDEval) arg0, predicate);
        } else {
            throw new IllegalArgumentException("Bad range arg type (" + arg0.getClass().getName() + ")");
        }
        return new NumberEval(result);
    }
}
