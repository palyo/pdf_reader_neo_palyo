package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel;

import java.util.Iterator;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.CellValueRecordInterface;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RowRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Cell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Row;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Sheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Workbook;

public class ARow extends Row {

    public final static int INITIAL_CAPACITY = 5;

    public ARow(Workbook book, Sheet sheet, RowRecord record) {
        super(record.getLastCol() - record.getFirstCol() + INITIAL_CAPACITY);
        setSheet(sheet);
        record.setEmpty();
        rowNumber = record.getRowNumber();
        firstCol = record.getFirstCol();
        lastCol = Math.max(lastCol, record.getLastCol());

        this.styleIndex = record.getXFIndex();

        int t = 0;
        while ((styleIndex & (0xFFFF >> t)) > book.getNumStyles()) {
            t += 1;
        }
        styleIndex &= (0xFFFF >> t);

        setZeroHeight(record.getZeroHeight());
        short height = record.getHeight();
        if ((height & 0x8000) != 0) {
            height = 0xFF;
        } else {
            height &= 0x7FFF;
        }
        setRowPixelHeight((int) (height / 20 * KeyKt.POINT_TO_PIXEL));
    }

    private boolean isValidateCell(CellValueRecordInterface cval) {
        int cellType = ACell.determineType(cval);
        if (cellType != Cell.CELL_TYPE_BLANK) {
            return true;
        }

        Workbook book = sheet.getWorkbook();
        return (Workbook.isValidateStyle(book.getCellStyle(cval.getXFIndex()))
                || Workbook.isValidateStyle(book.getCellStyle(getRowStyle()))
                || Workbook.isValidateStyle(book.getCellStyle(sheet.getColumnStyle(cval.getColumn()))));
    }

    ACell createCellFromRecord(CellValueRecordInterface cellRec) {
        Cell cell = cells.get(cellRec.getColumn());
        if (cell != null) {
            return (ACell) cell;
        }

        if (isValidateCell(cellRec)) {
            ACell acell = new ACell(sheet, cellRec);

            int colIx = cellRec.getColumn();
            if (colIx < firstCol) {
                firstCol = colIx;
            } else if (colIx > lastCol) {
                lastCol = colIx;
            }

            addCell(acell);

            return acell;
        }

        return null;
    }

    public Iterator<Cell> cellIterator() {
        @SuppressWarnings("unchecked")

        Iterator<Cell> result = cells.values().iterator();
        return result;
    }
}
