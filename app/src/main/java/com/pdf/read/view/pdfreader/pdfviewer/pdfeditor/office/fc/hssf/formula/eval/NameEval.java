package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

public final class NameEval implements ValueEval {

    private final String _functionName;

    public NameEval(String functionName) {
        _functionName = functionName;
    }

    public String getFunctionName() {
        return _functionName;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                _functionName +
                "]";
        return sb;
    }
}
