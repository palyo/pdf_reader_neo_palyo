package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

public class EscherShapePathProperty extends EscherSimpleProperty {

    public static final int LINE_OF_STRAIGHT_SEGMENTS = 0;
    public static final int CLOSED_POLYGON = 1;
    public static final int CURVES = 2;
    public static final int CLOSED_CURVES = 3;
    public static final int COMPLEX = 4;

    public EscherShapePathProperty(short propertyNumber, int shapePath) {
        super(propertyNumber, false, false, shapePath);
    }
}
