package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.model.CellElement;

public class BreakPagesCell {

    private final CellElement cell;

    private final long breakOffset;

    public BreakPagesCell(CellElement cell, long breakOffset) {
        this.cell = cell;
        this.breakOffset = breakOffset;
    }

    public CellElement getCell() {
        return cell;
    }

    public long getBreakOffset() {
        return breakOffset;
    }

}
