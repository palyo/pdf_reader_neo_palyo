package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PointF;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.PaintKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.ExtendPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.Gradient;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.LinearGradientShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.RadialGradientShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.TileShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.hyperlink.Hyperlink;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfoFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ArbitraryPolygonShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.Arrow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AutoShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.GroupShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.LineShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.PictureShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableCell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.AutoShapeConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPAttrConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.FCKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.HSLFSlideShow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Fill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.HeadersFooters;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Line;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.MasterSheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Notes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Shape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.ShapeGroup;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Sheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.SimpleShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Slide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.SlideMaster;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TitleMaster;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.BinaryTagDataBlob;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.ClientVisualElementContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.DocumentAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.OEPlaceholderAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.PositionDependentRecordContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.Record;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.SlideAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.SlideAtom.SSlideLayoutAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.SlideProgBinaryTagContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.SlideProgTagsContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.SlideShowSlideInfoAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeAnimateBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeColorBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeCommandBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeEffectBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeMotionBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeNodeAttributeContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeNodeContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeRotationBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeScaleBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeSetBehaviorContainer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TimeVariant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.VisualShapeAtom;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.PictureData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.RichTextRun;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.SlideShow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.bulletnumber.BulletNumberManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Dimension;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectanglef;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom.Rectangle2D;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.ShapeAnimation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGNotes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGSlide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font.FontTypefaceManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttrManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IAttributeSet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.LeafElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.ParagraphElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.format.NumericFormatter;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.AbstractReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.BackReaderThread;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class PPTReader extends AbstractReader {
    public static final int FIRST_READ_SLIDE_NUM = 2;
    public static final float POINT_PER_LINE_PER_FONTSIZE = 1.2f;
    public static final int DEFAULT_CELL_WIDTH = 100;
    public static final int DEFAULT_CELL_HEIGHT = 40;
    private final boolean isGetThumbnail;
    private int number = 1;
    private int currentReaderIndex;
    private String filePath;
    private PGModel model;
    private SlideShow poiSlideShow;
    private int maxFontSize;
    private Map<Integer, Integer> slideMasterIndexs;
    private Map<Integer, Integer> titleMasterIndexs;
    private int offset;
    private boolean tableShape;
    private HeadersFooters poiHeadersFooters;
    private boolean hasProcessedMasterSlideNumber;
    private boolean hasProcessedMasterFooter;
    private boolean hasProcessedMasterDateTime;

    public PPTReader(IControl control, String filePath) {
        this(control, filePath, false);
    }

    public PPTReader(IControl control, String filePath, boolean isGetThumbnail) {
        this.filePath = filePath;
        this.control = control;
        this.isGetThumbnail = isGetThumbnail;
    }

    public Object getModel() throws Exception {
        if (model != null) {
            return model;
        }
        poiSlideShow = new SlideShow(new HSLFSlideShow(control, filePath), isGetThumbnail);

        model = new PGModel();

        Dimension d = poiSlideShow.getPageSize();
        d.width = (int) (d.width * KeyKt.POINT_TO_PIXEL);
        d.height = (int) (d.height * KeyKt.POINT_TO_PIXEL);
        model.setPageSize(d);

        DocumentAtom docAtom = poiSlideShow.getDocumentRecord().getDocumentAtom();
        if (docAtom != null) {
            model.setSlideNumberOffset(docAtom.getFirstSlideNum() - 1);
            model.setOmitTitleSlide(docAtom.getOmitTitlePlace());
        }

        int count = poiSlideShow.getSlideCount();
        model.setSlideCount(count);
        if (count == 0) {

            throw new Exception("Format error");
        } else {
            poiHeadersFooters = poiSlideShow.getSlideHeadersFooters();
            int len = Math.min(count, FIRST_READ_SLIDE_NUM);
            for (int i = 0; i < len && !abortReader; i++) {
                processSlide(poiSlideShow.getSlide(currentReaderIndex++));
            }
            if (!isReaderFinish() && !isGetThumbnail) {
                new BackReaderThread(this, control).start();
            }
        }

        return model;
    }

    public boolean isReaderFinish() {
        if (model != null && poiSlideShow != null) {
            return abortReader || model.getSlideCount() == 0 || currentReaderIndex >= poiSlideShow.getSlideCount();
        }
        return true;
    }

    public void backReader() throws Exception {
        processSlide(poiSlideShow.getSlide(currentReaderIndex++));

        if (!isGetThumbnail) {
            control.actionEvent(EventConstant.APP_COUNT_PAGES_CHANGE_ID, null);
        }
    }

    private boolean isTitleSlide(Slide slide) {
        int geometry = 0;
        SlideAtom sa = slide.getSlideRecord().getSlideAtom();
        if (sa != null && sa.getSSlideLayoutAtom() != null) {
            geometry = sa.getSSlideLayoutAtom().getGeometryType();
        }

        if (geometry == SSlideLayoutAtom.TITLE_SLIDE) {
            return true;
        } else if (geometry == SSlideLayoutAtom.BLANK_SLIDE) {
            Shape[] shapes = slide.getShapes();
            for (Shape shape : shapes) {
                if (!(shape instanceof TextShape)) {
                    return false;
                }

                OEPlaceholderAtom placeHolder = ((TextShape) shape).getPlaceholderAtom();
                if (placeHolder != null) {
                    int placeHolderID = placeHolder.getPlaceholderId();
                    if (placeHolderID != OEPlaceholderAtom.CenteredTitle
                            && placeHolderID != OEPlaceholderAtom.Subtitle
                            && placeHolderID != 0xFFFFFFFF) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    private void resetFlag() {
        hasProcessedMasterDateTime = false;
        hasProcessedMasterFooter = false;
        hasProcessedMasterSlideNumber = false;
    }

    private void processSlide(Slide slide) {
        PGSlide pgSlide = new PGSlide();
        pgSlide.setSlideType(PGSlide.Slide_Normal);

        pgSlide.setSlideNo(number++);

        if (slide.getBackground() != null) {
            pgSlide.setBackgroundAndFill(converFill(pgSlide, slide.getBackground().getFill()));
        }

        processMaster(pgSlide, slide);
        SlideAtom sa = slide.getSlideRecord().getSlideAtom();
        if (sa != null && sa.getSSlideLayoutAtom() != null) {
            pgSlide.setGeometryType(sa.getSSlideLayoutAtom().getGeometryType());
        }

        resetFlag();

        Shape[] shapes = slide.getShapes();
        for (Shape shape : shapes) {
            processShape(pgSlide, null, shape, PGSlide.Slide_Normal);
        }

        if (!model.isOmitTitleSlide() || !isTitleSlide(slide)) {
            TextBox tempShape = null;
            PGSlide masterSlide = model.getSlideMaster(pgSlide.getMasterIndexs()[0]);
            if (masterSlide != null) {
                HeadersFooters slideHeadersFooters = slide.getSlideHeadersFooters();
                if (slideHeadersFooters != null) {
                    pgSlide.setShowMasterHeadersFooters(false);

                    if (slideHeadersFooters.isSlideNumberVisible() && !hasProcessedMasterSlideNumber) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterSlideNumber);
                        if (tempShape != null) {
                            tempShape = processCurrentSlideHeadersFooters(tempShape, String.valueOf(pgSlide.getSlideNo() + model.getSlideNumberOffset()));
                            pgSlide.appendShapes(tempShape);
                        }
                    }

                    if (!hasProcessedMasterFooter && slideHeadersFooters.isFooterVisible() && slideHeadersFooters.getFooterText() != null) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterFooter);
                        if (tempShape != null) {
                            tempShape = processCurrentSlideHeadersFooters(tempShape, slideHeadersFooters.getFooterText());
                            pgSlide.appendShapes(tempShape);
                        }
                    }

                    if (!hasProcessedMasterDateTime && slideHeadersFooters.isUserDateVisible() && slideHeadersFooters.getDateTimeText() != null) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterDate);
                        if (tempShape != null) {
                            tempShape = processCurrentSlideHeadersFooters(tempShape, slideHeadersFooters.getDateTimeText());
                            pgSlide.appendShapes(tempShape);
                        }
                    } else if (!hasProcessedMasterDateTime && slideHeadersFooters.isDateTimeVisible()) {

                        String val = NumericFormatter.instance().getFormatContents("yyyy/m/d", new Date(System.currentTimeMillis()));

                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterDate);
                        if (tempShape != null && tempShape.getElement() != null) {
                            tempShape = processCurrentSlideHeadersFooters(tempShape, val);
                            pgSlide.appendShapes(tempShape);
                        }
                    }
                } else {
                    if (!hasProcessedMasterSlideNumber && poiHeadersFooters.isSlideNumberVisible()) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterSlideNumber);
                        if (tempShape != null) {
                            tempShape = processCurrentSlideHeadersFooters(tempShape, String.valueOf(pgSlide.getSlideNo() + model.getSlideNumberOffset()));
                            pgSlide.appendShapes(tempShape);
                        }
                    }

                    if (!hasProcessedMasterFooter && poiHeadersFooters.isFooterVisible() && poiHeadersFooters.getFooterText() != null) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterFooter);
                        if (tempShape != null) {
                            pgSlide.appendShapes(tempShape);
                        }
                    }

                    if (!hasProcessedMasterDateTime
                            && ((poiHeadersFooters.getDateTimeText() != null && poiHeadersFooters.isUserDateVisible())
                            || poiHeadersFooters.isDateTimeVisible())) {
                        tempShape = (TextBox) masterSlide.getTextboxByPlaceHolderID(OEPlaceholderAtom.MasterDate);

                        if (tempShape != null) {
                            pgSlide.appendShapes(tempShape);
                        }
                    }
                }
            }
        }

        processNotes(pgSlide, slide.getNotesSheet());

        processGroupShape(pgSlide);

        SlideShowSlideInfoAtom slideInfotAtom = slide.getSlideShowSlideInfoAtom();
        pgSlide.setTransition(slideInfotAtom != null && slideInfotAtom.isValidateTransition());

        processSlideshow(pgSlide, slide.getSlideProgTagsContainer());

        model.appendSlide(pgSlide);

        if (abortReader || model.getSlideCount() == 0 || currentReaderIndex >= poiSlideShow.getSlideCount()) {
            slideMasterIndexs.clear();
            slideMasterIndexs = null;
            titleMasterIndexs.clear();
            titleMasterIndexs = null;
        }
    }

    private TextBox processCurrentSlideHeadersFooters(TextBox styleShape, String text) {
        if (styleShape != null && text != null && text.length() > 0) {
            if (styleShape.getElement() != null && styleShape.getElement().getEndOffset() - styleShape.getElement().getStartOffset() > 0) {
                TextBox textShape = new TextBox();
                textShape.setBounds(styleShape.getBounds());
                textShape.setWrapLine(styleShape.isWrapLine());

                SectionElement secElem = new SectionElement();
                secElem.setStartOffset(0);
                secElem.setEndOffset(text.length());
                secElem.setAttribute(styleShape.getElement().getAttribute().clone());
                textShape.setElement(secElem);

                ParagraphElement paraElem = (ParagraphElement) styleShape.getElement().getParaCollection().getElementForIndex(0);
                ParagraphElement paraElemNew = new ParagraphElement();
                paraElemNew.setStartOffset(0);
                paraElemNew.setEndOffset(text.length());
                paraElemNew.setAttribute(paraElem.getAttribute().clone());
                secElem.appendParagraph(paraElemNew, WPModelConstant.MAIN);

                LeafElement leafElem = (LeafElement) paraElem.getElementForIndex(0);
                String str = leafElem.getText(null);
                if (str != null && str.contains("*")) {
                    text = str.replace("*", text);
                }
                LeafElement leafElemNew = new LeafElement(text);
                leafElemNew.setStartOffset(0);
                leafElemNew.setEndOffset(text.length());
                leafElemNew.setAttribute(leafElem.getAttribute().clone());
                paraElemNew.appendLeaf(leafElemNew);

                return textShape;
            }
        }

        return null;
    }

    private void processGroupShape(PGSlide pgSlide) {
        Map<Integer, List<Integer>> grpShape = pgSlide.getGroupShape();
        if (grpShape == null) {
            return;
        }

        int count = pgSlide.getShapeCount();
        int grpSpID;
        for (int i = 0; i < count; i++) {
            IShape shape = pgSlide.getShape(i);
            grpSpID = getGroupShapeID(shape.getShapeID(), grpShape);
            shape.setGroupShapeID(grpSpID);
        }
    }

    private int getGroupShapeID(int shapeID, Map<Integer, List<Integer>> grpShape) {
        Iterator<Integer> grpIDIter = grpShape.keySet().iterator();
        while (grpIDIter.hasNext()) {
            int grpID = grpIDIter.next();
            List<Integer> childShape = grpShape.get(grpID);
            if (childShape != null && childShape.contains(shapeID)) {
                return grpID;
            }
        }

        return -1;
    }

    private void processSlideshow(PGSlide pgSlide, SlideProgTagsContainer propTagsContainer) {
        try {
            if (propTagsContainer == null) {
                return;
            }
            Record[] records = propTagsContainer.getChildRecords();
            if (records == null || records.length < 1
                    || !(records[0] instanceof SlideProgBinaryTagContainer)) {
                return;
            }

            Record rec = ((SlideProgBinaryTagContainer) records[0]).findFirstOfType(BinaryTagDataBlob.RECORD_ID);
            if (rec == null) {
                return;
            }
            rec = ((BinaryTagDataBlob) rec).findFirstOfType(TimeNodeContainer.RECORD_ID);
            if (rec == null) {
                return;
            }

            rec = ((TimeNodeContainer) rec).findFirstOfType(TimeNodeContainer.RECORD_ID);
            if (rec == null) {
                return;
            }

            records = rec.getChildRecords();
            if (records != null) {
                for (Record record : records) {
                    if (record instanceof TimeNodeContainer) {
                        List<ShapeAnimation> animations = processAnimation(pgSlide, (TimeNodeContainer) record);
                        if (animations != null) {
                            for (ShapeAnimation anim : animations) {
                                pgSlide.addShapeAnimation(anim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            control.getSysKit().getErrorKit().writerLog(e);
        }

    }

    private List<ShapeAnimation> processAnimation(PGSlide pgSlide, TimeNodeContainer timeNodeContainer) {
        try {
            List<ShapeAnimation> animations = new ArrayList<ShapeAnimation>();
            Record[] childRecords = timeNodeContainer.getChildRecords();
            if (childRecords == null) {
                return null;
            }

            List<TimeNodeContainer> timeNodeContainerList = new ArrayList<TimeNodeContainer>();
            for (int i = 0; i < childRecords.length; i++) {
                if (childRecords[i] instanceof TimeNodeContainer) {
                    timeNodeContainerList.add((TimeNodeContainer) childRecords[i]);
                }
            }

            ShapeAnimation shapeAnim;
            if (timeNodeContainerList.size() > 1) {

                for (TimeNodeContainer container : timeNodeContainerList) {
                    Record record = container.findFirstOfType(TimeNodeContainer.RECORD_ID);
                    if (record != null) {
                        shapeAnim = processSingleAnimation(pgSlide, (TimeNodeContainer) record);
                        if (shapeAnim != null) {
                            animations.add(shapeAnim);
                        }
                    }
                }
            } else if (timeNodeContainerList.size() == 1) {
                timeNodeContainer = timeNodeContainerList.get(0);
                timeNodeContainerList.clear();
                childRecords = timeNodeContainer.getChildRecords();
                for (int i = 0; i < childRecords.length; i++) {
                    if (childRecords[i] instanceof TimeNodeContainer) {
                        timeNodeContainerList.add((TimeNodeContainer) childRecords[i]);
                    }
                }

                if (timeNodeContainerList.size() == 1) {

                    shapeAnim = processSingleAnimation(pgSlide, timeNodeContainerList.get(0));
                    if (shapeAnim != null) {
                        animations.add(shapeAnim);
                    }
                } else if (timeNodeContainerList.size() > 1) {

                    for (TimeNodeContainer container : timeNodeContainerList) {
                        shapeAnim = processSingleAnimation(pgSlide, container);
                        if (shapeAnim != null) {
                            animations.add(shapeAnim);
                        }
                    }
                }
            }

            return animations;
        } catch (Exception e) {
            return null;
        }

    }

    private ShapeAnimation processSingleAnimation(PGSlide pgSlide, TimeNodeContainer timeNodeContainer) {
        try {

            byte type = -1;
            TimeNodeAttributeContainer timeNodeAttrContainer = (TimeNodeAttributeContainer) timeNodeContainer.findFirstOfType(TimeNodeAttributeContainer.RECORD_ID);
            Record[] records = timeNodeAttrContainer.getChildRecords();
            for (Record record : records) {
                if (record instanceof TimeVariant) {
                    if (((TimeVariant) record).getAttributeType() == TimeVariant.TPID_EffectType) {
                        int t = (Integer) ((TimeVariant) record).getValue();
                        switch (t) {
                            case TimeVariant.TimeEffectType__Entrance:
                                type = ShapeAnimation.SA_ENTR;
                                break;
                            case TimeVariant.TimeEffectType__Exit:
                                type = ShapeAnimation.SA_EXIT;
                                break;
                            case TimeVariant.TimeEffectType__Emphasis:
                                type = ShapeAnimation.SA_EMPH;
                                break;
                            default:
                                return null;
                        }
                        break;
                    }
                }
            }

            timeNodeContainer = (TimeNodeContainer) timeNodeContainer.findFirstOfType(TimeNodeContainer.RECORD_ID);

            records = timeNodeContainer.getChildRecords();
            for (Record record : records) {
                if (record.getRecordType() == TimeAnimateBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeColorBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeEffectBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeMotionBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeRotationBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeScaleBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeSetBehaviorContainer.RECORD_ID
                        || record.getRecordType() == TimeCommandBehaviorContainer.RECORD_ID) {
                    TimeBehaviorContainer behaviorContainer = (TimeBehaviorContainer) ((PositionDependentRecordContainer) record).findFirstOfType(TimeBehaviorContainer.RECORD_ID);
                    ClientVisualElementContainer clientVisualElement = (ClientVisualElementContainer) behaviorContainer.findFirstOfType(ClientVisualElementContainer.RECORD_ID);
                    VisualShapeAtom visualElementAtom = (VisualShapeAtom) clientVisualElement.findFirstOfType(VisualShapeAtom.RECORD_ID);

                    switch (visualElementAtom.getTargetElementType()) {
                        case VisualShapeAtom.TVET_Shape:
                            return (new ShapeAnimation(visualElementAtom.getTargetElementID(),
                                    type, ShapeAnimation.Para_All, ShapeAnimation.Para_All));

                        case VisualShapeAtom.TVET_ShapeOnly:
                            return (new ShapeAnimation(visualElementAtom.getTargetElementID(),
                                    type, ShapeAnimation.Para_BG, ShapeAnimation.Para_BG));

                        case VisualShapeAtom.TVET_TextRange:
                            int paraID = getParaIndex(pgSlide, visualElementAtom);
                            return (new ShapeAnimation(visualElementAtom.getTargetElementID(),
                                    type, paraID, paraID));

                    }
                    break;
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private int getParaIndex(PGSlide pgSlide, VisualShapeAtom visualElementAtom) {
        IShape[] shapes = pgSlide.getShapes();
        int cnt = shapes.length;

        ParagraphElement element = null;
        for (int i = 0; i < cnt; i++) {
            if ((shapes[i] instanceof TextBox)
                    && shapes[i].getShapeID() == visualElementAtom.getTargetElementID()) {
                long offset = 0;
                int paraID = 0;
                SectionElement sec = ((TextBox) shapes[i]).getElement();
                element = (ParagraphElement) sec.getElement(offset);
                while (element != null) {
                    offset = element.getEndOffset();
                    if (element.getStartOffset() == visualElementAtom.getData1() &&
                            (offset == visualElementAtom.getData2()
                                    || offset == visualElementAtom.getData2() - 1)) {
                        return paraID;
                    }

                    paraID++;
                    element = (ParagraphElement) sec.getElement(offset);
                }

                break;
            }
        }

        return ShapeAnimation.Para_All;
    }

    public void processMaster(PGSlide pgSlide, Slide slide) {
        if (slideMasterIndexs == null) {
            slideMasterIndexs = new HashMap<Integer, Integer>();
        }
        if (titleMasterIndexs == null) {
            titleMasterIndexs = new HashMap<Integer, Integer>();
        }

        MasterSheet sheet = null;
        SlideAtom sa = slide.getSlideRecord().getSlideAtom();
        if (!sa.getFollowMasterObjects()) {
            return;
        }
        int masterId = sa.getMasterID();

        SlideMaster[] master = poiSlideShow.getSlidesMasters();
        for (int i = 0; i < master.length; i++) {
            if (masterId == master[i]._getSheetNumber()) {
                Integer index = slideMasterIndexs.get(masterId);
                if (index != null) {
                    pgSlide.setMasterSlideIndex(index);
                    return;
                } else {
                    PGSlide slideMaster = new PGSlide();
                    slideMaster.setSlideType(PGSlide.Slide_Master);
                    slideMaster.setBackgroundAndFill(pgSlide.getBackgroundAndFill());

                    Shape[] sh = master[i].getShapes();
                    for (int j = 0; j < sh.length; j++) {
                        processShape(slideMaster, null, sh[j], PGSlide.Slide_Master);
                    }
                    if (slideMaster.getShapeCount() > 0) {
                        index = model.appendSlideMaster(slideMaster);
                        pgSlide.setMasterSlideIndex(index);
                        slideMasterIndexs.put(masterId, index);
                    }
                }
                break;
            }
        }
        if (sheet == null) {
            TitleMaster[] titleMaster = poiSlideShow.getTitleMasters();
            if (titleMaster != null) {
                for (int i = 0; i < titleMaster.length; i++) {
                    if (masterId == titleMaster[i]._getSheetNumber()) {
                        Integer index = titleMasterIndexs.get(masterId);
                        if (index != null) {
                            pgSlide.setLayoutSlideIndex(index);
                        } else {
                            PGSlide slideMaster = new PGSlide();
                            slideMaster.setSlideType(PGSlide.Slide_Master);
                            slideMaster.setBackgroundAndFill(pgSlide.getBackgroundAndFill());

                            Shape[] sh = titleMaster[i].getShapes();
                            for (int j = 0; j < sh.length; j++) {
                                processShape(slideMaster, null, sh[j], PGSlide.Slide_Master);
                            }
                            if (slideMaster.getShapeCount() > 0) {
                                index = model.appendSlideMaster(slideMaster);
                                pgSlide.setLayoutSlideIndex(index);
                                titleMasterIndexs.put(masterId, index);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line getShapeLine(SimpleShape shape) {
        return getShapeLine(shape, false);
    }

    private com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line getShapeLine(SimpleShape shape, boolean isTableCellLine) {
        com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line line = null;
        if (shape != null && shape.hasLine()) {
            int lineWidth = (int) Math.round(shape.getLineWidth() * KeyKt.POINT_TO_PIXEL);
            boolean dash = shape.getLineDashing() > AutoShapeConstant.LINESTYLE_SOLID;
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color color = shape.getLineColor();

            if (color != null) {
                line = new com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line();
                BackgroundAndFill lineFill = new BackgroundAndFill();
                lineFill.setForegroundColor(converterColor(color));

                line.setBackgroundAndFill(lineFill);
                line.setDash(dash);
                line.setLineWidth(lineWidth);
            }
        } else if (isTableCellLine) {
            line = new com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line();
            BackgroundAndFill lineFill = new BackgroundAndFill();
            lineFill.setForegroundColor(0xFF000000);

            line.setBackgroundAndFill(lineFill);
        }

        return line;
    }

    private BackgroundAndFill converFill(PGSlide pgSlide, Fill fill) {
        BackgroundAndFill bgFill = null;
        if (fill != null) {
            int type = fill.getFillType();

            if (type == BackgroundAndFill.FILL_BACKGROUND) {
                bgFill = pgSlide.getBackgroundAndFill();
            } else if (type == BackgroundAndFill.FILL_SOLID) {
                if (fill.getForegroundColor() != null) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);

                    bgFill.setForegroundColor(converterColor(fill.getForegroundColor()));
                }
            } else if (type == BackgroundAndFill.FILL_SHADE_LINEAR
                    || type == BackgroundAndFill.FILL_SHADE_RADIAL
                    || type == BackgroundAndFill.FILL_SHADE_RECT
                    || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                int angle = fill.getFillAngle();
                switch (angle) {
                    case -90:
                    case 0:
                        angle += 90;
                        break;
                    case -45:
                        angle = 135;
                        break;
                    case -135:
                        angle = 45;
                        break;
                }
                int focus = fill.getFillFocus();
                com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color fillColor = fill.getForegroundColor();
                com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color fillbackColor = fill.getFillbackColor();

                int[] colors = null;
                float[] positions = null;
                if (fill.isShaderPreset()) {
                    colors = fill.getShaderColors();
                    positions = fill.getShaderPositions();
                }

                if (colors == null) {
                    colors = new int[]{fillColor == null ? 0xFFFFFFFF : fillColor.getRGB(),
                            fillbackColor == null ? 0xFFFFFFFF : fillbackColor.getRGB()};
                }
                if (positions == null) {
                    positions = new float[]{0f, 1f};
                }

                Gradient gradient = null;
                if (type == BackgroundAndFill.FILL_SHADE_LINEAR) {
                    gradient = new LinearGradientShader(angle, colors, positions);
                } else if (type == BackgroundAndFill.FILL_SHADE_RADIAL
                        || type == BackgroundAndFill.FILL_SHADE_RECT
                        || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                    gradient =
                            new RadialGradientShader(fill.getRadialGradientPositionType(), colors, positions);
                }

                if (gradient != null) {
                    gradient.setFocus(focus);
                }

                bgFill = new BackgroundAndFill();
                bgFill.setFillType((byte) type);
                bgFill.setShader(gradient);
            } else if (type == BackgroundAndFill.FILL_SHADE_TILE) {
                bgFill = new BackgroundAndFill();
                bgFill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);

                PictureData pData = fill.getPictureData();
                if (pData != null) {

                    int index = control.getSysKit().getPictureManage().addPicture(pData);
                    bgFill.setShader(
                            new TileShader(control.getSysKit().getPictureManage().getPicture(index),
                                    TileShader.Flip_None, 1f, 1.0f));
                }

            } else if (type == BackgroundAndFill.FILL_PICTURE) {

                PictureData pData = fill.getPictureData();
                if (pData != null) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_PICTURE);

                    bgFill.setPictureIndex(control.getSysKit().getPictureManage().addPicture(pData));
                }
            } else if (type == BackgroundAndFill.FILL_PATTERN) {
                if (fill.getFillbackColor() != null) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);

                    bgFill.setForegroundColor(converterColor(fill.getFillbackColor()));
                }
            }
        }

        return bgFill;
    }

    private void processNotes(PGSlide pgSlide, Notes notes) {
        if (notes != null) {
            String note = "";
            for (Shape shape : notes.getShapes()) {
                if (abortReader) {
                    break;
                }
                if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.AutoShape
                        || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextBox) {
                    OEPlaceholderAtom phAtom = ((TextShape) shape).getPlaceholderAtom();
                    if (phAtom != null && phAtom.getPlaceholderId() == OEPlaceholderAtom.NotesBody) {
                        String text = ((TextShape) shape).getText();
                        if (text != null && text.length() > 0) {
                            note += text;
                            note += '\n';
                        }
                    }
                }
            }
            if (note.trim().length() > 0) {
                PGNotes pgNotes = new PGNotes(note.trim());
                pgSlide.setNotes(pgNotes);
            }
        }
    }

    private void processShape(PGSlide pgSlide, GroupShape parent, Shape shape, int slideType) {
        boolean addShape = true;
        int placeHolderID = -1;
        tableShape = false;
        if (abortReader || shape.isHidden()) {
            return;
        }

        Rectangle2D rect2D = null;
        if (shape instanceof ShapeGroup) {
            rect2D = ((ShapeGroup) shape).getClientAnchor2D(shape);
        } else {
            rect2D = shape.getLogicalAnchor2D();
        }
        if (rect2D == null) {
            return;
        }

        Rectangle rect = new Rectangle();
        rect.x = (int) (rect2D.getX() * KeyKt.POINT_TO_PIXEL);
        rect.y = (int) (rect2D.getY() * KeyKt.POINT_TO_PIXEL);
        rect.width = (int) (rect2D.getWidth() * KeyKt.POINT_TO_PIXEL);
        rect.height = (int) (rect2D.getHeight() * KeyKt.POINT_TO_PIXEL);

        BackgroundAndFill fill = null;
        com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line line = null;
        if (shape instanceof SimpleShape) {
            IShape masterShape = null;
            if (slideType == PGSlide.Slide_Normal) {

                int masterShapeID = shape.getMasterShapeID();

                int[] indexs = pgSlide.getMasterIndexs();
                PGSlide master = model.getSlideMaster(indexs[0]);
                if (master != null) {
                    int count = master.getShapeCount();
                    for (int i = 0; i < count; i++) {
                        IShape item = master.getShape(i);
                        if (item.getShapeID() == masterShapeID) {
                            masterShape = item;
                            break;
                        }
                    }
                }
            }

            fill = converFill(pgSlide, shape.getFill());
            if (fill == null && masterShape != null && masterShape instanceof AbstractShape) {
                fill = ((AbstractShape) masterShape).getBackgroundAndFill();
            }

            line = getShapeLine((SimpleShape) shape);
            if (line == null && masterShape != null && masterShape instanceof AbstractShape) {
                line = ((AbstractShape) masterShape).getLine();
            }
        }

        if (shape instanceof Line
                || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Freeform
                || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.AutoShape
                || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextBox
                || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture) {
            if (shape instanceof Line) {
                if (line != null) {
                    LineShape lineShape = new LineShape();
                    lineShape.setShapeType(shape.getShapeType());
                    lineShape.setBounds(rect);
                    lineShape.setBackgroundAndFill(fill);

                    lineShape.setLine(line);

                    Float[] adj = shape.getAdjustmentValue();
                    if (lineShape.getShapeType() == ShapeTypes.BentConnector2 && adj == null) {
                        lineShape.setAdjustData(new Float[]{1.0f});
                    } else {
                        lineShape.setAdjustData(adj);
                    }

                    int type = shape.getStartArrowType();
                    if (type > 0) {
                        lineShape.createStartArrow((byte) type,
                                shape.getStartArrowWidth(),
                                shape.getStartArrowLength());
                    }

                    type = shape.getEndArrowType();
                    if (type > 0) {
                        lineShape.createEndArrow((byte) type,
                                shape.getEndArrowWidth(),
                                shape.getEndArrowLength());
                    }
                    processGrpRotation(shape, lineShape);

                    lineShape.setShapeID(shape.getShapeId());
                    if (parent == null) {
                        pgSlide.appendShapes(lineShape);
                    } else {
                        parent.appendShapes(lineShape);
                    }
                }
            } else if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Freeform) {
                if (fill != null || line != null) {
                    ArbitraryPolygonShape arbitraryPolygonShape = new ArbitraryPolygonShape();
                    arbitraryPolygonShape.setShapeType(ShapeTypes.ArbitraryPolygon);
                    arbitraryPolygonShape.setBounds(rect);

                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;

                    int startArrowType = shape.getStartArrowType();
                    if (startArrowType > 0) {
                        ArrowPathAndTail arrowPathAndTail = ((com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Freeform) shape).getStartArrowPathAndTail(rect);
                        if (arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null) {
                            startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(arrowPathAndTail.getArrowPath());
                            pathExtend.setArrowFlag(true);
                            if (startArrowType != Arrow.Arrow_Arrow) {
                                if (line == null || line.getBackgroundAndFill() == null) {
                                    com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color color = shape.getLineColor();
                                    if (color != null) {
                                        BackgroundAndFill arrowFill = new BackgroundAndFill();
                                        arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                        arrowFill.setForegroundColor(converterColor(color));
                                        pathExtend.setBackgroundAndFill(arrowFill);
                                    }
                                } else {
                                    pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                }
                            } else {
                                pathExtend.setLine(line);
                            }

                            arbitraryPolygonShape.appendPath(pathExtend);
                        }
                    }

                    int endArrowType = shape.getEndArrowType();
                    if (endArrowType > 0) {
                        ArrowPathAndTail arrowPathAndTail = ((com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Freeform) shape).getEndArrowPathAndTail(rect);
                        if (arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null) {
                            endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(arrowPathAndTail.getArrowPath());

                            pathExtend.setArrowFlag(true);
                            if (endArrowType != Arrow.Arrow_Arrow) {
                                if (line == null || line.getBackgroundAndFill() == null) {
                                    com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color color = shape.getLineColor();
                                    if (color != null) {
                                        BackgroundAndFill arrowFill = new BackgroundAndFill();
                                        arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                        arrowFill.setForegroundColor(converterColor(color));
                                        pathExtend.setBackgroundAndFill(arrowFill);
                                    }
                                } else {
                                    pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                }
                            } else {
                                pathExtend.setLine(line);
                            }

                            arbitraryPolygonShape.appendPath(pathExtend);
                        }
                    }

                    Path[] paths = ((com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Freeform) shape).getFreeformPath(rect,
                            startArrowTailCenter, (byte) startArrowType, endArrowTailCenter, (byte) endArrowType);
                    for (int i = 0; paths != null && i < paths.length; i++) {
                        ExtendPath pathExtend = new ExtendPath();
                        pathExtend.setPath(paths[i]);
                        if (line != null) {
                            pathExtend.setLine(line);
                        }
                        if (fill != null) {
                            pathExtend.setBackgroundAndFill(fill);
                        }
                        arbitraryPolygonShape.appendPath(pathExtend);
                    }

                    processGrpRotation(shape, arbitraryPolygonShape);
                    arbitraryPolygonShape.setShapeID(shape.getShapeId());
                    if (parent == null) {
                        pgSlide.appendShapes(arbitraryPolygonShape);
                    } else {
                        parent.appendShapes(arbitraryPolygonShape);
                    }
                }
            } else if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.AutoShape
                    || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextBox) {

                placeHolderID = ((TextShape) shape).getPlaceholderId();

                AutoShape autoShape = null;
                if (fill != null || line != null) {
                    int shapeType = shape.getShapeType();
                    if (shapeType == ShapeTypes.Line
                            || shapeType == ShapeTypes.StraightConnector1
                            || shapeType == ShapeTypes.BentConnector2
                            || shapeType == ShapeTypes.BentConnector3
                            || shapeType == ShapeTypes.BentConnector4
                            || shapeType == ShapeTypes.BentConnector5
                            || shapeType == ShapeTypes.CurvedConnector2
                            || shapeType == ShapeTypes.CurvedConnector3
                            || shapeType == ShapeTypes.CurvedConnector4
                            || shapeType == ShapeTypes.CurvedConnector5) {
                        LineShape lineShape = new LineShape();
                        lineShape.setShapeType(shape.getShapeType());
                        lineShape.setBounds(rect);
                        lineShape.setLine(line);

                        Float[] adj = shape.getAdjustmentValue();
                        if (lineShape.getShapeType() == ShapeTypes.BentConnector2 && adj == null) {
                            lineShape.setAdjustData(new Float[]{1.0f});
                        } else {
                            lineShape.setAdjustData(adj);
                        }

                        int type = shape.getStartArrowType();
                        if (type > 0) {
                            lineShape.createStartArrow((byte) type,
                                    shape.getStartArrowWidth(),
                                    shape.getStartArrowLength());
                        }

                        type = shape.getEndArrowType();
                        if (type > 0) {
                            lineShape.createEndArrow((byte) type,
                                    shape.getEndArrowWidth(),
                                    shape.getEndArrowLength());
                        }

                        autoShape = lineShape;
                    } else {
                        autoShape = new AutoShape(shape.getShapeType());
                        autoShape.setAuotShape07(false);
                        autoShape.setBounds(rect);
                        autoShape.setBackgroundAndFill(fill);

                        if (line != null) {
                            autoShape.setLine(line);
                        }
                        if (shape.getShapeType() != ShapeTypes.TextBox) {
                            autoShape.setAdjustData(shape.getAdjustmentValue());
                        }
                    }

                    processGrpRotation(shape, autoShape);

                    autoShape.setShapeID(shape.getShapeId());
                    autoShape.setPlaceHolderID(placeHolderID);
                    if (parent == null) {
                        pgSlide.appendShapes(autoShape);
                    } else {
                        parent.appendShapes(autoShape);
                    }
                }

                TextBox tb = new TextBox();
                byte mcType = ((TextShape) shape).getMetaCharactersType();
                tb.setMCType(mcType);

                processTextShape(tb, (TextShape) shape, rect, slideType, placeHolderID);
                if (tb.getElement() != null) {
                    if (tb.isWordArt() && autoShape != null) {

                        autoShape.setBackgroundAndFill(null);
                    }
                    processGrpRotation(shape, tb);
                    tb.setShapeID(shape.getShapeId());
                    tb.setPlaceHolderID(placeHolderID);

                    if (slideType == PGSlide.Slide_Normal) {
                        if (placeHolderID == OEPlaceholderAtom.MasterFooter) {
                            hasProcessedMasterFooter = true;
                        } else if (placeHolderID == OEPlaceholderAtom.MasterDate
                                && (mcType == TextBox.MC_DateTime || mcType == TextBox.MC_GenericDate || mcType == TextBox.MC_RTFDateTime)) {
                            hasProcessedMasterDateTime = true;
                        } else if (placeHolderID == OEPlaceholderAtom.MasterSlideNumber && mcType == TextBox.MC_SlideNumber) {
                            hasProcessedMasterSlideNumber = true;
                        }
                    }

                    if (parent == null || (slideType == PGSlide.Slide_Master && MasterSheet.isPlaceholder(shape))) {
                        pgSlide.appendShapes(tb);
                    } else {
                        parent.appendShapes(tb);
                    }
                }
            } else if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture) {
                com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture poiPic = (com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture) shape;
                PictureData pData = poiPic.getPictureData();
                if (pData != null) {
                    PictureShape pictureShape = new PictureShape();
                    pictureShape.setPictureIndex(control.getSysKit().getPictureManage().addPicture(pData));
                    pictureShape.setBounds(rect);
                    processGrpRotation(shape, pictureShape);
                    pictureShape.setShapeID(shape.getShapeId());
                    pictureShape.setPictureEffectInfor(PictureEffectInfoFactory.getPictureEffectInfor(poiPic.getEscherOptRecord()));

                    pictureShape.setBackgroundAndFill(fill);
                    pictureShape.setLine(line);

                    if (parent == null) {
                        pgSlide.appendShapes(pictureShape);
                    } else {
                        parent.appendShapes(pictureShape);
                    }
                } else if (fill != null || line != null) {

                    AutoShape autoShape = new AutoShape(ShapeTypes.Rectangle);
                    autoShape.setAuotShape07(false);
                    autoShape.setBounds(rect);
                    autoShape.setBackgroundAndFill(fill);
                    autoShape.setLine(line);
                    if (parent == null) {
                        pgSlide.appendShapes(autoShape);
                    } else {
                        parent.appendShapes(autoShape);
                    }
                }
            }
        } else if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Table) {
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Table poiTable = (com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Table) shape;
            if (poiTable != null) {
                processTable(pgSlide, poiTable, parent, slideType);
            }
        } else if (shape instanceof ShapeGroup) {
            ShapeGroup shapeGroup = (ShapeGroup) shape;
            GroupShape groupShape = new GroupShape();
            groupShape.setBounds(rect);
            groupShape.setShapeID(shape.getShapeId());
            groupShape.setFlipHorizontal(shapeGroup.getFlipHorizontal());
            groupShape.setFlipVertical(shapeGroup.getFlipVertical());
            groupShape.setParent(parent);
            processGrpRotation(shape, groupShape);

            Shape[] sh = ((ShapeGroup) shape).getShapes();
            List<Integer> childShapeLst = new ArrayList<Integer>(sh.length);
            for (int i = 0; i < sh.length; i++) {
                processShape(pgSlide, groupShape, sh[i], slideType);
                childShapeLst.add(sh[i].getShapeId());
            }
            if (parent == null) {
                pgSlide.appendShapes(groupShape);
            } else {
                parent.appendShapes(groupShape);
            }
            pgSlide.addGroupShape(shape.getShapeId(), childShapeLst);
        }
    }

    private void processTable(PGSlide pgSlide, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Table poiTable, GroupShape parent, int slideType) {
        Rectangle2D clientAnchor = poiTable.getClientAnchor2D(poiTable);
        Rectangle2D spgrAnchor = poiTable.getCoordinates();
        tableShape = true;
        int rows = poiTable.getNumberOfRows();
        int columns = poiTable.getNumberOfColumns();
        TableShape table = new TableShape(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (abortReader) {
                    return;
                }
                com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TableCell poiCell = poiTable.getCell(i, j);
                if (poiCell != null) {
                    Rectangle2D anchor = poiCell.getLogicalAnchor2D();
                    if (anchor != null) {
                        double scalex = spgrAnchor.getWidth() / clientAnchor.getWidth();
                        double scaley = spgrAnchor.getHeight() / clientAnchor.getHeight();

                        double x = clientAnchor.getX() + (anchor.getX() - spgrAnchor.getX()) / scalex;
                        double y = clientAnchor.getY() + (anchor.getY() - spgrAnchor.getY()) / scaley;
                        double width = anchor.getWidth() / scalex;
                        double height = anchor.getHeight() / scaley;

                        Rectanglef rect = new Rectanglef();
                        rect.setX((float) (x * KeyKt.POINT_TO_PIXEL));
                        rect.setY((float) (y * KeyKt.POINT_TO_PIXEL));
                        rect.setWidth((float) (width * KeyKt.POINT_TO_PIXEL));
                        rect.setHeight((float) (height * KeyKt.POINT_TO_PIXEL));

                        TableCell cell = new TableCell();

                        cell.setBounds(rect);

                        cell.setLeftLine(getShapeLine(poiCell.getBorderLeft(), true));
                        cell.setRightLine(getShapeLine(poiCell.getBorderRight(), true));
                        cell.setTopLine(getShapeLine(poiCell.getBorderTop(), true));
                        cell.setBottomLine(getShapeLine(poiCell.getBorderBottom(), true));

                        cell.setBackgroundAndFill(converFill(pgSlide, poiCell.getFill()));

                        String text = poiCell.getText();
                        if (text != null && text.trim().length() > 0) {
                            TextBox textBox = new TextBox();
                            Rectangle r = new Rectangle((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                            processTextShape(textBox, poiCell, r, slideType, -1);
                            if (textBox.getElement() != null) {
                                processGrpRotation(poiCell, textBox);
                                cell.setText(textBox);
                            }
                        }
                        table.addCell(i * columns + j, cell);
                    }
                }
            }
        }

        Line[] borders = poiTable.getTableBorders();
        for (Line line : borders) {
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line aLine = getShapeLine(line, true);
            if (aLine != null) {
                Rectangle2D rect2D = line.getLogicalAnchor2D();
                if (rect2D == null) {
                    return;
                }
                Rectangle rect = new Rectangle();
                rect.x = (int) (rect2D.getX() * KeyKt.POINT_TO_PIXEL);
                rect.y = (int) (rect2D.getY() * KeyKt.POINT_TO_PIXEL);
                rect.width = (int) (rect2D.getWidth() * KeyKt.POINT_TO_PIXEL);
                rect.height = (int) (rect2D.getHeight() * KeyKt.POINT_TO_PIXEL);

                LineShape lineShape = new LineShape();
                lineShape.setShapeType(line.getShapeType());
                lineShape.setBounds(rect);
                lineShape.setLine(aLine);

                Float[] adj = line.getAdjustmentValue();
                if (lineShape.getShapeType() == ShapeTypes.BentConnector2 && adj == null) {
                    lineShape.setAdjustData(new Float[]{1.0f});
                } else {
                    lineShape.setAdjustData(null);
                }

                processGrpRotation(line, lineShape);

                lineShape.setShapeID(line.getShapeId());
                pgSlide.appendShapes(lineShape);
            }
        }

        Rectangle rect = new Rectangle();
        rect.x = (int) (clientAnchor.getX() * KeyKt.POINT_TO_PIXEL);
        rect.y = (int) (clientAnchor.getY() * KeyKt.POINT_TO_PIXEL);
        rect.width = (int) (clientAnchor.getWidth() * KeyKt.POINT_TO_PIXEL);
        rect.height = (int) (clientAnchor.getHeight() * KeyKt.POINT_TO_PIXEL);

        table.setBounds(rect);
        table.setShapeID(poiTable.getShapeId());
        table.setTable07(false);
        if (parent == null) {
            pgSlide.appendShapes(table);
        } else {
            parent.appendShapes(table);
        }
        tableShape = false;
    }

    private int getBorderColor(Line line) {
        if (line != null) {
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color color = line.getLineColor();
            if (color != null) {
                return converterColor(color);
            }
        }
        return Color.BLACK;
    }

    private void processTextShape(TextBox tb, TextShape ts, Rectangle rect, int slideType, int placeHolderID) {
        if (rect == null) {
            Rectangle2D rect2D = ts.getLogicalAnchor2D();
            if (rect2D == null) {
                return;
            }
            rect = new Rectangle();
            rect.x = (int) (rect2D.getX() * KeyKt.POINT_TO_PIXEL);
            rect.y = (int) (rect2D.getY() * KeyKt.POINT_TO_PIXEL);
            rect.width = (int) (rect2D.getWidth() * KeyKt.POINT_TO_PIXEL);
            rect.height = (int) (rect2D.getHeight() * KeyKt.POINT_TO_PIXEL);
        }

        tb.setBounds(rect);

        tb.setWrapLine(ts.getWordWrap() == 0);

        String text = ts.getText();
        if (text != null) {
            processNormalTextShape(tb, ts, rect, slideType, placeHolderID);
        } else {

            text = ts.getUnicodeGeoText();
            if (text != null && text.length() > 0) {
                tb.setWordArt(true);
                processWordArtTextShape(tb, ts, text, rect, slideType, placeHolderID);
            }
        }

    }

    private void processNormalTextShape(TextBox tb, TextShape ts, Rectangle rect, int slideType, int placeHolderID) {
        String text = ts.getText();
        if (text != null && text.trim().length() > 0) {

            SectionElement secElem = new SectionElement();
            tb.setElement(secElem);

            IAttributeSet attr = secElem.getAttribute();

            AttrManage.instance().setPageWidth(attr, (int) (rect.width * KeyKt.PIXEL_TO_TWIPS));

            AttrManage.instance().setPageHeight(attr, (int) (rect.height * KeyKt.PIXEL_TO_TWIPS));

            AttrManage.instance().setPageMarginLeft(attr, (int) (ts.getMarginLeft() * KeyKt.POINT_TO_TWIPS));

            AttrManage.instance().setPageMarginRight(attr, (int) (ts.getMarginRight() * KeyKt.POINT_TO_TWIPS));

            AttrManage.instance().setPageMarginTop(attr, (int) (ts.getMarginTop() * KeyKt.POINT_TO_TWIPS));

            AttrManage.instance().setPageMarginBottom(attr, (int) (ts.getMarginBottom() * KeyKt.POINT_TO_TWIPS));
            byte verAlign = WPAttrConstant.PAGE_V_TOP;

            {
                int align = ts.getVerticalAlignment();
                switch (align) {
                    case TextShape.AnchorTop:
                    case TextShape.AnchorTopBaseline:
                    case TextShape.AnchorTopCentered:
                    case TextShape.AnchorTopCenteredBaseline:
                        verAlign = WPAttrConstant.PAGE_V_TOP;
                        break;

                    case TextShape.AnchorMiddle:
                    case TextShape.AnchorMiddleCentered:
                        verAlign = WPAttrConstant.PAGE_V_CENTER;
                        break;

                    case TextShape.AnchorBottom:
                    case TextShape.AnchorBottomBaseline:
                    case TextShape.AnchorBottomCentered:
                    case TextShape.AnchorBottomCenteredBaseline:
                        verAlign = WPAttrConstant.PAGE_V_BOTTOM;
                        break;

                    default:
                        break;
                }

                if (align == TextShape.AnchorTopCentered
                        || align == TextShape.AnchorTopCenteredBaseline
                        || align == TextShape.AnchorMiddleCentered
                        || align == TextShape.AnchorBottomCentered
                        || align == TextShape.AnchorBottomCenteredBaseline) {
                    AttrManage.instance().setPageHorizontalAlign(attr, WPAttrConstant.PAGE_H_CENTER);
                }
            }
            AttrManage.instance().setPageVerticalAlign(attr, verAlign);

            offset = 0;
            secElem.setStartOffset(offset);
            int len = text.length();
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Hyperlink[] links = ts.getTextRun().getHyperlinks();
            int start = 0;

            if (ts.getTextRun().getRunType() != com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TextHeaderAtom.TITLE_TYPE) {
                for (int i = 0; i < len; i++) {
                    if (abortReader) {
                        break;
                    }
                    if (text.charAt(i) == '\n') {
                        if (i + 1 >= len) {
                            break;
                        }
                        processParagraph(secElem, ts, text, links, start, i + 1, placeHolderID);
                        start = i + 1;
                    }
                }
            }
            processParagraph(secElem, ts, text, links, start, len, placeHolderID);

            secElem.setEndOffset(offset);
            BulletNumberManage.instance().clearData();
        }
    }

    private void processWordArtTextShape(TextBox tb, TextShape ts, String text, Rectangle rect, int slideType, int placeHolderID) {
        if (placeHolderID == OEPlaceholderAtom.MasterFooter && text.contains("*")) {
            if (slideType == PGSlide.Slide_Master) {
                if (poiHeadersFooters.getFooterText() != null) {
                    text = poiHeadersFooters.getFooterText();
                }
            } else if (slideType == PGSlide.Slide_Normal) {
                text = null;

                if (poiHeadersFooters.getFooterText() != null) {
                    text = poiHeadersFooters.getFooterText();
                }
            }
        } else if (placeHolderID == OEPlaceholderAtom.MasterDate && text.contains("*")) {
            if (slideType == PGSlide.Slide_Master) {
                if (poiHeadersFooters.getDateTimeText() != null) {
                    text = poiHeadersFooters.getDateTimeText();
                }
            } else if (slideType == PGSlide.Slide_Normal) {
                text = null;
                if (poiHeadersFooters.getDateTimeText() != null) {
                    text = poiHeadersFooters.getDateTimeText();
                }
            }
        }

        SectionElement secElem = new SectionElement();
        tb.setElement(secElem);

        IAttributeSet attr = secElem.getAttribute();

        AttrManage.instance().setPageWidth(attr, (int) (rect.width * KeyKt.PIXEL_TO_TWIPS));

        AttrManage.instance().setPageHeight(attr, (int) (rect.height * KeyKt.PIXEL_TO_TWIPS));

        AttrManage.instance().setPageMarginLeft(attr, (int) (ts.getMarginLeft() * KeyKt.POINT_TO_TWIPS));

        AttrManage.instance().setPageMarginRight(attr, (int) (ts.getMarginRight() * KeyKt.POINT_TO_TWIPS));

        AttrManage.instance().setPageMarginTop(attr, (int) (ts.getMarginTop() * KeyKt.POINT_TO_TWIPS));

        AttrManage.instance().setPageMarginBottom(attr, (int) (ts.getMarginBottom() * KeyKt.POINT_TO_TWIPS));

        AttrManage.instance().setPageHorizontalAlign(attr, WPAttrConstant.PAGE_H_CENTER);
        AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_CENTER);

        int width = (int) (rect.width - (ts.getMarginLeft() + ts.getMarginRight()) * KeyKt.POINT_TO_PIXEL);
        int height = (int) (rect.height - (ts.getMarginTop() + ts.getMarginBottom()) * KeyKt.POINT_TO_PIXEL);

        offset = 0;
        secElem.setStartOffset(offset);
        Fill fill = ts.getFill();
        int type = fill.getFillType();

        int fontColor = 0xff000000;

        if (type == BackgroundAndFill.FILL_SOLID) {
            if (fill.getForegroundColor() != null) {
                fontColor = converterColor(fill.getForegroundColor());
            }
        } else if (type == BackgroundAndFill.FILL_SHADE_LINEAR
                || type == BackgroundAndFill.FILL_SHADE_RADIAL
                || type == BackgroundAndFill.FILL_SHADE_RECT
                || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
            com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color fillColor = fill.getForegroundColor();

            int[] colors = null;
            if (fill.isShaderPreset()) {
                colors = fill.getShaderColors();
                if (colors != null) {
                    fontColor = colors[0];
                } else if (fillColor != null) {
                    fontColor = fillColor.getRGB();
                }
            }
        }

        processWordArtParagraph(secElem, text, width, height, fontColor);

        secElem.setEndOffset(offset);
        BulletNumberManage.instance().clearData();

    }

    private void processParagraph(SectionElement secElem, TextShape ts, String text,
                                  com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Hyperlink[] links, int start, int end, int placeHolderID) {
        ParagraphElement paraElem = new ParagraphElement();
        paraElem.setStartOffset(offset);

        IAttributeSet attr = paraElem.getAttribute();
        RichTextRun rt = ts.getTextRun().getRichTextRunAt(start);

        AttrManage.instance().setParaHorizontalAlign(attr, rt.getAlignment());

        int temp = rt.getLineSpacing();

        if (temp >= 0) {
            if (temp == 0) {
                temp = 100;
            }
            AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
            AttrManage.instance().setParaLineSpace(attr, temp / 100.f);
        } else {
            AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SPACE_EXACTLY);
            AttrManage.instance().setParaLineSpace(attr, (int) (-temp / 8 * KeyKt.POINT_TO_TWIPS));
        }

        if (tableShape) {
            if (start == 0) {
                AttrManage.instance().setParaBefore(paraElem.getAttribute(), 0);
            }
            if (end == text.length()) {
                AttrManage.instance().setParaAfter(paraElem.getAttribute(), 0);
            }
        }

        int bulletOffset = (int) (rt.getTextOffset() * KeyKt.POINT_TO_TWIPS);
        int textOffset = (int) (rt.getBulletOffset() * KeyKt.POINT_TO_TWIPS);
        int indent = rt.getIndentLevel();
        com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TextRulerAtom ruler = ts.getTextRun().getTextRuler();
        if (ruler != null) {
            temp = ruler.getBulletOffsets()[indent];
            if (temp >= 0) {
                bulletOffset = (int) (temp * KeyKt.POINT_DPI
                        / ShapeKit.MASTER_DPI * KeyKt.POINT_TO_TWIPS);
            }
            temp = ruler.getTextOffsets()[indent];
            if (temp >= 0) {
                textOffset = (int) (temp * KeyKt.POINT_DPI
                        / ShapeKit.MASTER_DPI * KeyKt.POINT_TO_TWIPS);
            }
        }
        temp = textOffset - bulletOffset;
        AttrManage.instance().setParaSpecialIndent(attr, temp);
        if (temp < 0) {

            AttrManage.instance().setParaIndentLeft(attr, textOffset);
        } else {
            AttrManage.instance().setParaIndentLeft(attr, bulletOffset);

        }

        if (rt.isBullet() && !"\n".equals(text.substring(start, end))) {
            temp = BulletNumberManage.instance().addBulletNumber(control, indent, ts.getTextRun().getNumberingType(start),
                    ts.getTextRun().getNumberingStart(start), rt.getBulletChar());
            if (temp >= 0) {
                AttrManage.instance().setPGParaBulletID(attr, temp);
            }
        }

        boolean handleReturn = ts.getTextRun().getRunType() == com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.TextHeaderAtom.TITLE_TYPE;
        while (start < end) {
            if (abortReader) {
                break;
            }
            RichTextRun run = ts.getTextRun().getRichTextRunAt(start);
            if (run == null) {
                break;
            }
            int rtEnd = run.getEndIndex();
            if (rtEnd > end) {
                rtEnd = end;
            }
            if (links != null) {
                boolean hasHyperlink = false;
                for (int i = 0; i < links.length; i++) {
                    int linkStart = links[i].getStartIndex();
                    int linkEnd = links[i].getEndIndex();
                    if (linkStart >= start && linkStart <= rtEnd) {
                        temp = control.getSysKit().getHyperlinkManage().addHyperlink(links[i].getAddress(), Hyperlink.LINK_URL);
                        processRun(ts, run, paraElem, text.substring(start, linkStart), -1, start, linkStart, handleReturn);
                        if (linkEnd <= rtEnd) {
                            processRun(ts, run, paraElem, text.substring(linkStart, linkEnd), temp, linkStart, linkEnd, handleReturn);
                            start = linkEnd;
                        } else {
                            processRun(ts, run, paraElem, text.substring(linkStart, rtEnd), temp, linkStart, rtEnd, handleReturn);
                            start = rtEnd;
                        }
                        hasHyperlink = true;
                        break;
                    } else if (start > linkStart && linkEnd > start) {
                        temp = control.getSysKit().getHyperlinkManage().addHyperlink(links[i].getAddress(), Hyperlink.LINK_URL);
                        if (rtEnd <= linkEnd) {
                            processRun(ts, run, paraElem, text.substring(start, rtEnd), temp, start, rtEnd, handleReturn);
                            start = rtEnd;
                        } else {
                            processRun(ts, run, paraElem, text.substring(start, linkEnd), temp, start, linkEnd, handleReturn);
                            start = linkEnd;
                        }
                        hasHyperlink = true;
                        break;
                    }
                }
                if (hasHyperlink) {
                    continue;
                }
            }
            if (placeHolderID == OEPlaceholderAtom.MasterDate || placeHolderID == OEPlaceholderAtom.MasterFooter) {
                processRun(ts, run, paraElem, text, -1, start, rtEnd, handleReturn);
                start = end;
            } else {
                processRun(ts, run, paraElem, text.substring(start, rtEnd), -1, start, rtEnd, handleReturn);
                start = rtEnd;
            }
        }

        temp = rt.getSpaceBefore();
        if (temp > 0) {
            AttrManage.instance().setParaBefore(attr,
                    (int) (temp / 100.f * maxFontSize * POINT_PER_LINE_PER_FONTSIZE * KeyKt.POINT_TO_TWIPS));
        } else if (temp < 0) {
            AttrManage.instance().setParaBefore(attr, (int) (-temp / 8 * KeyKt.POINT_TO_TWIPS));
        }

        temp = rt.getSpaceAfter();
        if (temp >= 0) {
            AttrManage.instance().setParaAfter(attr,
                    (int) (temp / 100.f * maxFontSize * POINT_PER_LINE_PER_FONTSIZE * KeyKt.POINT_TO_TWIPS));
        } else if (temp < 0) {
            AttrManage.instance().setParaAfter(attr, (int) (-temp / 8 * KeyKt.POINT_TO_TWIPS));
        }

        paraElem.setEndOffset(offset);
        secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
    }

    private void processWordArtParagraph(SectionElement secElem, String text, int width, int height, int fontColor) {
        ParagraphElement paraElem = new ParagraphElement();
        paraElem.setStartOffset(offset);

        IAttributeSet paraAttr = paraElem.getAttribute();

        AttrManage.instance().setParaHorizontalAlign(paraAttr, WPAttrConstant.PAGE_H_CENTER);

        LeafElement leaf = new LeafElement(text);

        IAttributeSet attr = leaf.getAttribute();

        int fontsize = 12;
        Paint paint = PaintKit.instance().getPaint();
        paint.setTextSize(fontsize);
        FontMetrics fm = paint.getFontMetrics();
        while ((int) paint.measureText(text) < width && (int) (Math.ceil(fm.descent - fm.ascent)) < height) {
            paint.setTextSize(++fontsize);
            fm = paint.getFontMetrics();
        }

        AttrManage.instance().setFontSize(leaf.getAttribute(), (int) ((fontsize - 1) * KeyKt.PIXEL_TO_POINT));

        AttrManage.instance().setFontColor(attr, fontColor);

        setMaxFontSize(18);

        leaf.setStartOffset(offset);

        offset += text.length();
        leaf.setEndOffset(offset);
        paraElem.appendLeaf(leaf);

        paraElem.setEndOffset(offset);
        secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
    }

    private void processRun(TextShape ts, RichTextRun run, ParagraphElement paraElem,
                            String text, int linkIndex, int start, int end, boolean handleReturn) {
        Sheet sheet = ts.getSheet();
        byte mcType = ts.getMetaCharactersType();

        text = text.replace((char) 160, ' ');
        int pos = 0;
        if (handleReturn) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    processRun(ts, run, paraElem, text.substring(pos, i), linkIndex, start + pos, start + i, false);
                    processRun(ts, run, paraElem, String.valueOf('\u000b'), linkIndex, start + i, start + i + 1, false);
                    pos = i + 1;
                }
            }
            if (pos < text.length()) {
                processRun(ts, run, paraElem, text.substring(pos), linkIndex, start + pos, start + text.length(), false);
                pos = text.length();
            }
        }
        start += pos;
        maxFontSize = 0;
        if (end <= start) {
            return;
        }

        if (text.length() > end) {
            text = text.substring(start, end);
        }

        if (text.contains("*")) {
            if (mcType == TextBox.MC_DateTime
                    || mcType == TextBox.MC_GenericDate
                    || mcType == TextBox.MC_RTFDateTime) {

                String val = NumericFormatter.instance().getFormatContents("yyyy/m/d", new Date(System.currentTimeMillis()));
                text = text.replace("*", val);
            } else if (mcType == TextBox.MC_Footer && poiHeadersFooters.getFooterText() != null) {
                text = poiHeadersFooters.getFooterText();
            }
        }

        LeafElement leaf = new LeafElement(text);

        IAttributeSet attr = leaf.getAttribute();

        int temp = run.getFontSize();
        AttrManage.instance().setFontSize(attr, temp > 0 ? temp : 18);
        setMaxFontSize(run.getFontSize());
        if (!"\n".equals(text)) {

            if (run.getFontName() != null) {
                temp = FontTypefaceManage.instance().addFontName(run.getFontName());
                if (temp >= 0) {
                    AttrManage.instance().setFontName(attr, temp);
                }
            }

            AttrManage.instance().setFontColor(attr, converterColor(run.getFontColor()));

            AttrManage.instance().setFontBold(attr, run.isBold());

            AttrManage.instance().setFontItalic(attr, run.isItalic());

            AttrManage.instance().setFontUnderline(attr, run.isUnderlined() ? 1 : 0);

            AttrManage.instance().setFontStrike(attr, run.isStrikethrough());
            temp = run.getSuperscript();
            if (temp != 0) {
                AttrManage.instance().setFontScript(attr, temp > 0 ? 1 : 2);
            }

            if (linkIndex >= 0) {
                int color = Color.BLUE;
                if (sheet != null) {
                    color = FCKit.BGRtoRGB(sheet.getColorScheme().getAccentAndHyperlinkColourRGB());
                }
                AttrManage.instance().setFontColor(attr, color);
                AttrManage.instance().setFontUnderline(attr, 1);
                AttrManage.instance().setFontUnderlineColr(attr, color);
                AttrManage.instance().setHyperlinkID(attr, linkIndex);
            }
        }

        leaf.setStartOffset(offset);

        offset += text.length();
        leaf.setEndOffset(offset);
        paraElem.appendLeaf(leaf);
    }

    public boolean searchContent(File file, String key) throws Exception {
        SlideShow slideShow = new SlideShow(new HSLFSlideShow(control, filePath));
        Slide[] slides = slideShow.getSlides();
        for (Slide slide : slides) {

            Shape[] shapes = slide.getShapes();
            for (Shape shape : shapes) {
                if (searchShape(shape, key)) {
                    return true;
                }
            }

            Notes notes = slide.getNotesSheet();
            if (notes != null) {
                for (Shape shape : notes.getShapes()) {
                    if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.AutoShape
                            || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextBox) {
                        OEPlaceholderAtom phAtom = ((TextShape) shape).getPlaceholderAtom();
                        if (phAtom != null && phAtom.getPlaceholderId() == OEPlaceholderAtom.NotesBody
                                && searchShape(shape, key)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean searchShape(Shape shape, String key) {
        StringBuilder sb = new StringBuilder();
        if (shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.AutoShape
                || shape instanceof com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.TextBox) {
            sb.append(((TextShape) shape).getText());
            if (sb.indexOf(key) >= 0) {
                return true;
            }
            sb.delete(0, sb.length());
        } else if (shape instanceof ShapeGroup) {
            Shape[] sh = ((ShapeGroup) shape).getShapes();
            for (int i = 0; i < sh.length; i++) {
                if (searchShape(sh[i], key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int converterColor(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Color color) {

        return color.getRGB();
    }

    public void setMaxFontSize(int size) {
        if (size > maxFontSize) {
            maxFontSize = size;
        }
    }

    public boolean isRectangle(TextShape ts) {
        int type = ts.getShapeType();
        return type == ShapeTypes.Rectangle || type == ShapeTypes.RoundRectangle
                || type == ShapeTypes.TextBox;
    }

    public void processGrpRotation(Shape shape, IShape autoShape) {
        float angle = shape.getRotation();
        if (shape.getFlipHorizontal()) {
            autoShape.setFlipHorizontal(true);
            angle = -angle;
        }
        if (shape.getFlipVertical()) {
            autoShape.setFlipVertical(true);
            angle = -angle;
        }

        if (autoShape instanceof LineShape) {
            if ((angle == 45 || angle == 135 || angle == 225)
                    && !autoShape.getFlipHorizontal()
                    && !autoShape.getFlipVertical()) {
                angle -= 90;
            }
        }
        autoShape.setRotation(angle);
    }

    public void dispose() {
        if (isReaderFinish()) {
            super.dispose();

            if (abortReader && model != null && model.getSlideCount() < FIRST_READ_SLIDE_NUM
                    && poiSlideShow.getSlideCount() > 0) {
                model.dispose();
            }
            model = null;
            filePath = null;
            if (poiSlideShow != null) {
                try {
                    poiSlideShow.dispose();
                } catch (Exception e) {

                }
                poiSlideShow = null;
            }
            if (slideMasterIndexs != null) {
                slideMasterIndexs.clear();
                slideMasterIndexs = null;
            }
            if (titleMasterIndexs != null) {
                titleMasterIndexs.clear();
                titleMasterIndexs = null;
            }
            BulletNumberManage.instance().dispose();
            System.gc();
        }
    }
}
