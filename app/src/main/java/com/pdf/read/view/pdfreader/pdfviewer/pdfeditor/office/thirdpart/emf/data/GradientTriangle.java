package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;

public class GradientTriangle extends Gradient {

    private final int vertex1;
    private final int vertex2;
    private final int vertex3;

    public GradientTriangle(int vertex1, int vertex2, int vertex3) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    public GradientTriangle(EMFInputStream emf) throws IOException {
        vertex1 = emf.readULONG();
        vertex2 = emf.readULONG();
        vertex3 = emf.readULONG();
    }

    public String toString() {
        return "  GradientTriangle: " + vertex1 + ", " + vertex2 + ", " + vertex3;
    }
}
