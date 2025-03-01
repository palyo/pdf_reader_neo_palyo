package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.AffineTransform;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SetMapMode extends EMFTag implements EMFConstants {

    private int mode;

    public SetMapMode() {
        super(17, 1);
    }

    public SetMapMode(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SetMapMode(emf.readDWORD());
    }

    public String toString() {
        return super.toString() + "\n  mode: " + mode;
    }

    public void render(EMFRenderer renderer) {

        if (mode == EMFConstants.MM_ANISOTROPIC) {
            renderer.setMapModeIsotropic(false);
        } else if (mode == EMFConstants.MM_HIENGLISH) {

            double scale = 0.001 * 25.4;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        } else if (mode == EMFConstants.MM_HIMETRIC) {

            double scale = 0.01;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        } else if (mode == EMFConstants.MM_ISOTROPIC) {
            renderer.setMapModeIsotropic(true);
            renderer.fixViewportSize();
        } else if (mode == EMFConstants.MM_LOENGLISH) {

            double scale = 0.01 * 25.4;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        } else if (mode == EMFConstants.MM_LOMETRIC) {

            double scale = 0.1;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        } else if (mode == EMFConstants.MM_TEXT) {
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(1, -1));
        } else if (mode == EMFConstants.MM_TWIPS) {
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(EMFRenderer.TWIP_SCALE,
                    EMFRenderer.TWIP_SCALE));
        }
    }
}
