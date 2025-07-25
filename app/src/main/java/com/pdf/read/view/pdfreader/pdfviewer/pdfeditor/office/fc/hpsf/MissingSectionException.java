package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public class MissingSectionException extends HPSFRuntimeException {

    public MissingSectionException() {
        super();
    }

    public MissingSectionException(final String msg) {
        super(msg);
    }

    public MissingSectionException(final Throwable reason) {
        super(reason);
    }

    public MissingSectionException(final String msg, final Throwable reason) {
        super(msg, reason);
    }
}
