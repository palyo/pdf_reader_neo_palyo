package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public enum FSPADocumentPart {
    HEADER(FIBFieldHandler.PLCSPAHDR),

    MAIN(FIBFieldHandler.PLCSPAMOM);

    private final int fibFieldsField;

    FSPADocumentPart(final int fibHandlerField) {
        this.fibFieldsField = fibHandlerField;
    }

    public int getFibFieldsField() {
        return fibFieldsField;
    }
}
