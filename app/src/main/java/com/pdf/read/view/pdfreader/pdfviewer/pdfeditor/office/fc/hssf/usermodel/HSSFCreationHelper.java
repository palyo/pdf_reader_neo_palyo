package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.CreationHelper;

public class HSSFCreationHelper implements CreationHelper {
    private final HSSFWorkbook workbook;
    private final HSSFDataFormat dataFormat;

    HSSFCreationHelper(HSSFWorkbook wb) {
        workbook = wb;

        dataFormat = new HSSFDataFormat(workbook.getWorkbook());
    }

    public HSSFRichTextString createRichTextString(String text) {
        return new HSSFRichTextString(text);
    }

    public HSSFDataFormat createDataFormat() {
        return dataFormat;
    }

    public HSSFHyperlink createHyperlink(int type) {
        return new HSSFHyperlink(type);
    }

    public HSSFFormulaEvaluator createFormulaEvaluator() {
        return null;
    }

    public HSSFClientAnchor createClientAnchor() {
        return new HSSFClientAnchor();
    }
}
