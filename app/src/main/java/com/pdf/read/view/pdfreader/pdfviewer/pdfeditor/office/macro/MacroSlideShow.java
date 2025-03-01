package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ISlideShow;

public class MacroSlideShow implements ISlideShow {

    private SlideShowListener listener;

    protected MacroSlideShow(SlideShowListener listener) {
        this.listener = listener;
    }

    public void exit() {
        if (listener != null) {
            listener.exit();
        }
    }

    public void dispose() {
        listener = null;
    }
}
