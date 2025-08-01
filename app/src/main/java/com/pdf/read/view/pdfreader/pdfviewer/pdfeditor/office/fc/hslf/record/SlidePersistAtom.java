package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class SlidePersistAtom extends RecordAtom {
    private static final long _type = 1011L;
    private final boolean hasShapesOtherThanPlaceholders;
    private byte[] _header;
    private int refID;
    private int numPlaceholderTexts;
    private int slideIdentifier;
    private byte[] reservedFields;

    private SlidePersistAtom(byte[] source, int start, int len) {

        if (len < 8) {
            len = 8;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        refID = LittleEndian.getInt(source, start + 8);

        int flags = LittleEndian.getInt(source, start + 12);
        hasShapesOtherThanPlaceholders = flags == 4;

        numPlaceholderTexts = LittleEndian.getInt(source, start + 16);

        slideIdentifier = LittleEndian.getInt(source, start + 20);

        reservedFields = new byte[len - 24];
        System.arraycopy(source, start + 24, reservedFields, 0, reservedFields.length);
    }

    public SlidePersistAtom() {
        _header = new byte[8];
        LittleEndian.putUShort(_header, 0, 0);
        LittleEndian.putUShort(_header, 2, (int) _type);
        LittleEndian.putInt(_header, 4, 20);

        hasShapesOtherThanPlaceholders = true;
        reservedFields = new byte[4];
    }

    public int getRefID() {
        return refID;
    }

    public void setRefID(int id) {
        refID = id;
    }

    public int getSlideIdentifier() {
        return slideIdentifier;
    }

    public void setSlideIdentifier(int id) {
        slideIdentifier = id;
    }

    public int getNumPlaceholderTexts() {
        return numPlaceholderTexts;
    }

    public boolean getHasShapesOtherThanPlaceholders() {
        return hasShapesOtherThanPlaceholders;
    }

    public long getRecordType() {
        return _type;
    }

    public void writeOut(OutputStream out) throws IOException {

        out.write(_header);

        int flags = 0;
        if (hasShapesOtherThanPlaceholders) {
            flags = 4;
        }

        writeLittleEndian(refID, out);
        writeLittleEndian(flags, out);
        writeLittleEndian(numPlaceholderTexts, out);
        writeLittleEndian(slideIdentifier, out);
        out.write(reservedFields);
    }

    public void dispose() {
        _header = null;
        reservedFields = null;
    }
}
