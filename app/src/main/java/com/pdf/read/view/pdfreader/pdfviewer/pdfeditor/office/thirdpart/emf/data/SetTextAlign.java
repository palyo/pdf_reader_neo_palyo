package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SetTextAlign extends EMFTag implements EMFConstants {

    private int mode;

    public SetTextAlign() {
        super(22, 1);
    }

    public SetTextAlign(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SetTextAlign(emf.readDWORD());
    }

    public String toString() {
        return super.toString() + "\n  mode: " + mode;
    }

    public void render(EMFRenderer renderer) {
        renderer.setTextAlignMode(mode);
    }
}
