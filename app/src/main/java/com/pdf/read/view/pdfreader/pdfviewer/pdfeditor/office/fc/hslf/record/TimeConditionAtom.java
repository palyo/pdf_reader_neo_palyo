package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class TimeConditionAtom extends PositionDependentRecordAtom {
    public static final int TOT_None = 0;
    public static final int TOT_VisualElement = 1;
    public static final int TOT_TimeNode = 2;
    public static final int TOT_RuntimeNodeRef = 3;
    private static final long _type = 0xF128;
    private final int triggerObject;
    private final int triggerEvent;
    private final int id;
    private final int delay;
    private byte[] _header;

    protected TimeConditionAtom(byte[] source, int start, int len) {
        if (len < 40) {
            len = 40;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        triggerObject = LittleEndian.getInt(source, start + 8);
        triggerEvent = LittleEndian.getInt(source, start + 12);
        id = LittleEndian.getInt(source, start + 16);
        delay = LittleEndian.getInt(source, start + 20);
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
