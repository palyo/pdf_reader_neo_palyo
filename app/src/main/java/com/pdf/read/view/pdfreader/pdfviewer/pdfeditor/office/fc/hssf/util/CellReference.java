package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.util;

public final class CellReference extends com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellReference {

    public CellReference(String cellRef) {
        super(cellRef);
    }

    public CellReference(int pRow, int pCol) {
        super(pRow, pCol, true, true);
    }

    public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        super(null, pRow, pCol, pAbsRow, pAbsCol);
    }

    public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        super(pSheetName, pRow, pCol, pAbsRow, pAbsCol);
    }
}
