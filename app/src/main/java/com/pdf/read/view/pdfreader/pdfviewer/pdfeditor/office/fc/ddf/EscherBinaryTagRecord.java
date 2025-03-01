package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

public class EscherBinaryTagRecord extends EscherTextboxRecord {
    public static final short RECORD_ID = (short) 0x138B;

    public EscherBinaryTagRecord() {

    }

    public String getRecordName() {
        return "BinaryTagData";
    }
}
