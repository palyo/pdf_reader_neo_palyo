package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.GeneralPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class EMFPolygon extends AbstractPolygon {

    public EMFPolygon() {
        super(3, 1, null, 0, null);
    }

    public EMFPolygon(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(3, 1, bounds, numberOfPoints, points);
    }

    protected EMFPolygon(int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new EMFPolygon(r, n, emf.readPOINTL(n));
    }

    public void render(EMFRenderer renderer) {
        Point[] points = getPoints();

        if (points.length > 1) {
            GeneralPath path = new GeneralPath(
                    renderer.getWindingRule());
            path.moveTo((float) points[0].x, (float) points[0].y);
            for (int i = 1; i < points.length; i++) {
                path.lineTo((float) points[i].x, (float) points[i].y);
            }
            path.closePath();
            renderer.fillAndDrawOrAppend(path);
        }
    }
}
