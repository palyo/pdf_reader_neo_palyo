package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPViewConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.IView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.ViewKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control.Word;

public class WPViewKit extends ViewKit {

    private static final WPViewKit kit = new WPViewKit();

    public static WPViewKit instance() {
        return kit;
    }

    public PageView getPageView(IView root, int x, int y) {
        if (root == null) {
            return null;
        }
        IView view = root.getChildView();
        while (view != null) {
            if (y > view.getY() && y < view.getY() + view.getHeight() + WPViewConstant.PAGE_SPACE) {
                break;
            }
            view = view.getNextView();
        }

        if (view == null) {
            view = root.getChildView();
        }
        return view == null ? null : (PageView) view;
    }

    public IView getView(Word word, long offset, int type, boolean isBack) {
        return word.getRoot(word.getCurrentRootType()).getView(offset, type, isBack);
    }

    public IView getView(Word word, int x, int y, int type, boolean isBack) {
        return word.getRoot(word.getCurrentRootType()).getView(x, y, type, isBack);
    }

    public Rectangle getAbsoluteCoordinate(IView view, int type, Rectangle rect) {
        rect.setBounds(0, 0, 0, 0);
        while (view != null && view.getType() != type) {
            rect.x += view.getX();
            rect.y += view.getY();
            view = view.getParentView();
        }
        return rect;
    }

    public long getArea(long offset) {
        return offset & WPModelConstant.AREA_MASK;
    }

}
