package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class GreaterThanPtg extends ValueOperatorPtg {
    public final static byte sid = 0x0D;
    public static final ValueOperatorPtg instance = new GreaterThanPtg();
    private final static String GREATERTHAN = ">";

    private GreaterThanPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                GREATERTHAN +
                operands[1];
        return buffer;
    }
}
