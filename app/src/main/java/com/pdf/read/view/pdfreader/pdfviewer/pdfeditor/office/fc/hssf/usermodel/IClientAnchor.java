package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

public interface IClientAnchor {

    int MOVE_AND_RESIZE = 0;

    int MOVE_DONT_RESIZE = 2;

    int DONT_MOVE_AND_RESIZE = 3;

    short getCol1();

    void setCol1(int col1);

    short getCol2();

    void setCol2(int col2);

    int getRow1();

    void setRow1(int row1);

    int getRow2();

    void setRow2(int row2);

    int getDx1();

    void setDx1(int dx1);

    int getDy1();

    void setDy1(int dy1);

    int getDy2();

    void setDy2(int dy2);

    int getDx2();

    void setDx2(int dx2);

    int getAnchorType();

    void setAnchorType(int anchorType);

}
