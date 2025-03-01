package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFTag;

public class AngleArc extends EMFTag {

    private Point center;

    private int radius;

    private float startAngle, sweepAngle;

    public AngleArc() {
        super(41, 1);
    }

    public AngleArc(Point center, int radius, float startAngle, float sweepAngle) {
        this();
        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException {

        return new AngleArc(emf.readPOINTL(), emf.readDWORD(), emf.readFLOAT(), emf.readFLOAT());
    }

    public String toString() {
        return super.toString() + "\n  center: " + center + "\n  radius: " + radius
                + "\n  startAngle: " + startAngle + "\n  sweepAngle: " + sweepAngle;
    }
}
