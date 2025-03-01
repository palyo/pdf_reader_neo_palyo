package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.GeneralPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SetPolyFillMode extends EMFTag implements EMFConstants {

    private int mode;

    public SetPolyFillMode() {
        super(19, 1);
    }

    public SetPolyFillMode(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SetPolyFillMode(emf.readDWORD());
    }

    public String toString() {
        return super.toString() + "\n  mode: " + mode;
    }

    public void render(EMFRenderer renderer) {
        renderer.setWindingRule(getWindingRule(mode));
    }

    private int getWindingRule(int polyFillMode) {
        if (polyFillMode == EMFConstants.WINDING) {
            return GeneralPath.WIND_EVEN_ODD;
        } else if (polyFillMode == EMFConstants.ALTERNATE) {
            return GeneralPath.WIND_NON_ZERO;
        } else {
            return GeneralPath.WIND_EVEN_ODD;
        }
    }

}
