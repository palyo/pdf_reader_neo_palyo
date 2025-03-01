package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public class WPAutoShape extends WPAbstractShape {

    private WPGroupShape groupShape;

    public WPAutoShape() {

    }

    public short getType() {
        return SHAPE_AUTOSHAPE;
    }

    public boolean isWatermarkShape() {
        return false;
    }

    public Rectangle getBounds() {
        if (groupShape != null) {
            return groupShape.getBounds();
        }
        return super.getBounds();
    }

    public void addGroupShape(WPGroupShape groupShape) {
        this.groupShape = groupShape;
    }

    public WPGroupShape getGroupShape() {
        return groupShape;
    }

    public void dispose() {
        super.dispose();
        if (groupShape != null) {
            groupShape.dispose();
            groupShape = null;
        }
    }
}
