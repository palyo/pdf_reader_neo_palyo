package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface Name {

    String getSheetName();

    String getNameName();

    void setNameName(String name);

    String getRefersToFormula();

    void setRefersToFormula(String formulaText);

    boolean isFunctionName();

    boolean isDeleted();

    int getSheetIndex();

    void setSheetIndex(int sheetId);

    String getComment();

    void setComment(String comment);

    void setFunction(boolean value);
}
