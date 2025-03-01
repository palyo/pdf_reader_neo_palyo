package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ISlideShow;

public abstract class AbstractControl implements IControl {

    @Override
    public void layoutView(int x, int y, int w, int h) {

    }

    @Override
    public void actionEvent(int actionID, Object obj) {
    }

    @Override
    public Object getActionValue(int actionID, Object obj) {
        return null;
    }

    @Override
    public int getCurrentViewIndex() {
        return -1;
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public Dialog getDialog(Activity activity, int id) {
        return null;
    }

    @Override
    public IMainFrame getMainFrame() {
        return null;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public IFind getFind() {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isAutoTest() {
        return false;
    }

    @Override
    public IOfficeToPicture getOfficeToPicture() {
        return null;
    }

    public ICustomDialog getCustomDialog() {
        return null;
    }

    public boolean isSlideShow() {
        return false;
    }

    public ISlideShow getSlideShow() {
        return null;
    }

    @Override
    public boolean openFile(String filePath) {
        return false;
    }

    @Override
    public IReader getReader() {
        return null;
    }

    @Override
    public byte getApplicationType() {
        return -1;
    }
}
