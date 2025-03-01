package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class GreaterEqualPtg extends ValueOperatorPtg {
    public final static int SIZE = 1;
    public final static byte sid = 0x0c;

    public static final ValueOperatorPtg instance = new GreaterEqualPtg();

    private GreaterEqualPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                ">=" +
                operands[1];

        return buffer;
    }
}
