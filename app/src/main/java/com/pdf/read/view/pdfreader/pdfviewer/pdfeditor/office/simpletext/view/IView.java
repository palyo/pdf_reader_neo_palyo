package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPViewConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public interface IView {

    IElement getElement();

    void setElement(IElement elem);

    short getType();

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getWidth();

    void setWidth(int w);

    int getHeight();

    void setHeight(int h);

    void setSize(int w, int h);

    void setLocation(int x, int y);

    void setBound(int x, int y, int w, int h);

    int getTopIndent();

    void setTopIndent(int top);

    int getBottomIndent();

    void setBottomIndent(int bottom);

    int getLeftIndent();

    void setLeftIndent(int left);

    int getRightIndent();

    void setRightIndent(int right);

    void setIndent(int left, int top, int right, int bottom);

    IWord getContainer();

    IControl getControl();

    IDocument getDocument();

    IView getChildView();

    void setChildView(IView view);

    IView getParentView();

    void setParentView(IView view);

    IView getPreView();

    void setPreView(IView view);

    IView getNextView();

    void setNextView(IView view);

    IView getLastView();

    int getChildCount();

    void appendChlidView(IView view);

    void insertView(IView view, IView newView);

    void deleteView(IView view, boolean isDeleteChild);

    void setStartOffset(long start);

    long getStartOffset(IDocument doc);

    void setEndOffset(long end);

    long getEndOffset(IDocument doc);

    long getElemStart(IDocument doc);

    long getElemEnd(IDocument doc);

    Rect getViewRect(int originX, int originY, float zoom);

    boolean intersection(Rect rect, int originX, int originY, float zoom);

    boolean contains(long offset, boolean isBack);

    boolean contains(int x, int y, boolean isBack);

    Rectangle modelToView(long offset, Rectangle rect, boolean isBack);

    long viewToModel(int x, int y, boolean isBack);

    long getNextForCoordinate(long offset, int dir, int x, int y);

    long getNextForOffset(long offset, int dir, int x, int y);

    void draw(Canvas canvas, int originX, int originY, float zoom);

    int doLayout(int x, int y, int w, int h, long maxEnd, int flag);

    int getLayoutSpan(byte axis);

    IView getView(long offset, int type, boolean isBack);

    IView getView(int x, int y, int type, boolean isBack);

    void dispose();

    void free();

}
