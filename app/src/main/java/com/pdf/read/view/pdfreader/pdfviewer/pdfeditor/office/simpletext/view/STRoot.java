package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPAttrConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPViewConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.ShapeAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttrManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view.LayoutKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view.LineView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view.ParagraphView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.view.ViewFactory;

public class STRoot extends AbstractView implements IRoot {
    private boolean isWrapLine;
    private IDocument doc;
    private PageAttr pageAttr;
    private ParaAttr paraAttr;
    private DocAttr docAttr;
    private IWord container;

    public STRoot(IWord container, IDocument doc) {
        this.doc = doc;
        this.container = container;
        docAttr = new DocAttr();
        paraAttr = new ParaAttr();
        pageAttr = new PageAttr();
    }

    public short getType() {
        return WPViewConstant.SIMPLE_ROOT;
    }

    public IWord getContainer() {
        return container;
    }

    public IDocument getDocument() {
        return doc;
    }

    public IControl getControl() {
        return container.getControl();
    }

    public void doLayout() {
        IElement sec = doc.getSection(WPModelConstant.MAIN);
        AttrManage.instance().fillPageAttr(pageAttr, sec.getAttribute());

        int dx = pageAttr.leftMargin;
        int dy = pageAttr.topMargin;
        setTopIndent(pageAttr.topMargin);
        setLeftIndent(pageAttr.leftMargin);
        int spanW = (isWrapLine ? pageAttr.pageWidth : Integer.MAX_VALUE) - pageAttr.leftMargin - pageAttr.rightMargin;

        spanW = Math.max(IRoot.MINLAYOUTWIDTH, spanW);

        int spanH = Integer.MAX_VALUE;

        int flag = ViewKit.instance().setBitValue(0, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);

        flag = ViewKit.instance().setBitValue(0, WPViewConstant.LAYOUT_NOT_WRAP_LINE,
                !isWrapLine || pageAttr.horizontalAlign == WPAttrConstant.PAGE_H_CENTER);

        long maxEnd = doc.getAreaEnd(WPModelConstant.MAIN);
        int currentParaIndex = 0;
        long currentLayoutOffset = 0;

        IElement elem = doc.getParagraphForIndex(currentParaIndex++, WPModelConstant.MAIN);
        ParagraphView para = (ParagraphView) ViewFactory.createView(container.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
        appendChlidView(para);
        para.setStartOffset(currentLayoutOffset);
        para.setEndOffset(elem.getEndOffset());
        int maxParaWidth = 0;
        int contentHeight = 0;
        boolean firstPara = true;
        int paraCount = doc.getParaCount(maxEnd);
        while (spanH > 0 && currentLayoutOffset < maxEnd) {
            para.setLocation(dx, dy);
            AttrManage.instance().fillParaAttr(container.getControl(), paraAttr, elem.getAttribute());
            if (container.getEditType() == KeyKt.APPLICATION_TYPE_PPT) {
                if (firstPara) {
                    paraAttr.beforeSpace = 0;
                }
                if (paraCount == currentParaIndex) {
                    paraAttr.afterSpace = 0;
                }
            }
            LayoutKit.instance().layoutPara(container.getControl(), doc, docAttr, pageAttr, paraAttr, para,
                    currentLayoutOffset, dx, dy, spanW, Integer.MAX_VALUE, flag);
            int paraHeight = para.getLayoutSpan(WPViewConstant.Y_AXIS);
            dy += paraHeight;
            currentLayoutOffset = para.getEndOffset(null);
            spanH -= paraHeight;
            contentHeight += paraHeight;
            maxParaWidth = Math.max(maxParaWidth, para.getLayoutSpan(WPViewConstant.X_AXIS));
            if (spanH > 0 && currentLayoutOffset < maxEnd) {
                elem = doc.getParagraphForIndex(currentParaIndex++, WPModelConstant.MAIN);
                if (elem == null) {
                    break;
                }
                para = (ParagraphView) ViewFactory.createView(container.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
                para.setStartOffset(currentLayoutOffset);
                appendChlidView(para);
            }
            firstPara = false;
        }

        if (pageAttr.horizontalAlign == WPAttrConstant.PAGE_H_LEFT) {
            paraAlign(pageAttr.horizontalAlign == WPAttrConstant.PAGE_H_CENTER ? maxParaWidth :
                    pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin);
        }
        layoutPageAlign(contentHeight, maxParaWidth);
    }

    private void paraAlign(int maxParaWidth) {
        if (isWrapLine) {
            return;
        }
        IView paraView = getChildView();
        while (paraView != null) {
            paraAttr.horizontalAlignment = (byte) AttrManage.instance().getParaHorizontalAlign(paraView.getElement().getAttribute());
            IView line = paraView.getChildView();
            while (line != null) {
                if (line.getType() == WPViewConstant.LINE_VIEW) {
                    ((LineView) line).layoutAlignment(docAttr, pageAttr, paraAttr, ((ParagraphView) paraView).getBNView(), maxParaWidth, 0, false);
                }
                line = line.getNextView();
            }
            paraView = paraView.getNextView();
        }
    }

    private void layoutPageAlign(int contentHeight, int maxParaWidth) {
        int spanHeight = pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin;
        int addSpan = 0;
        switch (pageAttr.verticalAlign) {
            case WPAttrConstant.PAGE_V_CENTER:
                addSpan = (spanHeight - contentHeight) / 2;
                break;
            case WPAttrConstant.PAGE_V_BOTTOM:
                addSpan = spanHeight - contentHeight;
                break;
            default:
                break;
        }
        setY(addSpan);
        setTopIndent(addSpan);

        if (pageAttr.horizontalAlign == WPAttrConstant.PAGE_H_CENTER) {
            int pageWidth = pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin;
            addSpan = (pageWidth - maxParaWidth) / 2;
            IView view = getChildView();
            while (view != null) {
                paraAttr.horizontalAlignment = (byte) AttrManage.instance().getParaHorizontalAlign(view.getElement().getAttribute());
                IView line = view.getChildView();
                while (line != null && line.getType() == WPViewConstant.LINE_VIEW) {
                    ((LineView) line).layoutAlignment(docAttr, pageAttr, paraAttr, ((ParagraphView) view).getBNView(), maxParaWidth, 0, false);
                    line.setX(line.getX() + addSpan);
                    line = line.getNextView();
                }
                view = view.getNextView();
            }
        }
    }

    public String getText() {
        String text = "";
        IView view = getChildView();
        while (view != null) {
            text += ((ParagraphView) view).getText();
            view = view.getNextView();
        }

        return text;
    }

    public void draw(Canvas canvas, int originX, int originY, float zoom) {
        List<Integer> linesHeight = new ArrayList<Integer>(10);
        if (container != null) {
            int dX = (int) (x * zoom) + originX;
            int dY = (int) (y * zoom) + originY;
            IView view = getChildView();
            Rect clip = canvas.getClipBounds();
            int paraID = 0;
            while (view != null) {
                if (view.intersection(clip, dX, dY, zoom)) {
                    IAnimation animation = container.getParagraphAnimation(paraID);
                    if (animation != null) {
                        ShapeAnimation shapeAnim = animation.getShapeAnimation();
                        int paraBegin = shapeAnim.getParagraphBegin();
                        int paraEnd = shapeAnim.getParagraphEnd();
                        if (paraBegin == ShapeAnimation.Para_All && paraEnd == ShapeAnimation.Para_All
                                || (paraBegin == ShapeAnimation.Para_BG && paraEnd == ShapeAnimation.Para_BG)
                                || (paraBegin >= 0 && paraEnd >= 0 && paraID >= paraBegin && paraID <= paraEnd)) {
                            Rect area = view.getViewRect(dX, dY, zoom);
                            int frameCnt = (int) (animation.getFPS() * animation.getDuration() / 1000f);
                            float progress = animation.getCurrentAnimationInfor().getProgress();
                            if (shapeAnim.getAnimationType() == ShapeAnimation.SA_ENTR) {
                                double a = (area.bottom + KeyKt.GAP) * 2 / Math.pow(frameCnt, 2);
                                int y = (int) (dY - (area.bottom + KeyKt.GAP) + 0.5f * a * Math.pow(frameCnt * progress, 2));
                                if (view.intersection(clip, dX, y, zoom)) {
                                    if (view.intersection(clip, dX, y, zoom)) {
                                        view.draw(canvas, dX, y, zoom);
                                    }
                                }
                            } else if (shapeAnim.getAnimationType() == ShapeAnimation.SA_EMPH) {
                                canvas.save();
                                canvas.rotate(animation.getCurrentAnimationInfor().getAngle(), area.centerX(), area.centerY());
                                view.draw(canvas, dX, dY, zoom);
                                canvas.restore();
                            } else if (shapeAnim.getAnimationType() == ShapeAnimation.SA_EXIT) {
                                double a = (clip.bottom - area.top + KeyKt.GAP) * 2 / Math.pow(frameCnt, 2);
                                int y = (int) (dY + (clip.bottom - area.top + KeyKt.GAP) - 0.5f * a * Math.pow(frameCnt * (1 - progress), 2));
                                if (view.intersection(clip, dX, y, zoom)) {
                                    view.draw(canvas, dX, y, zoom);
                                }
                            } else {
                                view.draw(canvas, dX, dY, zoom);
                            }
                        }
                    } else {
                        view.draw(canvas, dX, dY, zoom);
                    }
                }
                paraID++;
                view = view.getNextView();
            }
        } else {
            super.draw(canvas, originX, originY, zoom);
        }
    }

    public boolean canBackLayout() {
        return false;
    }

    public void backLayout() {

    }

    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {
        IView view = getView(offset, WPViewConstant.PARAGRAPH_VIEW, isBack);
        if (view != null) {
            view.modelToView(offset, rect, isBack);
        }
        rect.x += getX();
        rect.y += getY();
        return rect;
    }

    public long viewToModel(int x, int y, boolean isBack) {
        x -= getX();
        y -= getY();
        IView view = getChildView();
        while (view != null) {
            if (y >= view.getY() && y < view.getY() + view.getLayoutSpan(WPViewConstant.Y_AXIS)) {
                break;
            }
            view = view.getNextView();
        }
        if (view != null) {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
    }

    public ViewContainer getViewContainer() {
        return null;
    }

    public void setWrapLine(boolean b) {
        this.isWrapLine = b;
    }

    public void dispose() {
        super.dispose();
        doc = null;
        container = null;
        pageAttr = null;
        paraAttr = null;
        docAttr = null;
    }
}
