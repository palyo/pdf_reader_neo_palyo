package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NameXPtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.SpreadsheetVersion;

public interface FormulaParsingWorkbook {
    EvaluationName getName(String name, int sheetIndex);

    NameXPtg getNameXPtg(String name);

    int getExternalSheetIndex(String sheetName);

    int getExternalSheetIndex(String workbookName, String sheetName);

    SpreadsheetVersion getSpreadsheetVersion();

}
