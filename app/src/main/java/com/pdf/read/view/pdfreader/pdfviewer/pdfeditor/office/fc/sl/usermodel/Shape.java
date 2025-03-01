package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Rectangle2D;

public interface Shape {
    int getShapeType();

    Rectangle2D getAnchor();

    void setAnchor(Rectangle2D anchor);

    void moveTo(float x, float y);

    Shape getParent();
}
