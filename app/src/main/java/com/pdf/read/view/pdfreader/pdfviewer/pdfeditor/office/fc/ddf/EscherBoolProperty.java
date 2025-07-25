package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

public class EscherBoolProperty
        extends EscherSimpleProperty {
    public EscherBoolProperty(short propertyNumber, int value) {
        super(propertyNumber, value);
    }

    public boolean isTrue() {
        return propertyValue != 0;
    }

    public boolean isFalse() {
        return propertyValue == 0;
    }
}
