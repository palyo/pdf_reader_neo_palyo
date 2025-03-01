package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;

public class HSSFPolygon
        extends HSSFShape {
    int[] xPoints;
    int[] yPoints;
    int drawAreaWidth = 100;
    int drawAreaHeight = 100;

    HSSFPolygon(EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor) {
        super(escherContainer, parent, anchor);
    }

    public int[] getXPoints() {
        return xPoints;
    }

    public int[] getYPoints() {
        return yPoints;
    }

    public void setPoints(int[] xPoints, int[] yPoints) {
        this.xPoints = cloneArray(xPoints);
        this.yPoints = cloneArray(yPoints);
    }

    private int[] cloneArray(int[] a) {
        int[] result = new int[a.length];
        System.arraycopy(a, 0, result, 0, a.length);

        return result;
    }

    public void setPolygonDrawArea(int width, int height) {
        this.drawAreaWidth = width;
        this.drawAreaHeight = height;
    }

    public int getDrawAreaWidth() {
        return drawAreaWidth;
    }

    public int getDrawAreaHeight() {
        return drawAreaHeight;
    }

}
