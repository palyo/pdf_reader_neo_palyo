package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import java.util.ArrayList;

public final class Table extends Range {
    private final int _tableLevel;
    private ArrayList<TableRow> _rows;
    private boolean _rowsFound = false;

    Table(int startIdxInclusive, int endIdxExclusive, Range parent,
          int levelNum) {
        super(startIdxInclusive, endIdxExclusive, parent);
        _tableLevel = levelNum;
        initRows();
    }

    public TableRow getRow(int index) {
        initRows();
        return _rows.get(index);
    }

    public int getTableLevel() {
        return _tableLevel;
    }

    private void initRows() {
        if (_rowsFound)
            return;

        _rows = new ArrayList<TableRow>();
        int rowStart = 0;
        int rowEnd = 0;

        int numParagraphs = numParagraphs();
        while (rowEnd < numParagraphs) {
            Paragraph startRowP = getParagraph(rowStart);
            Paragraph endRowP = getParagraph(rowEnd);
            rowEnd++;
            if (endRowP.isTableRowEnd()
                    && endRowP.getTableLevel() == _tableLevel) {
                _rows.add(new TableRow(startRowP.getStartOffset(), endRowP
                        .getEndOffset(), this, _tableLevel));
                rowStart = rowEnd;
            }
        }
        _rowsFound = true;
    }

    public int numRows() {
        initRows();
        return _rows.size();
    }

    @Override
    protected void reset() {
        _rowsFound = false;
    }

    public int type() {
        return TYPE_TABLE;
    }
}
