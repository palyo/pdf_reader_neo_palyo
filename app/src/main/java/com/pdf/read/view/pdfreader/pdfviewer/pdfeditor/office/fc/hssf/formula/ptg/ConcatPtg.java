package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class ConcatPtg extends ValueOperatorPtg {
    public final static byte sid = 0x08;
    public static final ValueOperatorPtg instance = new ConcatPtg();
    private final static String CONCAT = "&";

    private ConcatPtg() {
    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                CONCAT +
                operands[1];
        return buffer;
    }
}
