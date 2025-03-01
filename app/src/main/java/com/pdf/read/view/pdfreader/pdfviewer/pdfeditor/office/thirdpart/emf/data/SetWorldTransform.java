package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.AffineTransform;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SetWorldTransform extends EMFTag {

    private AffineTransform transform;

    public SetWorldTransform() {
        super(35, 1);
    }

    public SetWorldTransform(AffineTransform transform) {
        this();
        this.transform = transform;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SetWorldTransform(emf.readXFORM());
    }

    public String toString() {
        return super.toString() + "\n  transform: " + transform;
    }

    public void render(EMFRenderer renderer) {
        if (renderer.getPath() != null) {
            renderer.setPathTransform(transform);
        } else {
            renderer.resetTransformation();
            renderer.transform(transform);
        }
    }
}
