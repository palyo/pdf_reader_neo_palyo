package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class LessThanPtg extends ValueOperatorPtg {
    public final static byte sid = 0x09;
    public static final ValueOperatorPtg instance = new LessThanPtg();
    private final static String LESSTHAN = "<";

    private LessThanPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        String buffer = operands[0] +
                LESSTHAN +
                operands[1];
        return buffer;
    }
}
