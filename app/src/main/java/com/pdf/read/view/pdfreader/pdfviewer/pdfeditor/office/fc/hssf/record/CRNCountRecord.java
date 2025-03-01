package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CRNCountRecord extends StandardRecord {
    public final static short sid = 0x59;

    private static final short DATA_SIZE = 4;
    private final int field_2_sheet_table_index;
    private int field_1_number_crn_records;

    public CRNCountRecord() {
        throw new RuntimeException("incomplete code");
    }

    public CRNCountRecord(RecordInputStream in) {
        field_1_number_crn_records = in.readShort();
        if (field_1_number_crn_records < 0) {
            field_1_number_crn_records = (short) -field_1_number_crn_records;
        }
        field_2_sheet_table_index = in.readShort();
    }

    public int getNumberOfCRNs() {
        return field_1_number_crn_records;
    }

    public String toString() {
        String sb = getClass().getName() + " [XCT" +
                " nCRNs=" + field_1_number_crn_records +
                " sheetIx=" + field_2_sheet_table_index +
                "]";
        return sb;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort((short) field_1_number_crn_records);
        out.writeShort((short) field_2_sheet_table_index);
    }

    protected int getDataSize() {
        return DATA_SIZE;
    }

    public short getSid() {
        return sid;
    }
}
