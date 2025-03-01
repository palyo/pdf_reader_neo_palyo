package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPViewConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.AbstractView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.IRoot;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.IView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.ViewContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control.Word;

public class PageRoot extends AbstractView implements IRoot {

    private int paraCount;

    private boolean canBackLayout;

    private LayoutThread layoutThread;

    private Word word;

    private WPLayouter wpLayouter;

    private ViewContainer viewContainer;

    private List<PageView> pages;

    public PageRoot(Word word) {
        this.word = word;
        layoutThread = new LayoutThread(this);
        wpLayouter = new WPLayouter(this);
        viewContainer = new ViewContainer();
        pages = new ArrayList<PageView>();
        canBackLayout = true;

    }

    public short getType() {
        return WPViewConstant.PAGE_ROOT;
    }

    public int doLayout(int x, int y, int w, int h, int maxEnd, int flag) {
        try {
            IDocument doc = getDocument();
            setParaCount(doc.getParaCount(WPModelConstant.MAIN));
            wpLayouter.doLayout();
            if (!wpLayouter.isLayoutFinish() && !word.getControl().getMainFrame().isThumbnail()) {
                layoutThread.start();
                word.getControl().actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);

            } else {
                word.getControl().actionEvent(EventConstant.WP_LAYOUT_COMPLETED, true);
                word.getControl().actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
            }
        } catch (Exception e) {
            word.getControl().getSysKit().getErrorKit().writerLog(e);
        }
        return WPViewConstant.BREAK_NO;
    }

    public synchronized void draw(Canvas canvas, int originX, int originY, float zoom) {
        super.draw(canvas, originX, originY, zoom);
    }

    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {

        IView view = viewContainer.getParagraph(offset, isBack);
        if (view != null) {
            view.modelToView(offset, rect, isBack);

            IView p = view.getParentView();
            while (p != null && p.getType() != WPViewConstant.PAGE_ROOT) {

                {
                    rect.x += p.getX();
                    rect.y += p.getY();
                }
                p = p.getParentView();
            }
        }
        rect.x += getX();
        rect.y += getY();
        return rect;

    }

    public long viewToModel(int x, int y, boolean isBack) {
        x -= getX();
        y -= getY();
        IView view = getChildView();
        if (view != null && y > view.getY()) {
            while (view != null) {
                if (y >= view.getY() && y <= view.getY() + view.getHeight() + KeyKt.GAP / 2) {
                    break;
                }
                view = view.getNextView();
            }
        }
        view = view == null ? getChildView() : view;
        if (view != null) {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
    }

    public IDocument getDocument() {
        return word.getDocument();
    }

    public IWord getContainer() {
        return word;
    }

    public IControl getControl() {
        return word.getControl();
    }

    public boolean canBackLayout() {
        return canBackLayout && !wpLayouter.isLayoutFinish();
    }

    public synchronized void backLayout() {
        wpLayouter.backLayout();
        word.postInvalidate();

        if (wpLayouter.isLayoutFinish()) {
            word.getControl().actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
            word.getControl().actionEvent(EventConstant.WP_LAYOUT_COMPLETED, true);
        }
        word.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);

        LayoutKit.instance().layoutAllPage(this, word.getZoom());

        word.layoutPrintMode();
    }

    public int getParaCount() {
        return paraCount;
    }

    public void setParaCount(int paraCount) {
        this.paraCount = paraCount;
    }

    public int getPageCount() {
        return getChildCount();
    }

    public int getChildCount() {
        if (pages != null) {
            return pages.size();
        }
        return 1;
    }

    public ViewContainer getViewContainer() {
        return this.viewContainer;
    }

    public void addPageView(PageView pv) {
        pages.add(pv);
    }

    public PageView getPageView(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= pages.size()) {
            return null;
        }
        return pages.get(pageIndex);
    }

    public boolean checkUpdateHeaderFooterFieldText() {
        boolean hasTotalPageCode = false;
        for (PageView page : pages) {
            hasTotalPageCode = hasTotalPageCode || page.checkUpdateHeaderFooterFieldText(pages.size());
        }

        return hasTotalPageCode;
    }

    public synchronized void dispose() {
        super.dispose();
        canBackLayout = false;
        if (layoutThread != null) {
            layoutThread.dispose();
            layoutThread = null;
        }
        if (wpLayouter != null) {
            wpLayouter.dispose();
            wpLayouter = null;
        }
        if (viewContainer != null) {
            viewContainer.dispose();
            viewContainer = null;
        }
        if (pages != null) {
            pages.clear();
            pages = null;
        }
        word = null;
    }
}
