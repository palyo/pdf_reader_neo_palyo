package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.view;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.BackgroundDrawer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.PaintKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.AutoShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.PictureKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AutoShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.GroupShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.PictureShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.SmartArt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableCell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.OEPlaceholderAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Dimension;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectanglef;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.IAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.ShapeAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control.PGEditor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control.Presentation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGSlide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.LeafElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.ParagraphElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.view.STRoot;

public class SlideDrawKit {
    private static SlideDrawKit kit;
    private final Rect brRect = new Rect();

    public static SlideDrawKit instance() {
        if (kit == null) {
            kit = new SlideDrawKit();
        }
        return kit;
    }

    public void drawSlide(Canvas canvas, PGModel pgModel, PGEditor editor, PGSlide slide, float zoom) {
        drawSlide(canvas, pgModel, editor, slide, zoom, null);
    }

    public void drawSlide(Canvas canvas, PGModel pgModel, PGEditor editor, PGSlide slide, float zoom, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        synchronized (this) {
            Dimension d = pgModel.getPageSize();
            brRect.set(0, 0, (int) (d.width * zoom), (int) (d.height * zoom));

            if (!BackgroundDrawer.drawBackground(canvas, editor.getControl(), slide.getSlideNo(), slide.getBackgroundAndFill(), brRect, null, zoom)) {
                canvas.drawColor(Color.white.getRGB());
            }

            int[] indexs = slide.getMasterIndexs();
            for (int i = 0; i < indexs.length; i++) {
                drawShapes(canvas, pgModel, editor, pgModel.getSlideMaster(indexs[i]), slide.getSlideNo(), zoom, shapeVisible);
            }

            drawShapes(canvas, pgModel, editor, slide, slide.getSlideNo(), zoom, shapeVisible);
        }
    }

    private void drawShapes(Canvas canvas, PGModel pgModel, PGEditor editor, PGSlide slide, int slideNo, float zoom, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        if (slide != null) {
            int count = slide.getShapeCount();
            for (int i = 0; i < count; i++) {
                IShape shape = slide.getShape(i);
                if (shape.isHidden()) {
                    continue;
                }

                int placeHolderID = shape.getPlaceHolderID();
                boolean draw = false;
                if (slide.getSlideType() == PGSlide.Slide_Normal) {
                    draw = true;
                } else if (placeHolderID == OEPlaceholderAtom.None
                        || placeHolderID == OEPlaceholderAtom.Object
                        || placeHolderID == OEPlaceholderAtom.Graph
                        || placeHolderID == OEPlaceholderAtom.Table
                        || placeHolderID == OEPlaceholderAtom.ClipArt
                        || placeHolderID == OEPlaceholderAtom.OrganizationChart
                        || placeHolderID == OEPlaceholderAtom.MediaClip) {
                    draw = true;
                }

                if (draw) {
                    drawShape(canvas, pgModel, editor, slideNo, shape, zoom, shapeVisible);
                }
            }
        }
    }

    private Rect getShapeRect(IShape shape, float zoom) {
        Rectangle shapeRect = shape.getBounds();
        int left = Math.round(shapeRect.x * zoom);
        int top = Math.round(shapeRect.y * zoom);
        int width = Math.round(shapeRect.width * zoom);
        int height = Math.round(shapeRect.height * zoom);
        return new Rect(left, top, left + width, top + height);
    }

