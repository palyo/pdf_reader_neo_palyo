package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

public interface ParentAwareRecord {
    RecordContainer getParentRecord();

    void setParentRecord(RecordContainer parentRecord);
}
