package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

public interface ShapeContainer {
    Shape[] getShapes();

    void addShape(Shape shape);

    boolean removeShape(Shape shape);
}
