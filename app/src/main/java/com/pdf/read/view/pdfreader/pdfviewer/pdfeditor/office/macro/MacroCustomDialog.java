package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;

public class MacroCustomDialog implements ICustomDialog {

    private DialogListener dailogListener;

    protected MacroCustomDialog(DialogListener listener) {
        this.dailogListener = listener;
    }

    public void showDialog(byte type) {
        if (dailogListener != null) {
            dailogListener.showDialog(type);
        }
    }

    public void dismissDialog(byte type) {
        if (dailogListener != null) {
            dailogListener.dismissDialog(type);
        }

    }

    public void dispose() {
        dailogListener = null;
    }

}
