package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.LinkedList;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.textproperties.AutoNumberTextProp;

public abstract class RecordAtom extends Record {
    public boolean isAnAtom() {
        return true;
    }

    public Record[] getChildRecords() {
        return null;
    }

    public LinkedList<AutoNumberTextProp> getExtendedParagraphPropList() {
        return null;
    }

}
