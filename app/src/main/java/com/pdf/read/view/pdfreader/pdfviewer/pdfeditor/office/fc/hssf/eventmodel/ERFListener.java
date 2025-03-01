package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.eventmodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.Record;

public interface ERFListener {
    boolean processRecord(Record rec);
}
