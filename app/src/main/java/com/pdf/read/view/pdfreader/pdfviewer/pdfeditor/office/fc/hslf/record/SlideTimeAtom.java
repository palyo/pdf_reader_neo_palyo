package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class SlideTimeAtom extends PositionDependentRecordAtom {
    private static final long _type = 12011;
    private final long fileTime;
    private byte[] _header;

    protected SlideTimeAtom(byte[] source, int start, int len) {

        if (len < 16) {
            len = 16;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        fileTime = LittleEndian.getLong(source, start + 8);
    }

    public long getSlideCreateTime() {
        return fileTime;
    }

    public long getRecordType() {
        return _type;
    }

    public void updateOtherRecordReferences(Hashtable<Integer, Integer> oldToNewReferencesLookup) {

    }

    public void dispose() {
        _header = null;
    }
}
