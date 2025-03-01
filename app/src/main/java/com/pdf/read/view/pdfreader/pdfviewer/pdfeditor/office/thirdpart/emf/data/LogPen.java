package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;

public class LogPen extends AbstractPen {

    private final int penStyle;

    private final int width;

    private final Color color;

    public LogPen(int penStyle, int width, Color color) {
        this.penStyle = penStyle;
        this.width = width;
        this.color = color;
    }

    public LogPen(EMFInputStream emf) throws IOException {
        penStyle = emf.readDWORD();
        width = emf.readDWORD();

        emf.readDWORD();
        color = emf.readCOLORREF();
    }

    public String toString() {
        return "  LogPen\n" + "    penstyle: " + penStyle + "\n    width: " + width
                + "\n    color: " + color;
    }

    public void render(EMFRenderer renderer) {
        renderer.setUseCreatePen(true);
        renderer.setPenPaint(color);
        renderer.setPenStroke(createStroke(renderer, penStyle, null, width));
    }
}
