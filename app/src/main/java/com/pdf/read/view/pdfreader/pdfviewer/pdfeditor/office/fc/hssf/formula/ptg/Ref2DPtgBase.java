package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellReference;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

abstract class Ref2DPtgBase extends RefPtgBase {
    private final static int SIZE = 5;

    protected Ref2DPtgBase(int row, int column, boolean isRowRelative, boolean isColumnRelative) {
        setRow(row);
        setColumn(column);
        setRowRelative(isRowRelative);
        setColRelative(isColumnRelative);
    }

    protected Ref2DPtgBase(LittleEndianInput in) {
        readCoordinates(in);
    }

    protected Ref2DPtgBase(CellReference cr) {
        super(cr);
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getSid() + getPtgClass());
        writeCoordinates(out);
    }

    public final String toFormulaString() {
        return formatReferenceAsString();
    }

    protected abstract byte getSid();

    public final int getSize() {
        return SIZE;
    }

    public final String toString() {
        String sb = getClass().getName() +
                " [" +
                formatReferenceAsString() +
                "]";
        return sb;
    }
}
