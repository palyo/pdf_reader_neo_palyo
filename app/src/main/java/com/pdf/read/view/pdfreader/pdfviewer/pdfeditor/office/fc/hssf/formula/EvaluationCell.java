package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

public interface EvaluationCell {
    Object getIdentityKey();

    EvaluationSheet getSheet();

    int getRowIndex();

    int getColumnIndex();

    int getCellType();

    double getNumericCellValue();

    String getStringCellValue();

    boolean getBooleanCellValue();

    int getErrorCellValue();
}
