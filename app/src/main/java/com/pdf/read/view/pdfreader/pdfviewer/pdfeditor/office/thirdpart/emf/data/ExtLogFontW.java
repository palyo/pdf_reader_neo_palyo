package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font.Font;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFRenderer;

public class ExtLogFontW implements EMFConstants, GDIObject {

    private final LogFontW font;

    private final String fullName;

    private final String style;

    private final int version;

    private final int styleSize;

    private final int match;

    private final byte[] vendorID;

    private final int culture;

    private final Panose panose;

    public ExtLogFontW(LogFontW font, String fullName, String style, int version, int styleSize,
                       int match, byte[] vendorID, int culture, Panose panose) {
        this.font = font;
        this.fullName = fullName;
        this.style = style;
        this.version = version;
        this.styleSize = styleSize;
        this.match = match;
        this.vendorID = vendorID;
        this.culture = culture;
        this.panose = panose;
    }

    public ExtLogFontW(Font font) {
        this.font = new LogFontW(font);
        this.fullName = "";
        this.style = "";
        this.version = 0;
        this.styleSize = 0;
        this.match = 0;
        this.vendorID = new byte[]{0, 0, 0, 0};
        this.culture = 0;
        this.panose = new Panose();
    }

    public ExtLogFontW(EMFInputStream emf) throws IOException {
        font = new LogFontW(emf);
        fullName = emf.readWCHAR(64);
        style = emf.readWCHAR(32);
        version = emf.readDWORD();
        styleSize = emf.readDWORD();
        match = emf.readDWORD();
        emf.readDWORD();
        vendorID = emf.readBYTE(4);
        culture = emf.readDWORD();
        panose = new Panose(emf);
        emf.readWORD();

        emf.popBuffer();
    }

    public String toString() {
        return super.toString() + "\n  LogFontW\n" + font.toString() + "\n    fullname: "
                + fullName + "\n    style: " + style + "\n    version: " + version
                + "\n    stylesize: " + styleSize + "\n    match: " + match + "\n    vendorID: "
                + vendorID + "\n    culture: " + culture + "\n" + panose.toString();
    }

    public void render(EMFRenderer renderer) {
        renderer.setFont(font.getFont());
        renderer.setEscapement(font.getEscapement());
    }
}
