package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public enum NoteType {

    ENDNOTE(FIBFieldHandler.PLCFENDREF, FIBFieldHandler.PLCFENDTXT),

    FOOTNOTE(FIBFieldHandler.PLCFFNDREF, FIBFieldHandler.PLCFFNDTXT);

    private final int fibDescriptorsFieldIndex;
    private final int fibTextPositionsFieldIndex;

    NoteType(int fibDescriptorsFieldIndex,
             int fibTextPositionsFieldIndex) {
        this.fibDescriptorsFieldIndex = fibDescriptorsFieldIndex;
        this.fibTextPositionsFieldIndex = fibTextPositionsFieldIndex;
    }

    public int getFibDescriptorsFieldIndex() {
        return fibDescriptorsFieldIndex;
    }

    public int getFibTextPositionsFieldIndex() {
        return fibTextPositionsFieldIndex;
    }
}
