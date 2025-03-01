package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control;

import java.util.Vector;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.DialogConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IDialogAction;

public class WPDialogAction implements IDialogAction {

    public IControl control;

    public WPDialogAction(IControl control) {
        this.control = control;
    }

    public void doAction(int id, Vector<Object> model) {
        if (id == DialogConstant.ENCODING_DIALOG_ID) {
        }
    }

    public IControl getControl() {
        return this.control;
    }

    public void dispose() {
        control = null;
    }
}
