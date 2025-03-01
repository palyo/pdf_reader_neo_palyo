package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import java.util.Vector;

public interface IDialogAction {

    void doAction(int id, Vector<Object> model);

    IControl getControl();

    void dispose();
}
