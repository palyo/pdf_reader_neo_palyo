package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public interface Function {

    ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex);
}
