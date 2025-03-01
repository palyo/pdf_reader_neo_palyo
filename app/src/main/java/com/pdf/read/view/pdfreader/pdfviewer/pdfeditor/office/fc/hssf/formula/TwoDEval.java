package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public interface TwoDEval extends ValueEval {

    ValueEval getValue(int rowIndex, int columnIndex);

    int getWidth();

    int getHeight();

    boolean isRow();

    boolean isColumn();

    TwoDEval getRow(int rowIndex);

    TwoDEval getColumn(int columnIndex);

    boolean isSubTotal(int rowIndex, int columnIndex);

}
