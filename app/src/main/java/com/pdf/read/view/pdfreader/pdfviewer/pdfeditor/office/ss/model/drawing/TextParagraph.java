package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.drawing;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font.Font;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.style.Alignment;

public class TextParagraph {
    private String textRun;
    private Font font;
    private Alignment align;

    public TextParagraph() {
        align = new Alignment();
    }

    public String getTextRun() {
        return textRun;
    }

    public void setTextRun(String textRun) {
        this.textRun = textRun;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public short getHorizontalAlign() {
        return align.getHorizontalAlign();
    }

    public void setHorizontalAlign(short horizon) {
        align.setHorizontalAlign(horizon);
    }

    public short getVerticalAlign() {
        return align.getVerticalAlign();
    }

    public void setVerticalAlign(short vertical) {
        align.setVerticalAlign(vertical);
    }

    public void dispose() {
        textRun = null;
        font = null;

        if (align != null) {
            align.dispose();
            align = null;
        }

    }
}
