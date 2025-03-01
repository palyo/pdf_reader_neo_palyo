package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.NamePtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Ptg;

public interface EvaluationName {

    String getNameText();

    boolean isFunctionName();

    boolean hasFormula();

    Ptg[] getNameDefinition();

    boolean isRange();

    NamePtg createPtg();
}
