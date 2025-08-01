package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font.Font;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;

public class LogFontW implements EMFConstants, GDIObject {

    private final int height;

    private final int width;

    private final int escapement;

    private final int orientation;

    private final int weight;

    private final boolean italic;

    private final boolean underline;

    private final boolean strikeout;

    private final int charSet;

    private final int outPrecision;

    private final int clipPrecision;

    private final int quality;

    private final int pitchAndFamily;

    private final String faceFamily;

    private Font font;

    public LogFontW(int height, int width, int escapement, int orientation, int weight,
                    boolean italic, boolean underline, boolean strikeout, int charSet, int outPrecision,
                    int clipPrecision, int quality, int pitchAndFamily, String faceFamily) {
        this.height = height;
        this.width = width;
        this.escapement = escapement;
        this.orientation = orientation;
        this.weight = weight;
        this.italic = italic;
        this.underline = underline;
        this.strikeout = strikeout;
        this.charSet = charSet;
        this.outPrecision = outPrecision;
        this.clipPrecision = clipPrecision;
        this.quality = quality;
        this.pitchAndFamily = pitchAndFamily;
        this.faceFamily = faceFamily;
    }

    public LogFontW(Font font) {
        this.height = (int) -font.getFontSize();
        this.width = 0;
        this.escapement = 0;
        this.orientation = 0;
        this.weight = font.isBold() ? FW_BOLD : FW_NORMAL;
        this.italic = font.isItalic();
        this.underline = false;
        this.strikeout = false;
        this.charSet = 0;
        this.outPrecision = 0;
        this.clipPrecision = 0;
        this.quality = 4;
        this.pitchAndFamily = 0;
        this.faceFamily = font.getName();
    }

    public LogFontW(EMFInputStream emf) throws IOException {
        height = emf.readLONG();
        width = emf.readLONG();
        escapement = emf.readLONG();
        orientation = emf.readLONG();
        weight = emf.readLONG();
        italic = emf.readBOOLEAN();
        underline = emf.readBOOLEAN();
        strikeout = emf.readBOOLEAN();
        charSet = emf.readBYTE();
        outPrecision = emf.readBYTE();
        clipPrecision = emf.readBYTE();
        quality = emf.readBYTE();
        pitchAndFamily = emf.readBYTE();
        faceFamily = emf.readWCHAR(32);
    }

    public Font getFont() {
        if (font == null) {
            int style = 0;
            if (italic) {
                style |= Font.ITALIC;
            }

            if (weight > 400) {
                style |= Font.BOLD;
            }

            int size = Math.abs(height);
            font = new Font(faceFamily, style, size);

        }
        return font;
    }

    public int getEscapement() {
        return escapement;
    }

    public String toString() {
        return "  LogFontW\n" + "    height: " + height + "\n    width: " + width
                + "\n    orientation: " + orientation + "\n    weight: " + weight + "\n    italic: "
                + italic + "\n    underline: " + underline + "\n    strikeout: " + strikeout
                + "\n    charSet: " + charSet + "\n    outPrecision: " + outPrecision
                + "\n    clipPrecision: " + clipPrecision + "\n    quality: " + quality
                + "\n    pitchAndFamily: " + pitchAndFamily + "\n    faceFamily: " + faceFamily;
    }

    public void render(EMFRenderer renderer) {

        renderer.setFont(font);
    }
}