    private void drawShape(Canvas canvas, PGModel pgModel, PGEditor editor, int slideNo, IShape shape, float zoom, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        canvas.save();

        if (shape instanceof GroupShape) {
            Rect rect = getShapeRect(shape, zoom);

            if (shape.getFlipVertical()) {
                canvas.translate(rect.left, rect.bottom);
                canvas.scale(1, -1);
                canvas.translate(-rect.left, -rect.top);
            }

            if (shape.getFlipHorizontal()) {
                canvas.translate(rect.right, rect.top);
                canvas.scale(-1, 1);
                canvas.translate(-rect.left, -rect.top);
            }

            if (shape.getRotation() != 0) {
                canvas.rotate(shape.getRotation(), rect.exactCenterX(), rect.exactCenterY());
            }

            IShape[] shapes = ((GroupShape) shape).getShapes();
            for (int i = 0; i < shapes.length; i++) {
                IShape childShape = shapes[i];
                if (shape.isHidden()) {
                    continue;
                }
                drawShape(canvas, pgModel, editor, slideNo, childShape, zoom, shapeVisible);
            }
        } else {
            if (shape.getType() == AbstractShape.SHAPE_SMARTART) {
                Rect rect = getShapeRect(shape, zoom);

                SmartArt smartArt = (SmartArt) shape;
                BackgroundDrawer.drawLineAndFill(canvas, editor.getControl(), slideNo, smartArt, rect, zoom);

                canvas.translate(rect.left, rect.top);

                IShape[] shapes = smartArt.getShapes();
                for (IShape item : shapes) {
                    drawShape(canvas, pgModel, editor, slideNo, item, zoom, shapeVisible);
                }
            } else if (shape.getType() == AbstractShape.SHAPE_TEXTBOX) {

                drawTextShape(canvas, pgModel, editor, slideNo, (TextBox) shape, zoom, shapeVisible);
            } else {

                if (shape.getType() == AbstractShape.SHAPE_LINE || shape.getType() == AbstractShape.SHAPE_AUTOSHAPE) {
                    AutoShapeKit.instance().drawAutoShape(canvas, editor.getControl(), slideNo, (AutoShape) shape, zoom);
                } else if (shape.getType() == AbstractShape.SHAPE_PICTURE) {
                    drawPicture(canvas, editor, slideNo, (PictureShape) shape, zoom);
                } else if (shape.getType() == AbstractShape.SHAPE_CHART) {
                    drawChart(canvas, editor, (AChart) shape, zoom);
                } else if (shape.getType() == AbstractShape.SHAPE_TABLE) {
                    drawTable(canvas, pgModel, editor, slideNo, (TableShape) shape, zoom, shapeVisible);
                }
            }
        }

        canvas.restore();
    }

    private void drawTextShape(Canvas canvas, PGModel pgModel, PGEditor editor, int slideNo, TextBox shape, float zoom, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        Rectangle rect = shape.getBounds();

        SectionElement elem = shape.getElement();
        if (elem == null || elem.getEndOffset() - elem.getStartOffset() == 0) {
            return;
        }

        canvas.save();
        IDocument doc = null;
        STRoot root = shape.getRootView();
        Presentation pgView = editor.getPGView();

        if (pgView != null && root == null
                && (shape.getMCType() == TextBox.MC_SlideNumber || shape.getPlaceHolderID() == OEPlaceholderAtom.MasterSlideNumber)) {
            doc = pgModel.getRenderersDoc();
            doc.appendSection(elem);

            String pageNumber = elem.getText(null);
            if (pageNumber != null && pageNumber.contains("*")) {
                pageNumber = pageNumber.replace("*", String.valueOf(slideNo + pgView.getPGModel().getSlideNumberOffset()));

                elem = new SectionElement();
                elem.setStartOffset(0);
                elem.setEndOffset(pageNumber.length());
                elem.setAttribute(shape.getElement().getAttribute().clone());

                ParagraphElement paraElem = (ParagraphElement) shape.getElement().getParaCollection().getElementForIndex(0);

                ParagraphElement paraElemNew = new ParagraphElement();
                paraElemNew.setStartOffset(0);
                paraElemNew.setEndOffset(pageNumber.length());
                paraElemNew.setAttribute(paraElem.getAttribute().clone());
                elem.appendParagraph(paraElemNew, WPModelConstant.MAIN);

                LeafElement leafElem = (LeafElement) paraElem.getElementForIndex(0);

                LeafElement leafElemNew = new LeafElement(pageNumber);
                leafElemNew.setStartOffset(0);
                leafElemNew.setEndOffset(pageNumber.length());
                leafElemNew.setAttribute(leafElem.getAttribute().clone());
                paraElemNew.appendLeaf(leafElemNew);

                shape.setElement(elem);
            }
        }

        if (root == null) {
            doc = pgModel.getRenderersDoc();
            doc.appendSection(elem);
            root = new STRoot(editor, doc);
            root.setWrapLine(shape.isWrapLine());
            root.doLayout();
            shape.setRootView(root);
        }

        if (shapeVisible != null) {
            if (shape.getGroupShapeID() >= 0) {
                editor.setShapeAnimation(shapeVisible.get(shape.getGroupShapeID()));
            } else {
                editor.setShapeAnimation(shapeVisible.get(shape.getShapeID()));
            }

            root.draw(canvas, (int) ((rect.x) * zoom), (int) ((rect.y) * zoom), zoom);
        } else {
            editor.getHighlight().setPaintHighlight(shape == editor.getEditorTextBox());

            root.draw(canvas, (int) ((rect.x) * zoom), (int) ((rect.y) * zoom), zoom);

            editor.getHighlight().setPaintHighlight(false);
        }

        canvas.restore();
    }

