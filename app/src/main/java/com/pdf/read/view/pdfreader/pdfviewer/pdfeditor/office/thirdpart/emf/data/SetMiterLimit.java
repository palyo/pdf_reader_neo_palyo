package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SetMiterLimit extends EMFTag {

    private int limit;

    public SetMiterLimit() {
        super(58, 1);
    }

    public SetMiterLimit(int limit) {
        this();
        this.limit = limit;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SetMiterLimit(emf.readDWORD());
    }

    public String toString() {
        return super.toString() + "\n  limit: " + limit;
    }

    public void render(EMFRenderer renderer) {

        renderer.setMeterLimit(limit);
    }
}
