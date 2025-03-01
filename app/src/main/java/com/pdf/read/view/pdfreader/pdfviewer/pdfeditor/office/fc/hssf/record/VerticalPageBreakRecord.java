package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import java.util.Iterator;

public final class VerticalPageBreakRecord extends PageBreakRecord {

    public static final short sid = 0x001A;

    public VerticalPageBreakRecord() {

    }

    public VerticalPageBreakRecord(RecordInputStream in) {
        super(in);
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PageBreakRecord result = new VerticalPageBreakRecord();
        Iterator iterator = getBreaksIterator();
        while (iterator.hasNext()) {
            Break original = (Break) iterator.next();
            result.addBreak(original.main, original.subFrom, original.subTo);
        }
        return result;
    }
}
