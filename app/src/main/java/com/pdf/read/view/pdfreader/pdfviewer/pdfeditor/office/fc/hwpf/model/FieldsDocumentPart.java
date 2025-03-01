package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public enum FieldsDocumentPart {

    ANNOTATIONS(FIBFieldHandler.PLCFFLDATN),

    ENDNOTES(FIBFieldHandler.PLCFFLDEDN),

    FOOTNOTES(FIBFieldHandler.PLCFFLDFTN),

    HEADER(FIBFieldHandler.PLCFFLDHDR),

    HEADER_TEXTBOX(FIBFieldHandler.PLCFFLDHDRTXBX),

    MAIN(FIBFieldHandler.PLCFFLDMOM),

    TEXTBOX(FIBFieldHandler.PLCFFLDTXBX);

    private final int fibFieldsField;

    FieldsDocumentPart(final int fibHandlerField) {
        this.fibFieldsField = fibHandlerField;
    }

    public int getFibFieldsField() {
        return fibFieldsField;
    }

}
