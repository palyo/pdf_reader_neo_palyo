package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;

public class BlendFunction implements EMFConstants {

    public static final int size = 4;

    private final int blendOp;

    private final int blendFlags;

    private final int sourceConstantAlpha;

    private final int alphaFormat;

    public BlendFunction(int blendOp, int blendFlags, int sourceConstantAlpha, int alphaFormat) {
        this.blendOp = blendOp;
        this.blendFlags = blendFlags;
        this.sourceConstantAlpha = sourceConstantAlpha;
        this.alphaFormat = alphaFormat;
    }

    public BlendFunction(EMFInputStream emf) throws IOException {
        blendOp = emf.readUnsignedByte();
        blendFlags = emf.readUnsignedByte();
        sourceConstantAlpha = emf.readUnsignedByte();
        alphaFormat = emf.readUnsignedByte();
    }

    public String toString() {
        return "BlendFunction";
    }

    public int getSourceConstantAlpha() {
        return sourceConstantAlpha;
    }

    public int getAlphaFormat() {
        return alphaFormat;
    }
}
