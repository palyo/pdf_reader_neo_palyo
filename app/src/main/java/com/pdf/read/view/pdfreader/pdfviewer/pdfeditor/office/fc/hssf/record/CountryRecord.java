package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class CountryRecord
        extends StandardRecord {
    public final static short sid = 0x8c;

    private short field_1_default_country;
    private short field_2_current_country;

    public CountryRecord() {
    }

    public CountryRecord(RecordInputStream in) {
        field_1_default_country = in.readShort();
        field_2_current_country = in.readShort();
    }

    public short getDefaultCountry() {
        return field_1_default_country;
    }

    public void setDefaultCountry(short country) {
        field_1_default_country = country;
    }

    public short getCurrentCountry() {
        return field_2_current_country;
    }

    public void setCurrentCountry(short country) {
        field_2_current_country = country;
    }

    public String toString() {

        String buffer = "[COUNTRY]\n" +
                "    .defaultcountry  = " +
                Integer.toHexString(getDefaultCountry()) + "\n" +
                "    .currentcountry  = " +
                Integer.toHexString(getCurrentCountry()) + "\n" +
                "[/COUNTRY]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getDefaultCountry());
        out.writeShort(getCurrentCountry());
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return sid;
    }
}
