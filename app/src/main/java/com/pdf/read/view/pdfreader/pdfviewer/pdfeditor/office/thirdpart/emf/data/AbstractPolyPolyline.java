package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;

public abstract class AbstractPolyPolyline extends AbstractPolyPolygon {

    protected AbstractPolyPolyline(
            int id,
            int version,
            Rectangle bounds,
            int[] numberOfPoints,
            Point[][] points) {

        super(id, version, bounds, numberOfPoints, points);
    }

    public void render(EMFRenderer renderer) {
        render(renderer, false);
    }
}
