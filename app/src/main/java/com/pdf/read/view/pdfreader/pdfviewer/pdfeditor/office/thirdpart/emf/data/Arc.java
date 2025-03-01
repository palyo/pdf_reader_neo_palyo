package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Arc2D;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class Arc extends AbstractArc {

    public Arc() {
        super(45, 1, null, null, null);
    }

    public Arc(Rectangle bounds, Point start, Point end) {
        super(45, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Arc(
                emf.readRECTL(),
                emf.readPOINTL(),
                emf.readPOINTL());
    }

    public void render(EMFRenderer renderer) {

        renderer.fillAndDrawOrAppend(
                getShape(renderer, Arc2D.OPEN));
    }
}