    private void drawPicture(Canvas canvas, PGEditor editor, int slideNo, PictureShape pictureShape, float zoom) {
        canvas.save();
        processRotation(canvas, pictureShape, zoom);
        Rectangle r = pictureShape.getBounds();

        Rect rect = getShapeRect(pictureShape, zoom);

        BackgroundDrawer.drawLineAndFill(canvas, editor.getControl(), slideNo, pictureShape, rect, zoom);

        PictureKit.instance().drawPicture(canvas, editor.getControl(), slideNo, pictureShape.getPicture(editor.getControl()),
                r.x * zoom, r.y * zoom, zoom, r.width * zoom, r.height * zoom, pictureShape.getPictureEffectInfor(), pictureShape.getAnimation());
        canvas.restore();
    }

    private void drawChart(Canvas canvas, PGEditor editor, AChart chart, float zoom) {
        IAnimation animation = chart.getAnimation();
        if (animation != null && animation.getCurrentAnimationInfor().getAlpha() == 0) {
            return;
        }

        canvas.save();
        Rectangle rect = chart.getBounds();
        Paint paint = PaintKit.instance().getPaint();
        if (animation != null) {
            ShapeAnimation shapeAnim = animation.getShapeAnimation();
            int paraBegin = shapeAnim.getParagraphBegin();
            int paraEnd = shapeAnim.getParagraphEnd();

            if (paraBegin == ShapeAnimation.Para_All && paraEnd == ShapeAnimation.Para_All
                    || (paraBegin == ShapeAnimation.Para_BG && paraEnd == ShapeAnimation.Para_BG)) {
                int a = animation.getCurrentAnimationInfor().getAlpha();
                paint.setAlpha(a);

                float rate = a / 255f * 0.5f;
                double centerX = rect.getCenterX();
                double centerY = rect.getCenterY();
                rect = new Rectangle(rect);
                rect.x = Math.round((float) (centerX - rect.width * rate));
                rect.y = Math.round((float) (centerY - rect.height * rate));
                rect.width *= rate * 2;
                rect.height *= rate * 2;
                float zoomT = zoom * rate * 2;
                processRotation(canvas, chart, zoomT);
                chart.getAChart().setZoomRate(zoomT);
                chart.getAChart().draw(canvas, editor.getControl(), (int) (rect.x * zoom), (int) (rect.y * zoom),
                        (int) (rect.width * zoom), (int) (rect.height * zoom), paint);
                return;
            }
        }

        processRotation(canvas, chart, zoom);
        chart.getAChart().setZoomRate(zoom);
        chart.getAChart().draw(canvas, editor.getControl(), (int) (rect.x * zoom), (int) (rect.y * zoom),
                (int) (rect.width * zoom), (int) (rect.height * zoom), paint);

        canvas.restore();
    }

    private void drawTable(Canvas canvas, PGModel pgModel, PGEditor editor, int slideNo, TableShape table, float zoom, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        canvas.save();
        processRotation(canvas, table, zoom);
        int alpha = 255;
        if (table.getAnimation() != null
                && (alpha = table.getAnimation().getCurrentAnimationInfor().getAlpha()) != 255) {
            int LAYERS_FLAGS = Canvas.ALL_SAVE_FLAG;

            Rectangle tableRect = table.getBounds();
            if (tableRect != null) {
                canvas.saveLayerAlpha((tableRect.x * zoom), (tableRect.y * zoom),
                        (tableRect.x + tableRect.width + 1) * zoom, (tableRect.height + tableRect.y + 1) * zoom,
                        alpha, LAYERS_FLAGS);
            }
        }
        int count = table.getCellCount();
        for (int i = 0; i < count; i++) {
            TableCell cell = table.getCell(i);
            if (cell != null) {
                Rectanglef rect = cell.getBounds();

                brRect.set(Math.round(rect.getX() * zoom), Math.round(rect.getY() * zoom),
                        Math.round((rect.getX() + rect.getWidth()) * zoom), Math.round((rect.getY() + rect.getHeight()) * zoom));

                BackgroundDrawer.drawBackground(canvas, editor.getControl(), slideNo, cell.getBackgroundAndFill(), brRect, null, zoom);

                {
                    drawCellBorder(canvas, cell, rect, zoom);
                }

                TextBox tb = cell.getText();
                if (tb != null) {
                    drawTextShape(canvas, pgModel, editor, slideNo, cell.getText(), zoom, shapeVisible);
                }
            }
        }

        if (alpha != 255) {
            canvas.restore();
        }

        canvas.restore();
    }

