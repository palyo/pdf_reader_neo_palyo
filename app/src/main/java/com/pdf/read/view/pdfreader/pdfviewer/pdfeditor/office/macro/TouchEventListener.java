package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.view.MotionEvent;
import android.view.View;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IMainFrame;

public interface TouchEventListener {

    byte EVENT_TOUCH = IMainFrame.ON_TOUCH;

    byte EVENT_DOWN = IMainFrame.ON_DOWN;

    byte EVENT_SHOW_PRESS = IMainFrame.ON_SHOW_PRESS;

    byte EVENT_SINGLE_TAP_UP = IMainFrame.ON_SINGLE_TAP_UP;

    byte EVENT_SCROLL = IMainFrame.ON_SCROLL;

    byte EVENT_LONG_PRESS = IMainFrame.ON_LONG_PRESS;

    byte EVENT_FLING = IMainFrame.ON_FLING;

    byte EVENT_SINGLE_TAP_CONFIRMED = IMainFrame.ON_SINGLE_TAP_CONFIRMED;

    byte EVENT_DOUBLE_TAP = IMainFrame.ON_DOUBLE_TAP;

    byte EVENT_DOUBLE_TAP_EVENT = IMainFrame.ON_DOUBLE_TAP_EVENT;

    byte EVENT_CLICK = IMainFrame.ON_CLICK;

    boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType);
}
