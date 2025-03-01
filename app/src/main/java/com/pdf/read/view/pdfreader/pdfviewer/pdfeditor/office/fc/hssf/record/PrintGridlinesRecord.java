package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PrintGridlinesRecord
        extends StandardRecord {
    public final static short sid = 0x2b;
    private short field_1_print_gridlines;

    public PrintGridlinesRecord() {
    }

    public PrintGridlinesRecord(RecordInputStream in) {
        field_1_print_gridlines = in.readShort();
    }

    public boolean getPrintGridlines() {
        return (field_1_print_gridlines == 1);
    }

    public void setPrintGridlines(boolean pg) {
        if (pg) {
            field_1_print_gridlines = 1;
        } else {
            field_1_print_gridlines = 0;
        }
    }

    public String toString() {

        String buffer = "[PRINTGRIDLINES]\n" +
                "    .printgridlines = " + getPrintGridlines() +
                "\n" +
                "[/PRINTGRIDLINES]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_print_gridlines);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PrintGridlinesRecord rec = new PrintGridlinesRecord();
        rec.field_1_print_gridlines = field_1_print_gridlines;
        return rec;
    }
}
