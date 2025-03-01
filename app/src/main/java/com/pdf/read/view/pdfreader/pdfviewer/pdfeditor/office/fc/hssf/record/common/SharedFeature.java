package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public interface SharedFeature {
    String toString();

    void serialize(LittleEndianOutput out);

    int getDataSize();
}
