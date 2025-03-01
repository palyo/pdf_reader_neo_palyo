package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class TimeSequenceDataAtom extends PositionDependentRecordAtom {
    private static final long _type = 0xF141;
    private final int concurrency;
    private final int nextAction;
    private final int previousAction;
    private final int reserved1;
    private byte[] _header;
    private boolean fConcurrencyPropertyUsed;
    private boolean fNextActionPropertyUsed;
    private boolean fPreviousActionPropertyUsed;

    private byte[] reserved2;

    protected TimeSequenceDataAtom(byte[] source, int start, int len) {
        if (len < 28) {
            len = 28;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        concurrency = LittleEndian.getInt(source, start + 8);
        nextAction = LittleEndian.getInt(source, start + 12);
        previousAction = LittleEndian.getInt(source, start + 16);
        reserved1 = LittleEndian.getInt(source, start + 20);

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
