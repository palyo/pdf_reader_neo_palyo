package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.util.Arrays;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class UPX {
    private final byte[] _upx;

    public UPX(byte[] upx) {
        _upx = upx;
    }

    public byte[] getUPX() {
        return _upx;
    }

    public int size() {
        return _upx.length;
    }

    public boolean equals(Object o) {
        UPX upx = (UPX) o;
        return Arrays.equals(_upx, upx._upx);
    }
}
