package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public class WritingNotSupportedException extends UnsupportedVariantTypeException {

    public WritingNotSupportedException(final long variantType, final Object value) {
        super(variantType, value);
    }
}
