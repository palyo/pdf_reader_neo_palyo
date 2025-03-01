package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class EqualPtg extends ValueOperatorPtg {
    public final static byte sid = 0x0b;

    public static final ValueOperatorPtg instance = new EqualPtg();

    private EqualPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                "=" +
                operands[1];
        return buffer;
    }
}
