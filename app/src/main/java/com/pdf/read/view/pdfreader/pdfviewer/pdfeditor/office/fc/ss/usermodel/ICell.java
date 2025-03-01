package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

import java.util.Calendar;
import java.util.Date;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.FormulaParseException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.HSSFCellRangeAddress;

public interface ICell {

    int CELL_TYPE_NUMERIC = 0;

    int CELL_TYPE_STRING = 1;

    int CELL_TYPE_FORMULA = 2;

    int CELL_TYPE_BLANK = 3;

    int CELL_TYPE_BOOLEAN = 4;

    int CELL_TYPE_ERROR = 5;

    int getColumnIndex();

    int getRowIndex();

    Sheet getSheet();

    IRow getRow();

    int getCellType();

    void setCellType(int cellType);

    int getCachedFormulaResultType();

    void setCellValue(double value);

    void setCellValue(Date value);

    void setCellValue(Calendar value);

    void setCellValue(RichTextString value);

    void setCellValue(String value);

    String getCellFormula();

    void setCellFormula(String formula) throws FormulaParseException;

    double getNumericCellValue();

    Date getDateCellValue();

    RichTextString getRichStringCellValue();

    String getStringCellValue();

    void setCellValue(boolean value);

    void setCellErrorValue(byte value);

    boolean getBooleanCellValue();

    byte getErrorCellValue();

    ICellStyle getCellStyle();

    void setCellStyle(ICellStyle style);

    void setAsActiveCell();

    Comment getCellComment();

    void setCellComment(Comment comment);

    void removeCellComment();

    IHyperlink getHyperlink();

    void setHyperlink(IHyperlink link);

    HSSFCellRangeAddress getArrayFormulaRange();

    boolean isPartOfArrayFormulaGroup();
}