    private void drawCellBorder(Canvas canvas, TableCell cell, Rectanglef rect, float zoom) {
        drawCellBorder(canvas, cell, rect, zoom, null);
    }

    private void drawCellBorder(Canvas canvas, TableCell cell, Rectanglef rect, float zoom, IAnimation animation) {
        Paint paint = PaintKit.instance().getPaint();
        int oldColor = paint.getColor();

        canvas.save();
        float addExd = Math.max(1, zoom);

        Line line = cell.getLeftLine();
        if (line != null) {
            paint.setColor(line.getBackgroundAndFill().getForegroundColor());
            paint.setStrokeWidth(line.getLineWidth() * zoom);
            if (animation != null) {
                paint.setAlpha(animation.getCurrentAnimationInfor().getAlpha());
            }
            canvas.drawRect(rect.getX() * zoom, rect.getY() * zoom, (rect.getX()) * zoom + addExd, (rect.getY() + rect.getHeight()) * zoom, paint);
        }

        line = cell.getTopLine();
        if (line != null) {
            paint.setColor(line.getBackgroundAndFill().getForegroundColor());
            paint.setStrokeWidth(line.getLineWidth() * zoom);
            if (animation != null) {
                paint.setAlpha(animation.getCurrentAnimationInfor().getAlpha());
            }
            canvas.drawRect(rect.getX() * zoom, rect.getY() * zoom, (rect.getX() + rect.getWidth()) * zoom, (rect.getY()) * zoom + addExd, paint);
        }

        line = cell.getRightLine();
        if (line != null) {
            paint.setColor(line.getBackgroundAndFill().getForegroundColor());
            paint.setStrokeWidth(line.getLineWidth() * zoom);
            if (animation != null) {
                paint.setAlpha(animation.getCurrentAnimationInfor().getAlpha());
            }
            canvas.drawRect((rect.getX() + rect.getWidth()) * zoom, rect.getY() * zoom, (rect.getX() + rect.getWidth()) * zoom + addExd,
                    (rect.getY() + rect.getHeight()) * zoom, paint);
        }

        line = cell.getBottomLine();
        if (line != null) {
            paint.setColor(line.getBackgroundAndFill().getForegroundColor());
            paint.setStrokeWidth(line.getLineWidth() * zoom);
            if (animation != null) {
                paint.setAlpha(animation.getCurrentAnimationInfor().getAlpha());
            }
            canvas.drawRect(rect.getX() * zoom, (rect.getY() + rect.getHeight()) * zoom, (rect.getX() + rect.getWidth()) * zoom,
                    (rect.getY() + rect.getHeight()) * zoom + addExd, paint);
        }

        paint.setColor(oldColor);
        canvas.restore();
    }

    private void processRotation(Canvas canvas, IShape shape, float zoom) {
        Rectangle rect = shape.getBounds();
        float angle = shape.getRotation();

        if (shape.getFlipVertical()) {
            angle += 180;
        }
        IAnimation anim = shape.getAnimation();
        if (anim != null) {
            ShapeAnimation shapeAnim = anim.getShapeAnimation();
            if (shapeAnim.getAnimationType() == ShapeAnimation.SA_EMPH) {
                angle += anim.getCurrentAnimationInfor().getAngle();
            }
        }

        if (angle != 0) {
            float px = rect.x + (float) rect.width / 2;
            float py = rect.y + (float) rect.height / 2;
            canvas.rotate(angle, px * zoom, py * zoom);
        }
    }

    public Bitmap slideToImage(PGModel pgModel, PGEditor editor, PGSlide slide) {
        return slideToImage(pgModel, editor, slide, null);
    }

