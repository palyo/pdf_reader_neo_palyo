package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

public class SlideProgTagsContainer extends PositionDependentRecordContainer {
    public static long RECORD_ID = 0x1388;
    private final byte[] _header;

    protected SlideProgTagsContainer(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _children = Record.findChildRecords(source, start + 8, len - 8);
    }

    public long getRecordType() {
        return RECORD_ID;
    }
}
