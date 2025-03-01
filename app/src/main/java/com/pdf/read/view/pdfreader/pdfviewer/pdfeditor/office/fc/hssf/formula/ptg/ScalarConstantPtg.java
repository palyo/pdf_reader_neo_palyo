package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public abstract class ScalarConstantPtg extends Ptg {
    public final boolean isBaseToken() {
        return true;
    }

    public final byte getDefaultOperandClass() {
        return Ptg.CLASS_VALUE;
    }

    public final String toString() {
        String sb = getClass().getName() + " [" +
                toFormulaString() +
                "]";
        return sb;
    }
}
