package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.font;

import java.io.IOException;

public class TTFMaxPTable extends TTFVersionTable {

    public int numGlyphs;

    public int maxPoints, maxContours;

    public int maxCompositePoints, maxCompositeContours;

    public int maxZones;

    public int maxTwilightPoints;

    public int maxStorage;

    public int maxFunctionDefs;

    public int maxInstructionDefs;

    public int maxStackElements;

    public int maxSizeOfInstructions;

    public int maxComponentElements;

    public int maxComponentDepth;

    public String getTag() {
        return "maxp";
    }

    public void readTable() throws IOException {
        readVersion();

        numGlyphs = ttf.readUShort();

        maxPoints = ttf.readUShort();
        maxContours = ttf.readUShort();
        maxCompositePoints = ttf.readUShort();
        maxCompositeContours = ttf.readUShort();
        maxZones = ttf.readUShort();
        maxTwilightPoints = ttf.readUShort();
        maxStorage = ttf.readUShort();
        maxFunctionDefs = ttf.readUShort();
        maxInstructionDefs = ttf.readUShort();
        maxStackElements = ttf.readUShort();
        maxSizeOfInstructions = ttf.readUShort();
        maxComponentElements = ttf.readUShort();
        maxComponentDepth = ttf.readUShort();
    }

    public String toString() {
        return super.toString() + "\n" + "  numGlyphs: " + numGlyphs;
    }
}
