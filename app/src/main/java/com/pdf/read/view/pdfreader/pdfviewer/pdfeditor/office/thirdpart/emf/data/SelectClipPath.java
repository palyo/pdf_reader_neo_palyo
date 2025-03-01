package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SelectClipPath extends AbstractClipPath {

    public SelectClipPath() {
        super(67, 1, EMFConstants.RGN_AND);
    }

    public SelectClipPath(int mode) {
        super(67, 1, mode);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SelectClipPath(emf.readDWORD());
    }

    public void render(EMFRenderer renderer) {
        render(renderer, renderer.getPath());
    }
}
