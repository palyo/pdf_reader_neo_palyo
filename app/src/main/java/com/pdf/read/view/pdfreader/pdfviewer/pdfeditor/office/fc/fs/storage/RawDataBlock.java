package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.fs.storage;

public class RawDataBlock {
    private final byte[] _data;

    public RawDataBlock(byte[] data) {
        this._data = data;
    }

    public byte[] getData() {
        return _data;
    }
} 

