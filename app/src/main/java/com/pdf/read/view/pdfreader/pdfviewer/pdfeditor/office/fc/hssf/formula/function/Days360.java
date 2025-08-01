package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.EvaluationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.OperandResolver;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.DateUtil;

public class Days360 extends Var2or3ArgFunction {

    private static double evaluate(double d0, double d1, boolean method) {
        Calendar startingDate = getStartingDate(d0);
        Calendar endingDate = getEndingDateAccordingToStartingDate(d1, startingDate);
        long startingDay = startingDate.get(Calendar.MONTH) * 30 + startingDate.get(Calendar.DAY_OF_MONTH);
        long endingDay = (endingDate.get(Calendar.YEAR) - startingDate.get(Calendar.YEAR)) * 360L
                + endingDate.get(Calendar.MONTH) * 30 + endingDate.get(Calendar.DAY_OF_MONTH);
        return endingDay - startingDay;
    }

    private static Calendar getDate(double date) {
        Calendar processedDate = new GregorianCalendar();
        processedDate.setTime(DateUtil.getJavaDate(date, false));
        return processedDate;
    }

    private static Calendar getStartingDate(double date) {
        Calendar startingDate = getDate(date);
        if (isLastDayOfMonth(startingDate)) {
            startingDate.set(Calendar.DAY_OF_MONTH, 30);
        }
        return startingDate;
    }

    private static Calendar getEndingDateAccordingToStartingDate(double date, Calendar startingDate) {
        Calendar endingDate = getDate(date);
        endingDate.setTime(DateUtil.getJavaDate(date, false));
        if (isLastDayOfMonth(endingDate)) {
            if (startingDate.get(Calendar.DATE) < 30) {
                endingDate = getFirstDayOfNextMonth(endingDate);
            }
        }
        return endingDate;
    }

    private static boolean isLastDayOfMonth(Calendar date) {
        Calendar clone = (Calendar) date.clone();
        clone.add(Calendar.MONTH, 1);
        clone.add(Calendar.DAY_OF_MONTH, -1);
        int lastDayOfMonth = clone.get(Calendar.DAY_OF_MONTH);
        return date.get(Calendar.DAY_OF_MONTH) == lastDayOfMonth;
    }

    private static Calendar getFirstDayOfNextMonth(Calendar date) {
        Calendar newDate = (Calendar) date.clone();
        if (date.get(Calendar.MONTH) < Calendar.DECEMBER) {
            newDate.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1);
        } else {
            newDate.set(Calendar.MONTH, 1);
            newDate.set(Calendar.YEAR, date.get(Calendar.YEAR) + 1);
        }
        newDate.set(Calendar.DATE, 1);
        return newDate;
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        double result;
        try {
            double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
            double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
            result = evaluate(d0, d1, false);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
        return new NumberEval(result);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1,
                              ValueEval arg2) {
        double result;
        try {
            double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
            double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
            ValueEval ve = OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex);
            Boolean method = OperandResolver.coerceValueToBoolean(ve, false);
            result = evaluate(d0, d1, method != null && method.booleanValue());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
        return new NumberEval(result);
    }
}
