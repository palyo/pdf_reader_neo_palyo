package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Arc2D;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class ArcTo extends AbstractArc {

    public ArcTo() {
        super(55, 1, null, null, null);
    }

    public ArcTo(Rectangle bounds, Point start, Point end) {
        super(55, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ArcTo(
                emf.readRECTL(),
                emf.readPOINTL(),
                emf.readPOINTL());
    }

    public void render(EMFRenderer renderer) {

        renderer.getFigure().append(
                getShape(renderer, Arc2D.OPEN), true);
    }
}
