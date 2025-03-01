package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

public class RecordFormatException
        extends RuntimeException {
    public RecordFormatException(String exception) {
        super(exception);
    }

    public RecordFormatException(String exception, Throwable thr) {
        super(exception, thr);
    }

    public RecordFormatException(Throwable thr) {
        super(thr);
    }
}
