package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;

public interface IShape {
    int getGroupShapeID();

    void setGroupShapeID(int id);

    int getShapeID();

    void setShapeID(int id);

    short getType();

    IShape getParent();

    void setParent(IShape shape);

    Rectangle getBounds();

    void setBounds(Rectangle rect);

    Object getData();

    void setData(Object data);

    boolean getFlipHorizontal();

    void setFlipHorizontal(boolean flipH);

    boolean getFlipVertical();

    void setFlipVertical(boolean flipV);

    float getRotation();

    void setRotation(float angle);

    boolean isHidden();

    void setHidden(boolean hidden);

    IAnimation getAnimation();

    void setAnimation(IAnimation animation);

    int getPlaceHolderID();

    void setPlaceHolderID(int placeHolderID);

    void dispose();

}
