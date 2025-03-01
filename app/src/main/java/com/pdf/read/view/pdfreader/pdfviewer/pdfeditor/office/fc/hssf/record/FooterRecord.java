package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

public final class FooterRecord extends HeaderFooterBase {
    public final static short sid = 0x0015;

    public FooterRecord(String text) {
        super(text);
    }

    public FooterRecord(RecordInputStream in) {
        super(in);
    }

    public String toString() {

        String buffer = "[FOOTER]\n" +
                "    .footer = " + getText() + "\n" +
                "[/FOOTER]\n";
        return buffer;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return new FooterRecord(getText());
    }
}
