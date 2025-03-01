package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro.TouchEventListener;

public interface IPageListViewListener {

    byte ON_TOUCH = 0;

    byte ON_DOWN = 1;

    byte ON_SHOW_PRESS = 2;

    byte ON_SINGLE_TAP_UP = 3;

    byte ON_SCROLL = 4;

    byte ON_LONG_PRESS = 5;

    byte ON_FLING = 6;

    byte ON_SINGLE_TAP_CONFIRMED = 7;

    byte ON_DOUBLE_TAP = 8;

    byte ON_DOUBLE_TAP_EVENT = 9;

    byte ON_CLICK = 10;

    byte Moving_Horizontal = 0;
    byte Moving_Vertical = 1;

    Object getModel();

    APageListItem getPageListItem(int position, View convertView, ViewGroup parent);

    void exportImage(final APageListItem view, final Bitmap srcBitmap);

    int getPageCount();

    Rect getPageSize(int pageIndex);

    boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType);

    void updateStutus(Object obj);

    void resetSearchResult(APageListItem pageItem);

    boolean isTouchZoom();

    boolean isShowZoomingMsg();

    void changeZoom();

    boolean isChangePage();

    void setDrawPictrue(boolean isDrawPictrue);

    boolean isInit();

    boolean isIgnoreOriginalSize();

    byte getPageListViewMovingPosition();
}
