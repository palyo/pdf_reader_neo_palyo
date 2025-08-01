package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ErrorEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public abstract class Fixed2ArgFunction implements Function2Arg {
    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
    }
}
