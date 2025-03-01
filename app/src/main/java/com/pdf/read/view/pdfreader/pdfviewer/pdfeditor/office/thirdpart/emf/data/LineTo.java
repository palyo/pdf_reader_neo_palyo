package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.GeneralPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class LineTo extends EMFTag {

    private Point point;

    public LineTo() {
        super(54, 1);
    }

    public LineTo(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new LineTo(emf.readPOINTL());
    }

    public String toString() {
        return super.toString() + "\n  point: " + point;
    }

    public void render(EMFRenderer renderer) {

        GeneralPath currentFigure = renderer.getFigure();
        if (currentFigure != null) {
            currentFigure.lineTo((float) point.x, (float) point.y);
            renderer.drawShape(currentFigure);
        } else {
            currentFigure = new GeneralPath(renderer.getWindingRule());
            currentFigure.moveTo((float) point.x, (float) point.y);
            renderer.setFigure(currentFigure);
        }
    }
}
