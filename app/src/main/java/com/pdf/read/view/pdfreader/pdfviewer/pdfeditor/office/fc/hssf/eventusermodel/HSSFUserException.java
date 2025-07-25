package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.eventusermodel;

public class HSSFUserException extends Exception {
    private Throwable reason;

    public HSSFUserException() {
        super();
    }

    public HSSFUserException(final String msg) {
        super(msg);
    }

    public HSSFUserException(final Throwable reason) {
        super();
        this.reason = reason;
    }

    public HSSFUserException(final String msg, final Throwable reason) {
        super(msg);
        this.reason = reason;
    }

    public Throwable getReason() {
        return reason;
    }
}
