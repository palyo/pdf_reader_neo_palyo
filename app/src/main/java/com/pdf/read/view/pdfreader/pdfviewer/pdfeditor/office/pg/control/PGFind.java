package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGSlide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IFind;

public class PGFind implements IFind {
    protected Rectangle rect;
    private boolean isSetPointToVisible;
    private Presentation presentation;
    private String query;
    private int shapeIndex = -1;
    private int slideIndex = -1;
    private int startOffset = -1;

    public PGFind(Presentation presentation) {
        this.presentation = presentation;
        rect = new Rectangle();
    }

    public boolean find(String query) {
        if (query == null) {
            return false;
        }
        this.query = query;
        startOffset = -1;
        shapeIndex = -1;

        int slideIndex = presentation.getCurrentIndex();
        do {
            if (findSlideForward(slideIndex)) {
                return true;
            }
            if (++slideIndex == presentation.getRealSlideCount()) {
                slideIndex = 0;
            }
        }
        while (slideIndex != presentation.getCurrentIndex());
        return false;
    }

    public boolean findBackward() {
        if (query == null) {
            return false;
        }
        int slideIndex = presentation.getCurrentIndex();
        do {
            if (findSlideBackward(slideIndex)) {
                return true;
            }
            startOffset = -1;
            shapeIndex = -1;
        }
        while (--slideIndex >= 0);
        return false;
    }

    public boolean findForward() {
        if (query == null) {
            return false;
        }
        int slideIndex = presentation.getCurrentIndex();
        do {
            if (findSlideForward(slideIndex)) {
                return true;
            }
            startOffset = -1;
            shapeIndex = -1;
        }
        while (++slideIndex != presentation.getRealSlideCount());
        return false;
    }

    private boolean findSlideBackward(int slideIndex) {
        PGSlide slide = presentation.getSlide(slideIndex);
        int i = shapeIndex >= 0 ? shapeIndex : slide.getShapeCountForFind() - 1;
        for (; i >= 0; i--) {
            IShape shape = slide.getShapeForFind(i);
            if (shape != null && shape.getType() == AbstractShape.SHAPE_TEXTBOX) {
                int offset = shapeIndex == i && presentation.getCurrentIndex() == slideIndex ? startOffset : -1;
                SectionElement elem = ((TextBox) shape).getElement();
                if (elem == null || (offset >= 0 && offset < query.length()) || elem.getEndOffset() - elem.getStartOffset() == 0) {
                    continue;
                }
                if (offset >= 0) {
                    offset = elem.getText(presentation.getRenderersDoc()).lastIndexOf(query, Math.max(startOffset - query.length(), 0));
                } else {
                    offset = elem.getText(presentation.getRenderersDoc()).lastIndexOf(query);
                }
                if (offset >= 0) {
                    startOffset = offset;
                    shapeIndex = i;
                    addHighlight(slideIndex, (TextBox) shape);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findSlideForward(int slideIndex) {
        PGSlide slide = presentation.getSlide(slideIndex);
        for (int i = Math.max(0, shapeIndex); i < slide.getShapeCountForFind(); i++) {
            IShape shape = slide.getShapeForFind(i);
            if (shape != null && shape.getType() == AbstractShape.SHAPE_TEXTBOX) {
                SectionElement elem = ((TextBox) shape).getElement();
                if (elem == null || elem.getEndOffset() - elem.getStartOffset() == 0) {
                    continue;
                }
                int offset = shapeIndex == i && presentation.getCurrentIndex() == slideIndex ? startOffset : -1;
                if (offset >= 0) {
                    offset = elem.getText(presentation.getRenderersDoc()).indexOf(query, startOffset + query.length());
                } else {
                    offset = elem.getText(presentation.getRenderersDoc()).indexOf(query);
                }
                if (offset >= 0) {
                    startOffset = offset;
                    shapeIndex = i;
                    addHighlight(slideIndex, (TextBox) shape);
                    return true;
                }
            }
        }
        return false;
    }

    public void addHighlight(int slideIndex, TextBox textBox) {
        boolean invalidate = true;
        if (slideIndex != presentation.getCurrentIndex()) {
            presentation.showSlide(slideIndex, true);
            isSetPointToVisible = true;
            invalidate = false;
        } else {
            rect.setBounds(0, 0, 0, 0);
            presentation.getEditor().modelToView(startOffset, rect, false);
            if (!presentation.getPrintMode().getListView().isPointVisibleOnScreen(rect.x, rect.y)) {
                presentation.getPrintMode().getListView().setItemPointVisibleOnScreen(rect.x, rect.y);
                invalidate = false;
            } else {
                presentation.getPrintMode().exportImage(presentation.getPrintMode().getListView().getCurrentPageView(), null);
            }
        }
        if (invalidate) {
            presentation.postInvalidate();
        }
        this.slideIndex = slideIndex;
        presentation.getEditor().setEditorTextBox(textBox);
        presentation.getEditor().getHighlight().addHighlight(startOffset, startOffset + query.length());

        presentation.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);

    }

    public void onConfigurationChanged() {
        PGSlide slide = presentation.getCurrentSlide();
        if (shapeIndex >= 0 && shapeIndex < slide.getShapeCountForFind()) {
            presentation.getEditor().getHighlight().addHighlight(startOffset, startOffset + query.length());
            presentation.postInvalidate();
        }
    }

    public boolean isSetPointToVisible() {
        return isSetPointToVisible;
    }

    public void setSetPointToVisible(boolean isSetPointToVisible) {
        this.isSetPointToVisible = isSetPointToVisible;
    }

    public int getPageIndex() {
        return slideIndex;
    }

    public void resetSearchResult() {

    }

    public void dispose() {
        presentation = null;
        query = null;
    }
}
