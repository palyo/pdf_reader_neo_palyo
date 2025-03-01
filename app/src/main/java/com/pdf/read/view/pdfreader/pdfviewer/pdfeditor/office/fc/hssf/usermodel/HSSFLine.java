package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.AWorkbook;

public class HSSFLine extends HSSFSimpleShape {

    private Float[] adjusts;

    public HSSFLine(AWorkbook workbook, EscherContainerRecord escherContainer,
                    HSSFShape parent, HSSFAnchor anchor, int shapeType) {
        super(escherContainer, parent, anchor);
        setShapeType(shapeType);
        processLineWidth();
        processLine(escherContainer, workbook);
        processArrow(escherContainer);
        setAdjustmentValue(escherContainer);
        processRotationAndFlip(escherContainer);
    }

    public Float[] getAdjustmentValue() {
        return adjusts;
    }

    public void setAdjustmentValue(EscherContainerRecord escherContainer) {
        adjusts = ShapeKit.getAdjustmentValue(escherContainer);
    }
}
