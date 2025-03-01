package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

public class TimeConditionContainer extends PositionDependentRecordContainer {
    private static final long _type = 0xF125;
    private final byte[] _header;

    protected TimeConditionContainer(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _children = Record.findChildRecords(source, start + 8, len - 8);
    }

    public long getRecordType() {
        return _type;
    }
}
