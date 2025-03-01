package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg;

public abstract class Gradient extends AShader {

    public static final int COORDINATE_LENGTH = 100;
    protected int[] colors = null;
    protected float[] positions;
    private int focus = 100;

    public Gradient(int[] colors, float[] positions) {
        if (colors != null && colors.length >= 2) {
            this.colors = colors;
        }

        this.positions = positions;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
