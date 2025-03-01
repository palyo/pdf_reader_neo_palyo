package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPViewConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.AbstractView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.IView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class TitleView extends AbstractView {

    private IView pageRoot;

    public TitleView(IElement elem) {
        super();
        this.elem = elem;
    }

    public short getType() {
        return WPViewConstant.TITLE_VIEW;
    }

    public IWord getContainer() {
        if (pageRoot != null) {
            return pageRoot.getContainer();
        }
        return null;
    }

    public IControl getControl() {
        if (pageRoot != null) {
            return pageRoot.getControl();
        }
        return null;
    }

    public IDocument getDocument() {
        if (pageRoot != null) {
            return pageRoot.getDocument();
        }
        return null;
    }

    public void setPageRoot(IView root) {
        pageRoot = root;
    }

    public void dispose() {
        super.dispose();
        pageRoot = null;
    }
}
