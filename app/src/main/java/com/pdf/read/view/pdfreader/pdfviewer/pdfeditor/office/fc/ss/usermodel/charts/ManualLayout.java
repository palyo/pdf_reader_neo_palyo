package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts;

public interface ManualLayout {

    LayoutTarget getTarget();

    void setTarget(LayoutTarget target);

    LayoutMode getXMode();

    void setXMode(LayoutMode mode);

    LayoutMode getYMode();

    void setYMode(LayoutMode mode);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    LayoutMode getWidthMode();

    void setWidthMode(LayoutMode mode);

    LayoutMode getHeightMode();

    void setHeightMode(LayoutMode mode);

    double getWidthRatio();

    void setWidthRatio(double ratio);

    double getHeightRatio();

    void setHeightRatio(double ratio);
}
