package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.graphics.Bitmap;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;

class MacroOfficeToPicture implements IOfficeToPicture {

    private OfficeToPictureListener officeToPictureListener;
    private byte modeType = VIEW_CHANGE_END;

    protected MacroOfficeToPicture(OfficeToPictureListener listener) {
        this.officeToPictureListener = listener;
    }

    public byte getModeType() {
        return modeType;
    }

    public void setModeType(byte modeType) {
        this.modeType = modeType;
    }

    public Bitmap getBitmap(int componentWidth, int componentHeight) {
        if (officeToPictureListener != null) {
            return officeToPictureListener.getBitmap(componentWidth, componentHeight);

        }
        return null;
    }

    public void callBack(Bitmap bitmap) {
        if (officeToPictureListener != null) {
            officeToPictureListener.callBack(bitmap);
        }
    }

    public boolean isZoom() {
        return true;
    }

    public void dispose() {
        officeToPictureListener = null;
    }
}
