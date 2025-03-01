package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.font;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TTFFile extends TTFFont {

    private static final String mode = "r";

    private final String fileName;

    private final RandomAccessFile ttf;

    private final int sfntMajorVersion;

    private final int sfntMinorVersion;

    private final int numberOfTables;

    private final int searchRange;

    private final int entrySelector;

    private final int rangeShift;

    public TTFFile(String name) throws IOException {
        fileName = name;
        ttf = new RandomAccessFile(name, mode);

        ttf.seek(0);
        sfntMajorVersion = ttf.readUnsignedShort();
        sfntMinorVersion = ttf.readUnsignedShort();
        numberOfTables = ttf.readUnsignedShort();
        searchRange = ttf.readUnsignedShort();
        entrySelector = ttf.readUnsignedShort();
        rangeShift = ttf.readUnsignedShort();

        for (int i = 0; i < numberOfTables; i++) {
            ttf.seek(12 + i * 16L);
            byte[] b = new byte[4];
            ttf.readFully(b);
            String tag = new String(b);
            int checksum = ttf.readInt();
            int offset = ttf.readInt();
            int len = ttf.readInt();
            TTFInput input = new TTFFileInput(ttf, offset, len, checksum);
            newTable(tag, input);
        }
    }

    public int getFontVersion() {
        return sfntMajorVersion;
    }

    public void close() throws IOException {
        super.close();
        ttf.close();
    }

    public void show() {
        super.show();

        System.out.println("Font: " + fileName);
        System.out.println("  sfnt: " + sfntMajorVersion + "." + sfntMinorVersion);
        System.out.println("  numTables: " + numberOfTables);
        System.out.println("  searchRange: " + searchRange);
        System.out.println("  entrySelector: " + entrySelector);
        System.out.println("  rangeShift: " + rangeShift);
    }

}
