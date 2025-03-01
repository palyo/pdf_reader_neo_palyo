package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

public interface EscherRecordFactory {
    EscherRecord createRecord(byte[] data, int offset);
}
