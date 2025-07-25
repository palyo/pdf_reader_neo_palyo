package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellReference;

final class FormulaUsedBlankCellSet {
    private final Map<BookSheetKey, BlankCellSheetGroup> _sheetGroupsByBookSheet;

    public FormulaUsedBlankCellSet() {
        _sheetGroupsByBookSheet = new HashMap<BookSheetKey, BlankCellSheetGroup>();
    }

    public void addCell(int bookIndex, int sheetIndex, int rowIndex, int columnIndex) {
        BlankCellSheetGroup sbcg = getSheetGroup(bookIndex, sheetIndex);
        sbcg.addCell(rowIndex, columnIndex);
    }

    private BlankCellSheetGroup getSheetGroup(int bookIndex, int sheetIndex) {
        BookSheetKey key = new BookSheetKey(bookIndex, sheetIndex);

        BlankCellSheetGroup result = _sheetGroupsByBookSheet.get(key);
        if (result == null) {
            result = new BlankCellSheetGroup();
            _sheetGroupsByBookSheet.put(key, result);
        }
        return result;
    }

    public boolean containsCell(BookSheetKey key, int rowIndex, int columnIndex) {
        BlankCellSheetGroup bcsg = _sheetGroupsByBookSheet.get(key);
        if (bcsg == null) {
            return false;
        }
        return bcsg.containsCell(rowIndex, columnIndex);
    }

    public boolean isEmpty() {
        return _sheetGroupsByBookSheet.isEmpty();
    }

    public static final class BookSheetKey {

        private final int _bookIndex;
        private final int _sheetIndex;

        public BookSheetKey(int bookIndex, int sheetIndex) {
            _bookIndex = bookIndex;
            _sheetIndex = sheetIndex;
        }

        public int hashCode() {
            return _bookIndex * 17 + _sheetIndex;
        }

        public boolean equals(Object obj) {
            assert obj instanceof BookSheetKey : "these private cache key instances are only compared to themselves";
            BookSheetKey other = (BookSheetKey) obj;
            return _bookIndex == other._bookIndex && _sheetIndex == other._sheetIndex;
        }
    }

    private static final class BlankCellSheetGroup {
        private final List<BlankCellRectangleGroup> _rectangleGroups;
        private int _currentRowIndex;
        private int _firstColumnIndex;
        private int _lastColumnIndex;
        private BlankCellRectangleGroup _currentRectangleGroup;

        public BlankCellSheetGroup() {
            _rectangleGroups = new ArrayList<BlankCellRectangleGroup>();
            _currentRowIndex = -1;
        }

        public void addCell(int rowIndex, int columnIndex) {
            if (_currentRowIndex == -1) {
                _currentRowIndex = rowIndex;
                _firstColumnIndex = columnIndex;
                _lastColumnIndex = columnIndex;
            } else {
                if (_currentRowIndex == rowIndex && _lastColumnIndex + 1 == columnIndex) {
                    _lastColumnIndex = columnIndex;
                } else {

                    if (_currentRectangleGroup == null) {
                        _currentRectangleGroup = new BlankCellRectangleGroup(_currentRowIndex, _firstColumnIndex, _lastColumnIndex);
                    } else {
                        if (!_currentRectangleGroup.acceptRow(_currentRowIndex, _firstColumnIndex, _lastColumnIndex)) {
                            _rectangleGroups.add(_currentRectangleGroup);
                            _currentRectangleGroup = new BlankCellRectangleGroup(_currentRowIndex, _firstColumnIndex, _lastColumnIndex);
                        }
                    }
                    _currentRowIndex = rowIndex;
                    _firstColumnIndex = columnIndex;
                    _lastColumnIndex = columnIndex;
                }
            }
        }

        public boolean containsCell(int rowIndex, int columnIndex) {
            for (int i = _rectangleGroups.size() - 1; i >= 0; i--) {
                BlankCellRectangleGroup bcrg = _rectangleGroups.get(i);
                if (bcrg.containsCell(rowIndex, columnIndex)) {
                    return true;
                }
            }
            if (_currentRectangleGroup != null && _currentRectangleGroup.containsCell(rowIndex, columnIndex)) {
                return true;
            }
            if (_currentRowIndex != -1 && _currentRowIndex == rowIndex) {
                return _firstColumnIndex <= columnIndex && columnIndex <= _lastColumnIndex;
            }
            return false;
        }
    }

    private static final class BlankCellRectangleGroup {

        private final int _firstRowIndex;
        private final int _firstColumnIndex;
        private final int _lastColumnIndex;
        private int _lastRowIndex;

        public BlankCellRectangleGroup(int firstRowIndex, int firstColumnIndex, int lastColumnIndex) {
            _firstRowIndex = firstRowIndex;
            _firstColumnIndex = firstColumnIndex;
            _lastColumnIndex = lastColumnIndex;
            _lastRowIndex = firstRowIndex;
        }

        public boolean containsCell(int rowIndex, int columnIndex) {
            if (columnIndex < _firstColumnIndex) {
                return false;
            }
            if (columnIndex > _lastColumnIndex) {
                return false;
            }
            if (rowIndex < _firstRowIndex) {
                return false;
            }
            return rowIndex <= _lastRowIndex;
        }

        public boolean acceptRow(int rowIndex, int firstColumnIndex, int lastColumnIndex) {
            if (firstColumnIndex != _firstColumnIndex) {
                return false;
            }
            if (lastColumnIndex != _lastColumnIndex) {
                return false;
            }
            if (rowIndex != _lastRowIndex + 1) {
                return false;
            }
            _lastRowIndex = rowIndex;
            return true;
        }

        public String toString() {
            CellReference crA = new CellReference(_firstRowIndex, _firstColumnIndex, false, false);
            CellReference crB = new CellReference(_lastRowIndex, _lastColumnIndex, false, false);
            String sb = getClass().getName() +
                    " [" + crA.formatAsString() + ':' + crB.formatAsString() + "]";
            return sb;
        }
    }
}
