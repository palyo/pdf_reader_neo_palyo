package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import java.util.ArrayList;
import java.util.List;

public class SmartArt extends AbstractShape {

    private final List<IShape> shapes;
    private int offX, offY;

    public SmartArt() {
        shapes = new ArrayList<IShape>();
    }

    public short getType() {
        return SHAPE_SMARTART;
    }

    public void appendShapes(IShape shape) {
        this.shapes.add(shape);
    }

    public IShape[] getShapes() {
        return shapes.toArray(new IShape[shapes.size()]);
    }
}
