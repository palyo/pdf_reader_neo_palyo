package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PrintHeadersRecord
        extends StandardRecord {
    public final static short sid = 0x2a;
    private short field_1_print_headers;

    public PrintHeadersRecord() {
    }

    public PrintHeadersRecord(RecordInputStream in) {
        field_1_print_headers = in.readShort();
    }

    public boolean getPrintHeaders() {
        return (field_1_print_headers == 1);
    }

    public void setPrintHeaders(boolean p) {
        if (p) {
            field_1_print_headers = 1;
        } else {
            field_1_print_headers = 0;
        }
    }

    public String toString() {

        String buffer = "[PRINTHEADERS]\n" +
                "    .printheaders   = " + getPrintHeaders() +
                "\n" +
                "[/PRINTHEADERS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_print_headers);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PrintHeadersRecord rec = new PrintHeadersRecord();
        rec.field_1_print_headers = field_1_print_headers;
        return rec;
    }
}
