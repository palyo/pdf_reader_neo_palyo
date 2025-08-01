package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public class HPSFException extends Exception {

    private Throwable reason;

    public HPSFException() {
        super();
    }

    public HPSFException(final String msg) {
        super(msg);
    }

    public HPSFException(final Throwable reason) {
        super();
        this.reason = reason;
    }

    public HPSFException(final String msg, final Throwable reason) {
        super(msg);
        this.reason = reason;
    }

    public Throwable getReason() {
        return reason;
    }
}
