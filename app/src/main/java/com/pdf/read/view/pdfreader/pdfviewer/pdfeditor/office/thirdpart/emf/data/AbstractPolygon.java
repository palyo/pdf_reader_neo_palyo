package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public abstract class AbstractPolygon extends EMFTag {

    private Rectangle bounds;

    private int numberOfPoints;

    private Point[] points;

    protected AbstractPolygon(int id, int version) {
        super(id, version);
    }

    protected AbstractPolygon(int id, int version, Rectangle bounds, int numberOfPoints,
                              Point[] points) {
        super(id, version);
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public String toString() {
        String result = super.toString() + "\n  bounds: " + bounds + "\n  #points: "
                + numberOfPoints;
        if (points != null) {
            result += "\n  points: ";
            for (int i = 0; i < points.length; i++) {
                result += "[" + points[i].x + "," + points[i].y + "]";
                if (i < points.length - 1) {
                    result += ", ";
                }
            }
        }
        return result;
    }

    protected Rectangle getBounds() {
        return bounds;
    }

    protected int getNumberOfPoints() {
        return numberOfPoints;
    }

    protected Point[] getPoints() {
        return points;
    }
}
