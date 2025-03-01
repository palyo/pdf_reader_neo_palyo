package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;

public class Line extends Border {

    private BackgroundAndFill bgFill;
    private boolean dash;

    public Line() {
        setLineWidth((short) 1);
    }

    public BackgroundAndFill getBackgroundAndFill() {
        return bgFill;
    }

    public void setBackgroundAndFill(BackgroundAndFill bgFill) {
        this.bgFill = bgFill;
    }

    public boolean isDash() {
        return dash;
    }

    public void setDash(boolean dash) {
        this.dash = dash;
    }

    public void dispose() {
        if (bgFill != null) {
            bgFill = null;
        }
    }
}
