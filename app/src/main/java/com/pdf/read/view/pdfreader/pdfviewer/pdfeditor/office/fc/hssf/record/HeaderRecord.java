package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

public final class HeaderRecord extends HeaderFooterBase {
    public final static short sid = 0x0014;

    public HeaderRecord(String text) {
        super(text);
    }

    public HeaderRecord(RecordInputStream in) {
        super(in);
    }

    public String toString() {

        String buffer = "[HEADER]\n" +
                "    .header = " + getText() + "\n" +
                "[/HEADER]\n";
        return buffer;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return new HeaderRecord(getText());
    }
}
