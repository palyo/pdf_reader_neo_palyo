package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class PercentPtg extends ValueOperatorPtg {
    public final static int SIZE = 1;
    public final static byte sid = 0x14;
    public static final ValueOperatorPtg instance = new PercentPtg();
    private final static String PERCENT = "%";

    private PercentPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 1;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                PERCENT;
        return buffer;
    }
}
