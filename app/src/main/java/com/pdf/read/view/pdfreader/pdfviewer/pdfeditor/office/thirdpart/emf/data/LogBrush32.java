package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;
import java.util.logging.Logger;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;

public class LogBrush32 implements EMFConstants, GDIObject {

    private final int style;

    private final Color color;

    private final int hatch;

    public LogBrush32(int style, Color color, int hatch) {
        this.style = style;
        this.color = color;
        this.hatch = hatch;
    }

    public LogBrush32(EMFInputStream emf) throws IOException {
        style = emf.readUINT();
        color = emf.readCOLORREF();
        hatch = emf.readULONG();
    }

    public String toString() {
        return "  LogBrush32\n" + "    style: " + style + "\n    color: " + color + "\n    hatch: "
                + hatch;
    }

    public void render(EMFRenderer renderer) {
        if (style == EMFConstants.BS_SOLID) {
            renderer.setBrushPaint(color);
        } else if (style == EMFConstants.BS_NULL) {

            renderer.setBrushPaint(new Color(0, 0, 0, 0));

        } else {
            Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");
            logger.warning("LogBrush32 style not supported: " + this);
            renderer.setBrushPaint(color);
        }
    }
}
