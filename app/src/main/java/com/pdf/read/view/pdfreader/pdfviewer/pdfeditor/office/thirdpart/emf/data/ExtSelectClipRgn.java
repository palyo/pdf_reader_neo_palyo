package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class ExtSelectClipRgn extends AbstractClipPath {

    private Region rgn;

    public ExtSelectClipRgn() {
        super(75, 1, EMFConstants.RGN_COPY);
    }

    public ExtSelectClipRgn(int mode, Region rgn) {
        super(75, 1, mode);
        this.rgn = rgn;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        int length = emf.readDWORD();
        int mode = emf.readDWORD();
        return new ExtSelectClipRgn(mode, length > 8 ? new Region(emf) : null);
    }

    public void render(EMFRenderer renderer) {
        if (rgn == null || rgn.getBounds() == null) {
            return;
        }

        render(renderer, rgn.getBounds());
    }
}
