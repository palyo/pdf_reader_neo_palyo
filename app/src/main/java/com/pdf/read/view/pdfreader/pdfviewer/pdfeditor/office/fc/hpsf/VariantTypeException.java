package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public abstract class VariantTypeException extends HPSFException {

    private final Object value;

    private final long variantType;

    public VariantTypeException(final long variantType, final Object value,
                                final String msg) {
        super(msg);
        this.variantType = variantType;
        this.value = value;
    }

    public long getVariantType() {
        return variantType;
    }

    public Object getValue() {
        return value;
    }
}
