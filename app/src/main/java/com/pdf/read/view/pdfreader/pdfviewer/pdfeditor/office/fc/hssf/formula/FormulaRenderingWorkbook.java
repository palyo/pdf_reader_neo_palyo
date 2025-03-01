package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.EvaluationWorkbook.ExternalSheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NamePtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NameXPtg;

public interface FormulaRenderingWorkbook {

    ExternalSheet getExternalSheet(int externSheetIndex);

    String getSheetNameByExternSheet(int externSheetIndex);

    String resolveNameXText(NameXPtg nameXPtg);

    String getNameText(NamePtg namePtg);
}
