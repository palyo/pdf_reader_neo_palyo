package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public interface Function2Arg extends Function {
    ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1);
}
