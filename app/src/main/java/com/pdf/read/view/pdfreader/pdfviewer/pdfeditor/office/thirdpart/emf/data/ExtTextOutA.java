package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class ExtTextOutA extends AbstractExtTextOut implements EMFConstants {

    private TextA text;

    public ExtTextOutA() {
        super(83, 1, null, 0, 1, 1);
    }

    public ExtTextOutA(
            Rectangle bounds,
            int mode,
            float xScale,
            float yScale,
            TextA text) {

        super(83, 1, bounds, mode, xScale, yScale);
        this.text = text;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ExtTextOutA(
                emf.readRECTL(),
                emf.readDWORD(),
                emf.readFLOAT(),
                emf.readFLOAT(),
                TextA.read(emf));
    }

    public Text getText() {
        return text;
    }
}
