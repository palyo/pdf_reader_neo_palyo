package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.Record;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianByteArrayOutputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public abstract class ContinuableRecord extends Record {

    protected ContinuableRecord() {

    }

    protected abstract void serialize(ContinuableRecordOutput out);

    public final int getRecordSize() {
        ContinuableRecordOutput out = ContinuableRecordOutput.createForCountingOnly();
        serialize(out);
        out.terminate();
        return out.getTotalSize();
    }

    public final int serialize(int offset, byte[] data) {

        LittleEndianOutput leo = new LittleEndianByteArrayOutputStream(data, offset);
        ContinuableRecordOutput out = new ContinuableRecordOutput(leo, getSid());
        serialize(out);
        out.terminate();
        return out.getTotalSize();
    }
}
