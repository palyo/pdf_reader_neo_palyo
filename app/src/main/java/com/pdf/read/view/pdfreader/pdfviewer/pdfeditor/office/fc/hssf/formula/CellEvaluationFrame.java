package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import java.util.HashSet;
import java.util.Set;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

final class CellEvaluationFrame {

    private final FormulaCellCacheEntry _cce;
    private final Set<CellCacheEntry> _sensitiveInputCells;
    private FormulaUsedBlankCellSet _usedBlankCellGroup;

    public CellEvaluationFrame(FormulaCellCacheEntry cce) {
        _cce = cce;
        _sensitiveInputCells = new HashSet<CellCacheEntry>();
    }

    public CellCacheEntry getCCE() {
        return _cce;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                "]";
        return sb;
    }

    public void addSensitiveInputCell(CellCacheEntry inputCell) {
        _sensitiveInputCells.add(inputCell);
    }

    private CellCacheEntry[] getSensitiveInputCells() {
        int nItems = _sensitiveInputCells.size();
        if (nItems < 1) {
            return CellCacheEntry.EMPTY_ARRAY;
        }
        CellCacheEntry[] result = new CellCacheEntry[nItems];
        _sensitiveInputCells.toArray(result);
        return result;
    }

    public void addUsedBlankCell(int bookIndex, int sheetIndex, int rowIndex, int columnIndex) {
        if (_usedBlankCellGroup == null) {
            _usedBlankCellGroup = new FormulaUsedBlankCellSet();
        }
        _usedBlankCellGroup.addCell(bookIndex, sheetIndex, rowIndex, columnIndex);
    }

    public void updateFormulaResult(ValueEval result) {
        _cce.updateFormulaResult(result, getSensitiveInputCells(), _usedBlankCellGroup);
    }
}
