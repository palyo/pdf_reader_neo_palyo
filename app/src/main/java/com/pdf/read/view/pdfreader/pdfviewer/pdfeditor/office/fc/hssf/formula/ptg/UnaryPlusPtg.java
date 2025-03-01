package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class UnaryPlusPtg extends ValueOperatorPtg {
    public final static byte sid = 0x12;
    public static final ValueOperatorPtg instance = new UnaryPlusPtg();
    private final static String ADD = "+";

    private UnaryPlusPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 1;
    }

    public String toFormulaString(String[] operands) {
        String buffer = ADD +
                operands[0];
        return buffer;
    }
}
