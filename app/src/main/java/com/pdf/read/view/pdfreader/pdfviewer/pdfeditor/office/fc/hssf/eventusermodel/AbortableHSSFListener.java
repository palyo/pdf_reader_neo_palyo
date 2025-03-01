package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.eventusermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.Record;

public abstract class AbortableHSSFListener implements HSSFListener {
    public void processRecord(Record record) {
    }

    public abstract short abortableProcessRecord(Record record) throws HSSFUserException;
}
