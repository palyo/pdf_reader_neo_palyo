package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class SelectObject extends EMFTag {

    private int index;

    public SelectObject() {
        super(37, 1);
    }

    public SelectObject(int index) {
        this();
        this.index = index;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new SelectObject(emf.readDWORD());
    }

    public String toString() {
        return super.toString() + "\n  index: 0x" + Integer.toHexString(index);
    }

    public void render(EMFRenderer renderer) {
        GDIObject gdiObject;

        if (index < 0) {
            gdiObject = StockObjects.getStockObject(index);
        } else {
            gdiObject = renderer.getGDIObject(index);
        }

        if (gdiObject != null) {

            gdiObject.render(renderer);
        } else {
        }
    }
}
