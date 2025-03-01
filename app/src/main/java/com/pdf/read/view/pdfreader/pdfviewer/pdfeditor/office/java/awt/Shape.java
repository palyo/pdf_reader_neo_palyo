package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.AffineTransform;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.PathIterator;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Point2D;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Rectangle2D;

public interface Shape {

    Rectangle getBounds();

    Rectangle2D getBounds2D();

    boolean contains(double x, double y);

    boolean contains(Point2D p);

    boolean intersects(double x, double y, double w, double h);

    boolean intersects(Rectangle2D r);

    boolean contains(double x, double y, double w, double h);

    boolean contains(Rectangle2D r);

    PathIterator getPathIterator(AffineTransform at);

    PathIterator getPathIterator(AffineTransform at, double flatness);
}
