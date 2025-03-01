package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class TimeNodeAtom extends PositionDependentRecordAtom {
    public static final int TNT_Parallel = 0;
    public static final int TNT_Sequential = 1;
    public static final int TNT_Behavior = 2;
    public static final int TNT__Media = 3;
    private static final long _type = 0xF127;
    private final int reserved1;
    private final int restart;
    private final int timeNodeType;
    private final int fill;
    private final int duration;
    private final boolean fFillProperty;
    private final boolean fRestartProperty;
    private final boolean fGroupingTypeProperty;
    private final boolean fDurationProperty;
    private byte[] _header;
    private int reserved2;
    private byte reserved3;
    private int unused;
    private boolean reserved4;
    private byte[] reserved5;

    protected TimeNodeAtom(byte[] source, int start, int len) {
        if (len < 40) {
            len = 40;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        reserved1 = LittleEndian.getInt(source, start + 8);
        restart = LittleEndian.getInt(source, start + 12);

        timeNodeType = LittleEndian.getInt(source, start + 16);
        fill = LittleEndian.getInt(source, start + 20);

        duration = LittleEndian.getInt(source, start + 32);

        byte b = source[start + 36];
        fDurationProperty = ((b & 0x10)) >> 4 > 0;
        fGroupingTypeProperty = ((b & 0x8) >> 3) > 0;

        fRestartProperty = ((b & 0x2) >> 1) > 0;
        fFillProperty = ((b & 0x1)) > 0;
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
