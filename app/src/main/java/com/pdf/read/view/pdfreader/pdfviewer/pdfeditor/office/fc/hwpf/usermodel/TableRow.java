package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm.SprmBuffer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm.TableSprmUncompressor;

public final class TableRow extends Range {

    private final static short SPRM_DXAGAPHALF = (short) 0x9602;
    private final static short SPRM_DYAROWHEIGHT = (short) 0x9407;
    private final static short SPRM_FCANTSPLIT = 0x3403;
    private final static short SPRM_FTABLEHEADER = 0x3404;
    private final static short SPRM_TJC = 0x5400;

    private final static char TABLE_CELL_MARK = '\u0007';
    private final SprmBuffer _papx;
    private final TableProperties _tprops;
    int _levelNum;
    private TableCell[] _cells;
    private boolean _cellsFound = false;

    public TableRow(int startIdxInclusive, int endIdxExclusive, Table parent, int levelNum) {
        super(startIdxInclusive, endIdxExclusive, parent);

        Paragraph last = getParagraph(numParagraphs() - 1);
        _papx = last._papx;
        _tprops = TableSprmUncompressor.uncompressTAP(_papx);
        _levelNum = levelNum;
        initCells();
    }

    public boolean cantSplit() {
        return _tprops.getFCantSplit();
    }

    public BorderCode getBarBorder() {
        throw new UnsupportedOperationException("not applicable for TableRow");
    }

    public BorderCode getBottomBorder() {
        return _tprops.getBrcBottom();
    }

    public TableCell getCell(int index) {
        initCells();
        return _cells[index];
    }

    public int getGapHalf() {
        return _tprops.getDxaGapHalf();
    }

    public void setGapHalf(int dxaGapHalf) {
        _tprops.setDxaGapHalf(dxaGapHalf);
        _papx.updateSprm(SPRM_DXAGAPHALF, (short) dxaGapHalf);
    }

    public BorderCode getHorizontalBorder() {
        return _tprops.getBrcHorizontal();
    }

    public BorderCode getLeftBorder() {
        return _tprops.getBrcLeft();
    }

    public BorderCode getRightBorder() {
        return _tprops.getBrcRight();
    }

    public int getRowHeight() {
        return _tprops.getDyaRowHeight();
    }

    public void setRowHeight(int dyaRowHeight) {
        _tprops.setDyaRowHeight(dyaRowHeight);
        _papx.updateSprm(SPRM_DYAROWHEIGHT, (short) dyaRowHeight);
    }

    public int getRowJustification() {
        return _tprops.getJc();
    }

    public void setRowJustification(int jc) {
        _tprops.setJc((short) jc);
        _papx.updateSprm(SPRM_TJC, (short) jc);
    }

    public int getTableIndent() {
        return _tprops.getTableInIndent();
    }

    public int getCellSpacingDefault() {
        return _tprops.getTCellSpacingDefault();
    }

    public BorderCode getTopBorder() {
        return _tprops.getBrcBottom();
    }

    public BorderCode getVerticalBorder() {
        return _tprops.getBrcVertical();
    }

    private void initCells() {
        if (_cellsFound)
            return;

        final short expectedCellsCount = _tprops.getItcMac();

        int lastCellStart = 0;
        List<TableCell> cells = new ArrayList<TableCell>(expectedCellsCount + 1);
        short preLeftEdge = -1;
        for (int p = 0; p < numParagraphs(); p++) {
            Paragraph paragraph = getParagraph(p);
            String s = paragraph.text();

            if (((s.length() > 0 && s.charAt(s.length() - 1) == TABLE_CELL_MARK) || paragraph
                    .isEmbeddedCellMark()) && paragraph.getTableLevel() == _levelNum) {
                TableCellDescriptor tableCellDescriptor = _tprops.getRgtc() != null
                        && _tprops.getRgtc().length > cells.size() ? _tprops.getRgtc()[cells.size()]
                        : new TableCellDescriptor();
                if (tableCellDescriptor == null) {
                    tableCellDescriptor = new TableCellDescriptor();
                }
                short leftEdge = _tprops.getRgdxaCenter() != null
                        && _tprops.getRgdxaCenter().length > cells.size()
                        ? _tprops.getRgdxaCenter()[cells.size()] : 0;
                short rightEdge = _tprops.getRgdxaCenter() != null
                        && _tprops.getRgdxaCenter().length > cells.size() + 1 ? _tprops
                        .getRgdxaCenter()[cells.size() + 1] : 0;
                int cellWidth = rightEdge - leftEdge;
                if (cells.size() == 0 || cells.size() + 2 >= _tprops.getRgdxaCenter().length) {
                    cellWidth -= _tprops.getTCellSpacingDefault() * 2;
                }
                TableCell tableCell = new TableCell(getParagraph(lastCellStart).getStartOffset(),
                        getParagraph(p).getEndOffset(), this, _levelNum, tableCellDescriptor, leftEdge,
                        cellWidth);
                cells.add(tableCell);
                preLeftEdge = leftEdge;
                lastCellStart = p + 1;
            }
        }

        if (lastCellStart < (numParagraphs() - 1)) {
            TableCellDescriptor tableCellDescriptor = _tprops.getRgtc() != null
                    && _tprops.getRgtc().length > cells.size() ? _tprops.getRgtc()[cells.size()]
                    : new TableCellDescriptor();
            final short leftEdge = _tprops.getRgdxaCenter() != null
                    && _tprops.getRgdxaCenter().length > cells.size() ? _tprops.getRgdxaCenter()[cells
                    .size()] : 0;
            final short rightEdge = _tprops.getRgdxaCenter() != null
                    && _tprops.getRgdxaCenter().length > cells.size() + 1
                    ? _tprops.getRgdxaCenter()[cells.size() + 1] : 0;

            TableCell tableCell = new TableCell(lastCellStart, (numParagraphs() - 1), this,
                    _levelNum, tableCellDescriptor, leftEdge, rightEdge - leftEdge);
            cells.add(tableCell);
        }

        if (!cells.isEmpty()) {
            TableCell lastCell = cells.get(cells.size() - 1);
            if (lastCell.numParagraphs() == 1 && (lastCell.getParagraph(0).isTableRowEnd())) {

                cells.remove(cells.size() - 1);
            }
        }

        if (cells.size() != expectedCellsCount) {

            _tprops.setItcMac((short) cells.size());
        }

        _cells = cells.toArray(new TableCell[cells.size()]);
        _cellsFound = true;
    }

    public boolean isTableHeader() {
        return _tprops.getFTableHeader();
    }

    public void setTableHeader(boolean tableHeader) {
        _tprops.setFTableHeader(tableHeader);
        _papx.updateSprm(SPRM_FTABLEHEADER, (byte) (tableHeader ? 1 : 0));
    }

    public int numCells() {
        initCells();
        return _cells.length;
    }

    @Override
    protected void reset() {
        _cellsFound = false;
    }

    public void setCantSplit(boolean cantSplit) {
        _tprops.setFCantSplit(cantSplit);
        _papx.updateSprm(SPRM_FCANTSPLIT, (byte) (cantSplit ? 1 : 0));
    }

}
