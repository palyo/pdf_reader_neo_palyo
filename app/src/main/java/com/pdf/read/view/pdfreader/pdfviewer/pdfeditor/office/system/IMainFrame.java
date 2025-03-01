package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.util.List;

public interface IMainFrame {

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

    Activity getActivity();

    boolean doActionEvent(int actionID, Object obj);

    void openFileFinish();

    void updateToolsbarStatus();

    void setFindBackForwardState(boolean state);

    int getBottomBarHeight();

    int getTopBarHeight();

    String getAppName();

    File getTemporaryDirectory();

    boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float xValue, float yValue, byte eventMethodType);

    boolean isDrawPageNumber();

    boolean isShowZoomingMsg();

    boolean isPopUpErrorDlg();

    boolean isShowPasswordDlg();

    boolean isShowProgressBar();

    boolean isShowFindDlg();

    boolean isShowTXTEncodeDlg();

    String getTXTDefaultEncode();

    boolean isTouchZoom();

    boolean isZoomAfterLayoutForWord();

    byte getWordDefaultView();

    String getLocalString(String resName);

    void changeZoom();

    void changePage();

    void completeLayout();

    void error(int errorCode);

    void fullScreen(boolean fullscreen);

    void showProgressBar(boolean visible);

    void updateViewImages(List<Integer> viewList);

    boolean isChangePage();

    boolean isWriteLog();

    void setWriteLog(boolean saveLog);

    boolean isThumbnail();

    void setThumbnail(boolean isThumbnail);

    Object getViewBackground();

    boolean isIgnoreOriginalSize();

    void setIgnoreOriginalSize(boolean ignoreOriginalSize);

    byte getPageListViewMovingPosition();

    void dispose();
}
