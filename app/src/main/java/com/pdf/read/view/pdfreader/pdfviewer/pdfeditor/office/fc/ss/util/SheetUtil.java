package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util;

import java.awt.font.TextAttribute;
import java.text.AttributedString;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.CellValue;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.FormulaEvaluator;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.ICell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.IFont;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.Sheet;

public class SheetUtil {

    private static final char defaultChar = '0';

    private static final double fontHeightMultiple = 2.0;

    private static final FormulaEvaluator dummyEvaluator = new FormulaEvaluator() {
        public void clearAllCachedResultValues() {
        }

        public void notifySetFormula(ICell cell) {
        }

        public void notifyDeleteCell(ICell cell) {
        }

        public void notifyUpdateCell(ICell cell) {
        }

        public CellValue evaluate(ICell cell) {
            return null;
        }

        public ICell evaluateInCell(ICell cell) {
            return null;
        }

        public void evaluateAll() {
        }

        public int evaluateFormulaCell(ICell cell) {
            return cell.getCachedFormulaResultType();
        }

    };

    public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells) {

        return 0;
    }

    private static void copyAttributes(IFont font, AttributedString str, int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
        str.addAttribute(TextAttribute.SIZE, (float) font.getFontHeightInPoints());
        if (font.getBoldweight() == IFont.BOLDWEIGHT_BOLD) str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
        if (font.getItalic()) str.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
        if (font.getUnderline() == IFont.U_SINGLE) str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
    }

    public static boolean containsCell(HSSFCellRangeAddress cr, int rowIx, int colIx) {
        return cr.getFirstRow() <= rowIx && cr.getLastRow() >= rowIx
                && cr.getFirstColumn() <= colIx && cr.getLastColumn() >= colIx;
    }

}
