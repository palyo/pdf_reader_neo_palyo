package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Bitmap;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFImageLoader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class CreateDIBPatternBrushPt extends EMFTag {

    private int usage;

    private BitmapInfo bmi;

    private Bitmap image;

    private int index;

    public CreateDIBPatternBrushPt() {
        super(94, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        CreateDIBPatternBrushPt tag = new CreateDIBPatternBrushPt();

        tag.index = emf.readDWORD();

        emf.readByte(24);

        tag.bmi = new BitmapInfo(emf);

        tag.usage = emf.readDWORD();

        tag.image = EMFImageLoader.readImage(
                tag.bmi.getHeader(),
                tag.bmi.getHeader().getWidth(),
                tag.bmi.getHeader().getHeight(),
                emf,
                len - 4 - 24 - BitmapInfoHeader.size - 4, null);

        return tag;
    }

    public String toString() {
        return super.toString() +
                "\n  usage: " + usage +
                "\n" + bmi.toString();
    }

    public void render(EMFRenderer renderer) {

        renderer.storeGDIObject(index, new GDIObject() {
            public void render(EMFRenderer renderer) {
                if (image != null) {

                    renderer.setBrushPaint(image);
                }
            }
        });
    }
}
