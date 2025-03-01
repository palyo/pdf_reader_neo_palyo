package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import android.graphics.Path;
import android.graphics.PointF;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.AWorkbook;

public class HSSFFreeform extends HSSFAutoShape {
    public HSSFFreeform(AWorkbook workbook, EscherContainerRecord escherContainer,
                        HSSFShape parent, HSSFAnchor anchor, int shapeType) {
        super(workbook, escherContainer, parent, anchor, shapeType);
        processLineWidth();
        processArrow(escherContainer);
    }

    public Path[] getFreeformPath(Rectangle rect, PointF startArrowTailCenter, byte startArrowType, PointF endArrowTailCenter, byte endArrowType) {
        return ShapeKit.getFreeformPath(escherContainer, rect, startArrowTailCenter, startArrowType, endArrowTailCenter, endArrowType);
    }

    public ArrowPathAndTail getStartArrowPath(Rectangle rect) {
        return ShapeKit.getStartArrowPathAndTail(escherContainer, rect);
    }

    public ArrowPathAndTail getEndArrowPath(Rectangle rect) {
        return ShapeKit.getEndArrowPathAndTail(escherContainer, rect);
    }
}
