package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

public interface RefEval extends ValueEval {

    ValueEval getInnerValueEval();

    int getColumn();

    int getRow();

    AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx);
}
