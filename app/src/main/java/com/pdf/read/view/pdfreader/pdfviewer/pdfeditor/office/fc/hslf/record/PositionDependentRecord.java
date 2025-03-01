package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.util.Hashtable;

public interface PositionDependentRecord {
    int getLastOnDiskOffset();

    void setLastOnDiskOffset(int offset);

    void updateOtherRecordReferences(Hashtable<Integer, Integer> oldToNewReferencesLookup);

    void dispose();
}
