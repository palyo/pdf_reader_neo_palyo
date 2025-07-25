package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.CellRange;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.ICell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class SSCellRange<K extends ICell> implements CellRange<K> {

    private final int _height;
    private final int _width;
    private final K[] _flattenedArray;
    private final int _firstRow;
    private final int _firstColumn;

    private SSCellRange(int firstRow, int firstColumn, int height, int width, K[] flattenedArray) {
        _firstRow = firstRow;
        _firstColumn = firstColumn;
        _height = height;
        _width = width;
        _flattenedArray = flattenedArray;
    }

    public static <B extends ICell> SSCellRange<B> create(int firstRow, int firstColumn, int height, int width, List<B> flattenedList, Class<B> cellClass) {
        int nItems = flattenedList.size();
        if (height * width != nItems) {
            throw new IllegalArgumentException("Array size mismatch.");
        }

        @SuppressWarnings("unchecked")
        B[] flattenedArray = (B[]) Array.newInstance(cellClass, nItems);
        flattenedList.toArray(flattenedArray);
        return new SSCellRange<B>(firstRow, firstColumn, height, width, flattenedArray);
    }

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }

    public int size() {
        return _height * _width;
    }

    public String getReferenceText() {
        HSSFCellRangeAddress cra = new HSSFCellRangeAddress(_firstRow, _firstRow + _height - 1, _firstColumn, _firstColumn + _width - 1);
        return cra.formatAsString();
    }

    public K getTopLeftCell() {
        return _flattenedArray[0];
    }

    public K getCell(int relativeRowIndex, int relativeColumnIndex) {
        if (relativeRowIndex < 0 || relativeRowIndex >= _height) {
            throw new ArrayIndexOutOfBoundsException("Specified row " + relativeRowIndex
                    + " is outside the allowable range (0.." + (_height - 1) + ").");
        }
        if (relativeColumnIndex < 0 || relativeColumnIndex >= _width) {
            throw new ArrayIndexOutOfBoundsException("Specified colummn " + relativeColumnIndex
                    + " is outside the allowable range (0.." + (_width - 1) + ").");
        }
        int flatIndex = _width * relativeRowIndex + relativeColumnIndex;
        return _flattenedArray[flatIndex];
    }

    public K[] getFlattenedCells() {
        return _flattenedArray.clone();
    }

    public K[][] getCells() {
        Class<?> itemCls = _flattenedArray.getClass();
        @SuppressWarnings("unchecked")
        K[][] result = (K[][]) Array.newInstance(itemCls, _height);
        itemCls = itemCls.getComponentType();
        for (int r = _height - 1; r >= 0; r--) {
            @SuppressWarnings("unchecked")
            K[] row = (K[]) Array.newInstance(itemCls, _width);
            int flatIndex = _width * r;
            System.arraycopy(_flattenedArray, flatIndex, row, 0, _width);
        }
        return result;
    }

    public Iterator<K> iterator() {
        return new ArrayIterator<K>(_flattenedArray);
    }

    private static final class ArrayIterator<D> implements Iterator<D> {

        private final D[] _array;
        private int _index;

        public ArrayIterator(D[] array) {
            _array = array;
            _index = 0;
        }

        public boolean hasNext() {
            return _index < _array.length;
        }

        public D next() {
            if (_index >= _array.length) {
                throw new NoSuchElementException(String.valueOf(_index));
            }
            return _array[_index++];
        }

        public void remove() {
            throw new UnsupportedOperationException("Cannot remove cells from this CellRange.");
        }
    }
}
