package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public interface OfficeDrawing {

    PictureEffectInfo getPictureEffectInfor();

    byte getHorizontalPositioning();

    byte getHorizontalRelative();

    byte[] getPictureData(IControl control);

    byte[] getPictureData(IControl control, int index);

    HWPFShape getAutoShape();

    int getRectangleBottom();

    int getRectangleLeft();

    int getRectangleRight();

    int getRectangleTop();

    int getShapeId();

    int getWrap();

    boolean isBelowText();

    boolean isAnchorLock();

    String getTempFilePath(IControl control);

    byte getVerticalPositioning();

    byte getVerticalRelativeElement();

    enum HorizontalPositioning {

        ABSOLUTE,

        CENTER,

        INSIDE,

        LEFT,

        OUTSIDE,

        RIGHT
    }

    enum HorizontalRelativeElement {
        CHAR, MARGIN, PAGE, TEXT
    }

    enum VerticalPositioning {

        ABSOLUTE,

        BOTTOM,

        CENTER,

        INSIDE,

        OUTSIDE,

        TOP
    }

    enum VerticalRelativeElement {
        LINE, MARGIN, PAGE, TEXT
    }

}
