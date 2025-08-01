package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control;

import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.ShapeAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.Highlight;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IHighlight;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.control.IWord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.STRoot;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class PGEditor implements IWord {

    private TextBox editorTextBox;
    private IHighlight highlight;
    private Presentation pgView;
    private Map<Integer, IAnimation> paraAnimation;

    public PGEditor(Presentation pgView) {
        this.pgView = pgView;
        highlight = new Highlight(this);
    }

    public IHighlight getHighlight() {
        return highlight;
    }

    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {
        if (editorTextBox != null) {
            STRoot root = editorTextBox.getRootView();
            if (root != null) {
                root.modelToView(offset, rect, isBack);
            }
            rect.x += editorTextBox.getBounds().x;
            rect.y += editorTextBox.getBounds().y;
        }
        return rect;
    }

    public IDocument getDocument() {
        return null;
    }

    public String getText(long start, long end) {
        if (editorTextBox != null) {
            SectionElement elem = editorTextBox.getElement();
            if (elem.getEndOffset() - elem.getStartOffset() > 0) {
                String str = elem.getText(null);
                if (str != null) {
                    return str.substring((int) Math.max(start, elem.getStartOffset()),
                            (int) Math.min(end, elem.getEndOffset()));
                }
            }
        }
        return null;
    }

    public long viewToModel(int x, int y, boolean isBack) {
        if (pgView == null) {
            return -1;
        }
        IShape shape = pgView.getCurrentSlide().getShape(x, y);
        if (shape != null) {
            if (shape.getType() == AbstractShape.SHAPE_TEXTBOX) {
                STRoot root = ((TextBox) shape).getRootView();
                if (root != null) {
                    x -= shape.getBounds().x;
                    y -= shape.getBounds().y;
                    return root.viewToModel(x, y, isBack);
                }
            }
        }
        return -1;
    }

    public TextBox getEditorTextBox() {
        return editorTextBox;
    }

    public void setEditorTextBox(TextBox editorBox) {
        this.editorTextBox = editorBox;
    }

    public byte getEditType() {
        return KeyKt.APPLICATION_TYPE_PPT;
    }

    public void setShapeAnimation(Map<Integer, IAnimation> paraAnimation) {
        this.paraAnimation = paraAnimation;
    }

    public IAnimation getParagraphAnimation(int paragraphID) {
        if (pgView != null && paraAnimation != null) {
            IAnimation animation = paraAnimation.get(paragraphID);
            if (animation == null) {
                animation = paraAnimation.get(ShapeAnimation.Para_All);
            }
            if (animation == null) {
                animation = paraAnimation.get(ShapeAnimation.Para_BG);
            }
            return animation;

        }

        return null;
    }

    public IShape getTextBox() {
        return this.editorTextBox;
    }

    public void clearAnimation() {
        if (paraAnimation != null) {
            paraAnimation.clear();
        }
    }

    public IControl getControl() {
        if (pgView != null) {
            return pgView.getControl();
        }
        return null;
    }

    public Presentation getPGView() {
        return pgView;
    }

    public void dispose() {
        editorTextBox = null;
        if (highlight != null) {
            highlight.dispose();
            highlight = null;
        }
        pgView = null;
        if (paraAnimation != null) {
            paraAnimation.clear();
            paraAnimation = null;
        }

    }
}
