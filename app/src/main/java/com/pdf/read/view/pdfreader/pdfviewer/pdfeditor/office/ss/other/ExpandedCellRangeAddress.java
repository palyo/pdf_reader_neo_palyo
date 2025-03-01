package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.other;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.CellRangeAddress;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Cell;

public class ExpandedCellRangeAddress {
    private CellRangeAddress rangeAddr;
    private Cell expandedCell;

    public ExpandedCellRangeAddress(Cell expandedCell, int firstRow, int firstCol, int lastRow, int lastCol) {
        this.expandedCell = expandedCell;
        rangeAddr = new CellRangeAddress(firstRow, firstCol, lastRow, lastCol);
    }

    public CellRangeAddress getRangedAddress() {
        return rangeAddr;
    }

    public Cell getExpandedCell() {
        return expandedCell;
    }

    public void dispose() {
        rangeAddr = null;

        expandedCell = null;

    }
}
