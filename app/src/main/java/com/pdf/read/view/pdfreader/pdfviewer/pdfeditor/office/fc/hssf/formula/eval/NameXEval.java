package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NameXPtg;

public final class NameXEval implements ValueEval {

    private final NameXPtg _ptg;

    public NameXEval(NameXPtg ptg) {
        _ptg = ptg;
    }

    public NameXPtg getPtg() {
        return _ptg;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                _ptg.getSheetRefIndex() + ", " + _ptg.getNameIndex() +
                "]";
        return sb;
    }
}
