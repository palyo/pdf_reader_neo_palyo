package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.FormulaUsedBlankCellSet.BookSheetKey;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

final class FormulaCellCacheEntry extends CellCacheEntry {

    private CellCacheEntry[] _sensitiveInputCells;

    private FormulaUsedBlankCellSet _usedBlankCellGroup;

    public FormulaCellCacheEntry() {

    }

    public boolean isInputSensitive() {
        if (_sensitiveInputCells != null) {
            if (_sensitiveInputCells.length > 0) {
                return true;
            }
        }
        return _usedBlankCellGroup != null && !_usedBlankCellGroup.isEmpty();
    }

    public void setSensitiveInputCells(CellCacheEntry[] sensitiveInputCells) {

        changeConsumingCells(sensitiveInputCells == null ? CellCacheEntry.EMPTY_ARRAY : sensitiveInputCells);
        _sensitiveInputCells = sensitiveInputCells;
    }

    public void clearFormulaEntry() {
        CellCacheEntry[] usedCells = _sensitiveInputCells;
        if (usedCells != null) {
            for (int i = usedCells.length - 1; i >= 0; i--) {
                usedCells[i].clearConsumingCell(this);
            }
        }
        _sensitiveInputCells = null;
        clearValue();
    }

    private void changeConsumingCells(CellCacheEntry[] usedCells) {

        CellCacheEntry[] prevUsedCells = _sensitiveInputCells;
        int nUsed = usedCells.length;
        for (int i = 0; i < nUsed; i++) {
            usedCells[i].addConsumingCell(this);
        }
        if (prevUsedCells == null) {
            return;
        }
        int nPrevUsed = prevUsedCells.length;
        if (nPrevUsed < 1) {
            return;
        }
        Set<CellCacheEntry> usedSet;
        if (nUsed < 1) {
            usedSet = Collections.emptySet();
        } else {
            usedSet = new HashSet<CellCacheEntry>(nUsed * 3 / 2);
            usedSet.addAll(Arrays.asList(usedCells).subList(0, nUsed));
        }
        for (int i = 0; i < nPrevUsed; i++) {
            CellCacheEntry prevUsed = prevUsedCells[i];
            if (!usedSet.contains(prevUsed)) {

                prevUsed.clearConsumingCell(this);
            }
        }
    }

    public void updateFormulaResult(ValueEval result, CellCacheEntry[] sensitiveInputCells, FormulaUsedBlankCellSet usedBlankAreas) {
        updateValue(result);
        setSensitiveInputCells(sensitiveInputCells);
        _usedBlankCellGroup = usedBlankAreas;
    }

    public void notifyUpdatedBlankCell(BookSheetKey bsk, int rowIndex, int columnIndex, IEvaluationListener evaluationListener) {
        if (_usedBlankCellGroup != null) {
            if (_usedBlankCellGroup.containsCell(bsk, rowIndex, columnIndex)) {
                clearFormulaEntry();
                recurseClearCachedFormulaResults(evaluationListener);
            }
        }
    }
}
