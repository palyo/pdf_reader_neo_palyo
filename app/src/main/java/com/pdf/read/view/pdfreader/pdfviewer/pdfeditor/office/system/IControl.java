package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ISlideShow;

public interface IControl {

    void layoutView(int x, int y, int w, int h);

    void actionEvent(int actionID, Object obj);

    Object getActionValue(int actionID, Object obj);

    int getCurrentViewIndex();

    View getView();

    Dialog getDialog(Activity activity, int id);

    IMainFrame getMainFrame();

    Activity getActivity();

    IFind getFind();

    boolean isAutoTest();

    IOfficeToPicture getOfficeToPicture();

    ICustomDialog getCustomDialog();

    boolean isSlideShow();

    ISlideShow getSlideShow();

    IReader getReader();

    boolean openFile(String filePath);

    byte getApplicationType();

    SysKit getSysKit();

    void dispose();
}
