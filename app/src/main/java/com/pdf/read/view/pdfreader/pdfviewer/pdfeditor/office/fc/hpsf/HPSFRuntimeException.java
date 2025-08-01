package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.PrintStream;
import java.io.PrintWriter;

public class HPSFRuntimeException extends RuntimeException {

    private Throwable reason;

    public HPSFRuntimeException() {
        super();
    }

    public HPSFRuntimeException(final String msg) {
        super(msg);
    }

    public HPSFRuntimeException(final Throwable reason) {
        super();
        this.reason = reason;
    }

    public HPSFRuntimeException(final String msg, final Throwable reason) {
        super(msg);
        this.reason = reason;
    }

    public Throwable getReason() {
        return reason;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(final PrintStream p) {
        final Throwable reason = getReason();
        super.printStackTrace(p);
        if (reason != null) {
            p.println("Caused by:");
            reason.printStackTrace(p);
        }
    }

    public void printStackTrace(final PrintWriter p) {
        final Throwable reason = getReason();
        super.printStackTrace(p);
        if (reason != null) {
            p.println("Caused by:");
            reason.printStackTrace(p);
        }
    }
}
