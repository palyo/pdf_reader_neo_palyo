package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.atp;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.OperationEvaluationContext;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ErrorEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.EvaluationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.OperandResolver;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FreeRefFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.NumericFunction;

final class MRound implements FreeRefFunction {

    public static final FreeRefFunction instance = new MRound();

    private MRound() {

    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        double number, multiple, result;

        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }

        try {
            number = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0],
                    ec.getRowIndex(), ec.getColumnIndex()));
            multiple = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[1],
                    ec.getRowIndex(), ec.getColumnIndex()));

            if (multiple == 0.0) {
                result = 0.0;
            } else {
                if (number * multiple < 0) {

                    throw new EvaluationException(ErrorEval.NUM_ERROR);
                }
                result = multiple * Math.round(number / multiple);
            }
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
