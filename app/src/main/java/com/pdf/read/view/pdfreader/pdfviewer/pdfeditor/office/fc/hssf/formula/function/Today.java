package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.DateUtil;

public final class Today extends Fixed0ArgFunction {

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {

        Calendar now = new GregorianCalendar();
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
        now.set(Calendar.MILLISECOND, 0);
        return new NumberEval(DateUtil.getExcelDate(now.getTime()));
    }
}
