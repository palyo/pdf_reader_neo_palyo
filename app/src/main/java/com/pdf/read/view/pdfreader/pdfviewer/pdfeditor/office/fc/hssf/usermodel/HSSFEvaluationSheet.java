package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.EvaluationCell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.EvaluationSheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.ACell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.ARow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.ASheet;

class HSSFEvaluationSheet implements EvaluationSheet {

    private ASheet _hs;

    public HSSFEvaluationSheet(ASheet hs) {
        _hs = hs;
    }

    public ASheet getASheet() {
        return _hs;
    }

    public void setASheet(ASheet hs) {
        _hs = hs;
    }

    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        ARow row = (ARow) _hs.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        ACell cell = (ACell) row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return new HSSFEvaluationCell(cell, this);
    }
}
