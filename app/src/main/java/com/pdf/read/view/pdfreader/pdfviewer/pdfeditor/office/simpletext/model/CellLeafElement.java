package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Cell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Workbook;

public class CellLeafElement extends LeafElement {

    private final int sharedStringIndex;

    private final int offStart;
    private final int offEnd;
    private Workbook book;
    private boolean appendNewline;

    public CellLeafElement(Cell cell, int offStart, int offEnd) {
        super(null);

        book = cell.getSheet().getWorkbook();

        this.sharedStringIndex = cell.getStringCellValueIndex();
        this.offStart = offStart;
        this.offEnd = offEnd;
    }

    public String getText(IDocument doc) {
        if (appendNewline) {
            return book.getSharedString(sharedStringIndex).substring(offStart, offEnd) + "\n";
        } else {
            return book.getSharedString(sharedStringIndex).substring(offStart, offEnd);
        }

    }

    public void appendNewlineFlag() {
        appendNewline = true;
    }

    public void dispose() {
        book = null;
    }
}
