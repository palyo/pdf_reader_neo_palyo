package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class VisualShapeAtom extends PositionDependentRecordAtom {
    public static final int TVET_Shape = 0;
    public static final int TVET_Page = 1;
    public static final int TVET_TextRange = 2;
    public static final int TVET_Audio = 3;
    public static final int TVET_Video = 4;
    public static final int TVET_ChartElement = 5;
    public static final int TVET_ShapeOnly = 6;
    public static final int TVET_AllTextRange = 8;

    public static final int ET_ShapeType = 1;
    public static final int ET_SoundType = 2;
    public static long RECORD_ID = 0x2AFB;
    private final int animType;
    private final int refType;
    private final int shapeIdRef;
    private final int data1;
    private final int data2;
    private byte[] _header;

    protected VisualShapeAtom(byte[] source, int start, int len) {
        if (len < 28) {
            len = 28;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        animType = LittleEndian.getInt(source, start + 8);
        refType = LittleEndian.getInt(source, start + 12);
        shapeIdRef = LittleEndian.getInt(source, start + 16);

        data1 = LittleEndian.getInt(source, start + 20);
        data2 = LittleEndian.getInt(source, start + 24);
    }

    public long getRecordType() {
        return RECORD_ID;
    }

    public int getTargetElementType() {
        return animType;
    }

    public int getTargetElementID() {
        return shapeIdRef;
    }

    public int getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }

    public void updateOtherRecordReferences(Hashtable<Integer, Integer> oldToNewReferencesLookup) {

    }

    public void dispose() {
        _header = null;
    }
}
