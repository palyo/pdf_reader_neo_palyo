package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FeatSmartTag implements SharedFeature {
    private final byte[] data;

    public FeatSmartTag(RecordInputStream in) {
        data = in.readRemainder();
    }

    public String toString() {
        String buffer = " [FEATURE SMART TAGS]\n" +
                " [/FEATURE SMART TAGS]\n";
        return buffer;
    }

    public int getDataSize() {
        return data.length;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(data);
    }
}
