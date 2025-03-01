package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class UnknownRecordPlaceholder extends RecordAtom {
    private final long _type;
    private byte[] _contents;

    private UnknownRecordPlaceholder(byte[] source, int start, int len) {

        if (len < 0) {
            len = 0;
        }

        _contents = new byte[len];
        System.arraycopy(source, start, _contents, 0, len);
        _type = LittleEndian.getUShort(_contents, 2);
    }

    public long getRecordType() {
        return _type;
    }

    public void dispose() {
        _contents = null;
    }
}
