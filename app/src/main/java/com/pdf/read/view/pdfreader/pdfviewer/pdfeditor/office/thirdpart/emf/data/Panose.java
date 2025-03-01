package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;

public class Panose implements EMFConstants {

    private final int familyType;

    private final int serifStyle;

    private final int weight;

    private final int proportion;

    private final int contrast;

    private final int strokeVariation;

    private final int armStyle;

    private final int letterForm;

    private final int midLine;

    private final int xHeight;

    public Panose() {

        this.familyType = PAN_NO_FIT;
        this.serifStyle = PAN_NO_FIT;
        this.proportion = PAN_NO_FIT;
        this.weight = PAN_NO_FIT;
        this.contrast = PAN_NO_FIT;
        this.strokeVariation = PAN_NO_FIT;
        this.armStyle = PAN_ANY;
        this.letterForm = PAN_ANY;
        this.midLine = PAN_ANY;
        this.xHeight = PAN_ANY;
    }

    public Panose(EMFInputStream emf) throws IOException {
        familyType = emf.readBYTE();
        serifStyle = emf.readBYTE();
        proportion = emf.readBYTE();
        weight = emf.readBYTE();
        contrast = emf.readBYTE();
        strokeVariation = emf.readBYTE();
        armStyle = emf.readBYTE();
        letterForm = emf.readBYTE();
        midLine = emf.readBYTE();
        xHeight = emf.readBYTE();
    }

    public String toString() {
        return "  Panose\n" + "    familytype: " + familyType + "\n    serifStyle: " + serifStyle
                + "\n    weight: " + weight + "\n    proportion: " + proportion + "\n    contrast: "
                + contrast + "\n    strokeVariation: " + strokeVariation + "\n    armStyle: "
                + armStyle + "\n    letterForm: " + letterForm + "\n    midLine: " + midLine
                + "\n    xHeight: " + xHeight;
    }
}
