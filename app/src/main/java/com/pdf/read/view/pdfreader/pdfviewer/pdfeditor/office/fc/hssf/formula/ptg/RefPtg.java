package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellReference;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;

public final class RefPtg extends Ref2DPtgBase {
    public final static byte sid = 0x24;

    public RefPtg(String cellref) {
        super(new CellReference(cellref));
    }

    public RefPtg(int row, int column, boolean isRowRelative, boolean isColumnRelative) {
        super(row, column, isRowRelative, isColumnRelative);
    }

    public RefPtg(LittleEndianInput in) {
        super(in);
    }

    public RefPtg(CellReference cr) {
        super(cr);
    }

    protected byte getSid() {
        return sid;
    }
}
