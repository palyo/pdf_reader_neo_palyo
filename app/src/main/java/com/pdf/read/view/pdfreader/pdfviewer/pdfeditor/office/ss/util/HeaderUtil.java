package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.CellRangeAddress;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Cell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Sheet;

public class HeaderUtil {

    private static final HeaderUtil util = new HeaderUtil();

    public static HeaderUtil instance() {
        return util;
    }

    public String getColumnHeaderTextByIndex(int index) {
        String result = "";
        for (; index >= 0; index = index / 26 - 1) {
            result = (char) ((char) (index % 26) + 'A') + result;
        }
        return result;
    }

    public int getColumnHeaderIndexByText(String text) {
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            index = index * 26 + (text.charAt(i) - 'A') + 1;
        }
        return index - 1;
    }

    public boolean isActiveRow(Sheet sheet, int row) {
        if (sheet.getActiveCellType() == Sheet.ACTIVECELL_COLUMN) {
            return true;
        } else if (sheet.getActiveCellType() == Sheet.ACTIVECELL_ROW) {
            return sheet.getActiveCellRow() == row;
        } else {
            boolean active = false;
            Cell cell = sheet.getActiveCell();
            if (cell != null && cell.getRangeAddressIndex() >= 0) {
                CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                if (cr.getFirstRow() <= row && cr.getLastRow() >= row) {
                    active = true;
                }
            } else if (sheet.getActiveCellRow() == row) {
                active = true;
            }

            return active;
        }

    }

    public boolean isActiveColumn(Sheet sheet, int col) {
        if (sheet.getActiveCellType() == Sheet.ACTIVECELL_ROW) {
            return true;
        } else if (sheet.getActiveCellType() == Sheet.ACTIVECELL_COLUMN) {
            return sheet.getActiveCellColumn() == col;
        } else {
            boolean active = false;
            Cell cell = sheet.getActiveCell();
            if (cell != null && cell.getRangeAddressIndex() >= 0) {
                CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                if (cr.getFirstColumn() <= col && cr.getLastColumn() >= col) {
                    active = true;
                }
            } else if (sheet.getActiveCellColumn() == col) {
                active = true;
            }

            return active;
        }

    }
}
