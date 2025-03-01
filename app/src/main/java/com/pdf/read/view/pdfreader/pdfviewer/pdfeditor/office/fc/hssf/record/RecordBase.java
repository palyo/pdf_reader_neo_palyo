package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

public abstract class RecordBase {

    public abstract int serialize(int offset, byte[] data);

    public abstract int getRecordSize();
}
