package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg;

public final class AddPtg extends ValueOperatorPtg {
    public final static byte sid = 0x03;
    public static final ValueOperatorPtg instance = new AddPtg();
    private final static String ADD = "+";

    private AddPtg() {

    }

    protected byte getSid() {
        return sid;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {

        String buffer = operands[0] +
                ADD +
                operands[1];
        return buffer;
    }
}
