package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

public class EscherPropertyMetaData {
    public final static byte TYPE_UNKNOWN = (byte) 0;
    public final static byte TYPE_BOOLEAN = (byte) 1;
    public final static byte TYPE_RGB = (byte) 2;
    public final static byte TYPE_SHAPEPATH = (byte) 3;
    public final static byte TYPE_SIMPLE = (byte) 4;
    public final static byte TYPE_ARRAY = (byte) 5;

    private final String description;
    private byte type;

    public EscherPropertyMetaData(String description) {
        this.description = description;
    }

    public EscherPropertyMetaData(String description, byte type) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public byte getType() {
        return type;
    }

}
