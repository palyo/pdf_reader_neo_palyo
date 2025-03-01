package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.util;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;

public class CellRangeAddress extends com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.HSSFCellRangeAddress {

    public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
    }

    public CellRangeAddress(RecordInputStream in) {
        super(in);
    }
}
