package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.IClientAnchor;

public interface CreationHelper {

    RichTextString createRichTextString(String text);

    DataFormat createDataFormat();

    IHyperlink createHyperlink(int type);

    FormulaEvaluator createFormulaEvaluator();

    IClientAnchor createClientAnchor();
}
