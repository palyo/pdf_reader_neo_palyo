package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.TwoDEval;

public interface AreaEval extends TwoDEval {

    int getFirstRow();

    int getLastRow();

    int getFirstColumn();

    int getLastColumn();

    ValueEval getAbsoluteValue(int row, int col);

    boolean contains(int row, int col);

    boolean containsColumn(int col);

    boolean containsRow(int row);

    int getWidth();

    int getHeight();

    ValueEval getRelativeValue(int relativeRowIndex, int relativeColumnIndex);

    AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx);
}
