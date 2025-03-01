package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class DividePtg extends ValueOperatorPtg {
    public final static byte sid = 0x06;

    public static final ValueOperatorPtg instance = new DividePtg();

    private DividePtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                "/" +
                operands[1];
        return buffer;
    }
}