    public Bitmap slideToImage(PGModel pgModel, PGEditor editor, PGSlide slide, Map<Integer, Map<Integer, IAnimation>> shapeVisible) {
        synchronized (this) {
            if (slide == null) {
                return null;
            }

            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);

            Dimension d = pgModel.getPageSize();
            Bitmap bitmap = Bitmap.createBitmap(d.width, d.height, Config.ARGB_8888);
            brRect.set(0, 0, d.width, d.height);
            Canvas canvas = new Canvas(bitmap);

            if (!BackgroundDrawer.drawBackground(canvas, editor.getControl(), slide.getSlideNo(), slide.getBackgroundAndFill(), brRect, null, 1.0f)) {
                canvas.drawColor(Color.white.getRGB());
            }

            int[] indexs = slide.getMasterIndexs();
            for (int i = 0; i < indexs.length; i++) {
                drawShapes(canvas, pgModel, editor, pgModel.getSlideMaster(indexs[i]), slide.getSlideNo(), 1.f, null);
            }

            drawShapes(canvas, pgModel, editor, slide, slide.getSlideNo(), 1, shapeVisible);

            PictureKit.instance().setDrawPictrue(b);

            return bitmap;
        }
    }

    public Bitmap slideAreaToImage(PGModel pgModel, PGEditor editor, PGSlide slide,
                                   int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        synchronized (this) {
            if (slide == null) {
                return null;
            }
            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);

            float paintZoom = Math.min(desWidth / (float) srcWidth, desHeight / (float) srcHeight);
            Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap((int) (srcWidth * paintZoom), (int) (srcHeight * paintZoom), Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                return null;
            }
            if (bitmap == null) {
                return null;
            }
            Dimension d = pgModel.getPageSize();
            Canvas canvas = new Canvas(bitmap);
            brRect.set(0, 0, (int) (d.getWidth() * paintZoom), (int) (d.getHeight() * paintZoom));
            canvas.translate(-srcLeft * paintZoom, -srcTop * paintZoom);

            canvas.drawColor(Color.white.getRGB());

            if (!BackgroundDrawer.drawBackground(canvas, editor.getControl(), slide.getSlideNo(), slide.getBackgroundAndFill(), brRect, null, 1.f)) {

            }

            int[] indexs = slide.getMasterIndexs();
            for (int i = 0; i < indexs.length; i++) {

                drawShapes(canvas, pgModel, editor, pgModel.getSlideMaster(indexs[i]), slide.getSlideNo(), paintZoom, null);
            }

            drawShapes(canvas, pgModel, editor, slide, slide.getSlideNo(), paintZoom, null);

            PictureKit.instance().setDrawPictrue(b);

            return bitmap;
        }
    }

    public Bitmap getThumbnail(PGModel pgModel, PGEditor editor, PGSlide slide, float zoom) {
        synchronized (this) {
            if (slide == null) {
                return null;
            }
            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);

            Dimension d = pgModel.getPageSize();
            int w = (int) (d.width * zoom);
            int h = (int) (d.height * zoom);
            Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            brRect.set(0, 0, w, h);
            canvas.drawColor(Color.white.getRGB());

            if (!BackgroundDrawer.drawBackground(canvas, editor.getControl(), slide.getSlideNo(), slide.getBackgroundAndFill(), brRect, null, 1.f)) {

            }

            int[] indexs = slide.getMasterIndexs();
            for (int i = 0; i < indexs.length; i++) {

                drawShapes(canvas, pgModel, editor, pgModel.getSlideMaster(indexs[i]), slide.getSlideNo(), zoom, null);
            }

            drawShapes(canvas, pgModel, editor, slide, slide.getSlideNo(), zoom, null);

            PictureKit.instance().setDrawPictrue(b);
            return bitmap;
        }
    }

    public void disposeOldSlideView(PGModel pgModel, PGSlide slide) {
        if (slide != null) {
            int count = slide.getShapeCount();
            for (int i = 0; i < count; i++) {
                IShape shape = slide.getShape(i);
                if (shape.getType() == AbstractShape.SHAPE_TEXTBOX) {
                    STRoot root = ((TextBox) shape).getRootView();
                    if (root != null) {
                        root.dispose();
                        ((TextBox) shape).setRootView(null);
                    }
                } else if (shape.getType() == AbstractShape.SHAPE_TABLE) {
                    int cellCount = ((TableShape) shape).getCellCount();
                    for (int j = 0; j < cellCount; j++) {
                        TableCell cell = ((TableShape) shape).getCell(j);
                        if (cell != null) {
                            TextBox tb = cell.getText();
                            if (tb != null) {
                                STRoot root = tb.getRootView();
                                if (root != null) {
                                    root.dispose();
                                    tb.setRootView(null);
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
