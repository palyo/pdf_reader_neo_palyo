package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public class ReadingNotSupportedException
        extends UnsupportedVariantTypeException {

    public ReadingNotSupportedException(final long variantType,
                                        final Object value) {
        super(variantType, value);
    }
}
