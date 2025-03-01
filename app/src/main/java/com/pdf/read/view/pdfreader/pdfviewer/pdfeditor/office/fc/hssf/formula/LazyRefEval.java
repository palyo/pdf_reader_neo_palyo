package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.AreaEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.RefEvalBase;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.AreaI;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.AreaI.OffsetArea;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellReference;

final class LazyRefEval extends RefEvalBase {

    private final SheetRefEvaluator _evaluator;

    public LazyRefEval(int rowIndex, int columnIndex, SheetRefEvaluator sre) {
        super(rowIndex, columnIndex);
        if (sre == null) {
            throw new IllegalArgumentException("sre must not be null");
        }
        _evaluator = sre;
    }

    public ValueEval getInnerValueEval() {
        return _evaluator.getEvalForCell(getRow(), getColumn());
    }

    public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {

        AreaI area = new OffsetArea(getRow(), getColumn(),
                relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);

        return new LazyAreaEval(area, _evaluator);
    }

    public String toString() {
        CellReference cr = new CellReference(getRow(), getColumn());
        String sb = getClass().getName() + "[" +
                _evaluator.getSheetName() +
                '!' +
                cr.formatAsString() +
                "]";
        return sb;
    }
}
