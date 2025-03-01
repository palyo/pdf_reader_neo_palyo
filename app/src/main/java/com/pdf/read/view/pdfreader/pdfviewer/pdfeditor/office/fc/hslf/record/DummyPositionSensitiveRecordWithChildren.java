package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class DummyPositionSensitiveRecordWithChildren extends
        PositionDependentRecordContainer {
    private final long _type;
    private byte[] _header;

    private DummyPositionSensitiveRecordWithChildren(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        _type = LittleEndian.getUShort(_header, 2);

        _children = Record.findChildRecords(source, start + 8, len - 8);
    }

    public long getRecordType() {
        return _type;
    }

    public void dispose() {
        super.dispose();
        _header = null;
    }
}
