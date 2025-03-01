package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control;

import android.graphics.Canvas;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.IView;

public interface IHighlight {

    void draw(Canvas canvas, IView line, int originX, int originY, long start, long end, float zoom);

    String getSelectText();

    boolean isSelectText();

    void removeHighlight();

    void addHighlight(long start, long end);

    long getSelectStart();

    void setSelectStart(long selectStart);

    long getSelectEnd();

    void setSelectEnd(long selectEnd);

    void setPaintHighlight(boolean isPaintHighlight);

    void dispose();

}
