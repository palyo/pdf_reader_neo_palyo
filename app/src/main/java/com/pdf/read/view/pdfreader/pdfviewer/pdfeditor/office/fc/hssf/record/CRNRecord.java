package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.fc.ConstantValueParser;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CRNRecord extends StandardRecord {
    public final static short sid = 0x005A;

    private final int field_1_last_column_index;
    private final int field_2_first_column_index;
    private final int field_3_row_index;
    private final Object[] field_4_constant_values;

    public CRNRecord() {
        throw new RuntimeException("incomplete code");
    }

    public CRNRecord(RecordInputStream in) {
        field_1_last_column_index = in.readUByte();
        field_2_first_column_index = in.readUByte();
        field_3_row_index = in.readShort();
        int nValues = field_1_last_column_index - field_2_first_column_index + 1;
        field_4_constant_values = ConstantValueParser.parse(in, nValues);
    }

    public int getNumberOfCRNs() {
        return field_1_last_column_index;
    }

    public String toString() {
        String sb = getClass().getName() + " [CRN" +
                " rowIx=" + field_3_row_index +
                " firstColIx=" + field_2_first_column_index +
                " lastColIx=" + field_1_last_column_index +
                "]";
        return sb;
    }

    protected int getDataSize() {
        return 4 + ConstantValueParser.getEncodedSize(field_4_constant_values);
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(field_1_last_column_index);
        out.writeByte(field_2_first_column_index);
        out.writeShort(field_3_row_index);
        ConstantValueParser.encode(out, field_4_constant_values);
    }

    public short getSid() {
        return sid;
    }
}
