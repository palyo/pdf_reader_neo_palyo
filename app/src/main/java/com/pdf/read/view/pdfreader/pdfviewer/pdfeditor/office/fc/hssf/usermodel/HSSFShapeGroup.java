package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;

public class HSSFShapeGroup extends HSSFShape implements HSSFShapeContainer {
    private final List<HSSFShape> shapes = new ArrayList<HSSFShape>();
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 1023;
    private int y2 = 255;

    public HSSFShapeGroup(EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor) {
        super(escherContainer, parent, anchor);
    }

    public HSSFShapeGroup createGroup(HSSFChildAnchor anchor) {
        HSSFShapeGroup group = new HSSFShapeGroup(null, this, anchor);
        group.setAnchor(anchor);
        shapes.add(group);
        return group;
    }

    public HSSFSimpleShape createShape(HSSFChildAnchor anchor) {
        HSSFSimpleShape shape = new HSSFSimpleShape(null, this, anchor);
        shape.setAnchor(anchor);
        shapes.add(shape);
        return shape;
    }

    public HSSFTextbox createTextbox(HSSFChildAnchor anchor) {
        HSSFTextbox shape = new HSSFTextbox(null, this, anchor);
        shape.setAnchor(anchor);
        shapes.add(shape);
        return shape;
    }

    public HSSFPolygon createPolygon(HSSFChildAnchor anchor) {
        HSSFPolygon shape = new HSSFPolygon(null, this, anchor);
        shape.setAnchor(anchor);
        shapes.add(shape);
        return shape;
    }

    public HSSFPicture createPicture(HSSFChildAnchor anchor, int pictureIndex) {
        HSSFPicture shape = new HSSFPicture(null, this, anchor);
        shape.setAnchor(anchor);
        shape.setPictureIndex(pictureIndex);
        shapes.add(shape);
        return shape;
    }

    public void addChildShape(HSSFShape shape) {
        shapes.add(shape);
    }

    public List<HSSFShape> getChildren() {
        return shapes;
    }

    public void setCoordinates(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int countOfAllChildren() {
        int count = shapes.size();
        for (Iterator<HSSFShape> iterator = shapes.iterator(); iterator.hasNext(); ) {
            HSSFShape shape = iterator.next();
            count += shape.countOfAllChildren();
        }
        return count;
    }
}
