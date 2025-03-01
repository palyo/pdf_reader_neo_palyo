package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

public final class ExAviMovie extends ExMCIMovie {

    private ExAviMovie(byte[] source, int start, int len) {
        super(source, start, len);
    }

    public ExAviMovie() {
        super();
    }

    public long getRecordType() {
        return RecordTypes.ExAviMovie.typeID;
    }
}
