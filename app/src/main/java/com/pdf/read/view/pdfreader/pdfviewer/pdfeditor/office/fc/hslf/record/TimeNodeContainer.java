package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

public class TimeNodeContainer extends PositionDependentRecordContainer {
    public static long RECORD_ID = 0xF144;
    private final byte[] _header;

    protected TimeNodeContainer(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _children = Record.findChildRecords(source, start + 8, len - 8);
    }

    public long getRecordType() {
        return RECORD_ID;
    }
}
