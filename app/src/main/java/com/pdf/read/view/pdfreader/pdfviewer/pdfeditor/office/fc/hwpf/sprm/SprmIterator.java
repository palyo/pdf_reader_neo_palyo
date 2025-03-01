package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class SprmIterator {
    private final byte[] _grpprl;
    int _offset;

    public SprmIterator(byte[] grpprl, int offset) {
        _grpprl = grpprl;
        _offset = offset;
    }

    public boolean hasNext() {

        return _offset < (_grpprl.length - 1);
    }

    public SprmOperation next() {
        SprmOperation op = new SprmOperation(_grpprl, _offset);
        _offset += op.size();
        return op;
    }

}
