package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class TimeIterateDataAtom extends PositionDependentRecordAtom {
    private static final long _type = 0xF140;
    private final int iterateInterval;
    private final int iterateType;
    private final int iterateDirection;
    private final int iterateIntervalType;
    private byte[] _header;
    private boolean fIterateDirectionPropertyUsed;
    private boolean fIterateTypePropertyUsed;
    private boolean fIterateIntervalPropertyUsed;
    private boolean fIterateIntervalTypePropertyUsed;

    private byte[] reserved;

    protected TimeIterateDataAtom(byte[] source, int start, int len) {
        if (len < 28) {
            len = 28;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        iterateInterval = LittleEndian.getInt(source, start + 8);
        iterateType = LittleEndian.getInt(source, start + 12);
        iterateDirection = LittleEndian.getInt(source, start + 16);
        iterateIntervalType = LittleEndian.getInt(source, start + 20);

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
