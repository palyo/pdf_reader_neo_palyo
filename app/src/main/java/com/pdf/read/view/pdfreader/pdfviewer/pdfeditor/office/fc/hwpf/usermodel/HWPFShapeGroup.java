package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherChildAnchorRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherClientAnchorRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSpgrRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public class HWPFShapeGroup extends HWPFShape {
    public HWPFShapeGroup(EscherContainerRecord escherRecord, HWPFShape parent) {
        super(escherRecord, parent);
    }

    public Rectangle getCoordinates(float zoomX, float zoomY) {
        Rectangle anchor = null;
        EscherContainerRecord spContainer = (EscherContainerRecord) getSpContainer().getChild(0);
        if (spContainer != null) {
            EscherSpgrRecord spgr = (EscherSpgrRecord) ShapeKit.getEscherChild(spContainer, EscherSpgrRecord.RECORD_ID);
            if (spgr != null) {
                anchor = new Rectangle();
                anchor.x = (int) (spgr.getRectX1() * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.y = (int) (spgr.getRectY1() * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.width = (int) ((spgr.getRectX2() - spgr.getRectX1()) * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.height = (int) ((spgr.getRectY2() - spgr.getRectY1()) * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
            }
        }
        return anchor;
    }

    public Rectangle getAnchor(float zoomX, float zoomY) {
        Rectangle anchor = null;
        EscherContainerRecord spContainer = (EscherContainerRecord) getSpContainer().getChild(0);
        if (spContainer != null) {
            EscherClientAnchorRecord clrec = (EscherClientAnchorRecord) ShapeKit.getEscherChild(
                    spContainer, EscherClientAnchorRecord.RECORD_ID);
            if (clrec == null) {
                EscherChildAnchorRecord rec = (EscherChildAnchorRecord) ShapeKit.getEscherChild(spContainer,
                        EscherChildAnchorRecord.RECORD_ID);
                if (rec != null) {
                    anchor = new Rectangle();
                    anchor.x = (int) (rec.getDx1() * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.y = (int) (rec.getDy1() * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.width = (int) ((rec.getDx2() - rec.getDx1()) * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.height = (int) ((rec.getDy2() - rec.getDy1()) * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                }
            } else {
                anchor = new Rectangle();
                anchor.x = (int) (clrec.getCol1() * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.y = (int) (clrec.getFlag() * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.width = (int) ((clrec.getDx1() - clrec.getCol1()) * zoomX * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.height = (int) ((clrec.getRow1() - clrec.getFlag()) * zoomY * KeyKt.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
            }
        }

        return anchor;
    }

    public float[] getShapeAnchorFit(Rectangle rect, float zoomX, float zoomY) {
        float[] zoom = {1.0f, 1.0f};
        EscherContainerRecord spContainer = (EscherContainerRecord) getSpContainer().getChild(0);
        if (spContainer != null) {
            EscherSpgrRecord spgr = (EscherSpgrRecord) ShapeKit.getEscherChild(spContainer, EscherSpgrRecord.RECORD_ID);
            if (spgr != null) {
                float w = spgr.getRectX2() - spgr.getRectX1();
                float h = spgr.getRectY2() - spgr.getRectY1();
                if (w != 0 && h != 0) {
                    zoom[0] = rect.width * KeyKt.EMU_PER_INCH / KeyKt.PIXEL_DPI / zoomX / w;
                    zoom[1] = rect.height * KeyKt.EMU_PER_INCH / KeyKt.PIXEL_DPI / zoomY / h;
                }
            }
        }
        return zoom;
    }

    public boolean getFlipHorizontal() {
        return ShapeKit.getGroupFlipHorizontal(getSpContainer());
    }

    public boolean getFlipVertical() {
        return ShapeKit.getGroupFlipVertical(getSpContainer());
    }

    public int getGroupRotation() {
        return ShapeKit.getGroupRotation(getSpContainer());
    }

    public HWPFShape[] getShapes() {
        Iterator<EscherRecord> iter = getSpContainer().getChildIterator();
        if (iter.hasNext()) {
            iter.next();
        }
        List<HWPFShape> shapeList = new ArrayList<HWPFShape>();
        while (iter.hasNext()) {
            EscherRecord r = iter.next();
            if (r instanceof EscherContainerRecord) {
                EscherContainerRecord container = (EscherContainerRecord) r;
                HWPFShape shape = HWPFShapeFactory.createShape(container, this);
                shapeList.add(shape);
            }
        }

        HWPFShape[] shapes = shapeList.toArray(new HWPFShape[shapeList.size()]);
        return shapes;
    }

    public String getShapeName() {
        return ShapeKit.getShapeName(escherContainer);
    }
}
