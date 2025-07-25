package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.ArbitraryPolygonShapePath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.AutoShapeTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfoFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ArbitraryPolygonShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.Arrow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AutoShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.GroupShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.IShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.LineShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.PictureShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.SmartArt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.LineKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationship;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.ZipPackage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.ParaAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.RunAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.SectionAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.BackgroundReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.PictureReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.ReaderKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.TableReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.Reader.drawing.ChartReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGLayout;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGMaster;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGPlaceholderUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGSlide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGStyle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttrManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IAttributeSet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.AbstractChart;

public class ShapeManage {
    private static final ShapeManage kit = new ShapeManage();

    public static ShapeManage instance() {
        return kit;
    }

    public Integer processShape(IControl control, ZipPackage zipPackage, PackagePart packagePart,
                                PGModel pgModel, PGMaster pgMaster, PGLayout pgLayout, PGStyle defaultStyle, PGSlide pgSlide, byte slideType,
                                Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception {
        if (ReaderKit.instance().isHidden(sp)) {
            return null;
        }
        boolean addShape = packagePart.getPartName().getName().contains("/ppt/slides/");
        addShape = addShape || (!addShape && ReaderKit.instance().isUserDrawn(sp));
        RunAttr.instance().setSlide(addShape);
        String name = sp.getName();
        if (name.equals("sp") || name.equals("cxnSp")) {

            return processAutoShapeAndTextShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, defaultStyle, pgSlide, slideType,
                    sp, parent, zoomX, zoomY, addShape);
        } else if (name.equals("pic")) {

            if (addShape) {
                return processPicture(control, zipPackage, packagePart, pgMaster, pgLayout, pgSlide, sp, parent, zoomX, zoomY);
            }
        } else if (name.equals("graphicFrame")) {

            if (addShape) {
                return processGraphicFrame(control, zipPackage, packagePart, pgModel,
                        pgMaster, pgLayout, pgSlide, sp, parent, zoomX, zoomY);
            }
        } else if (name.equals("grpSp")) {

            Element e = sp.element("nvGrpSpPr");
            int grpShapeID = 0;
            if (e != null && (e = e.element("cNvPr")) != null) {
                grpShapeID = Integer.parseInt(e.attributeValue("id"));
            }

            GroupShape groupShape = null;
            float[] zoomXY = null;
            Element grpSpPr = sp.element("grpSpPr");
            if (grpSpPr != null) {
                Rectangle rect = null;
                Rectangle childRect = null;
                rect = ReaderKit.instance().getShapeAnchor(grpSpPr.element("xfrm"), zoomX, zoomY);
                rect = processGrpSpRect(parent, rect);

                zoomXY = ReaderKit.instance().getAnchorFitZoom(grpSpPr.element("xfrm"));
                childRect = ReaderKit.instance().getChildShapeAnchor(grpSpPr.element("xfrm"), zoomXY[0] * zoomX, zoomXY[1] * zoomY);

                groupShape = new GroupShape();
                groupShape.setOffPostion(rect.x - childRect.x,
                        rect.y - childRect.y);
                groupShape.setShapeID(grpShapeID);

                groupShape.setBounds(rect);
                groupShape.setParent(parent);
                processGrpRotation(parent, groupShape, grpSpPr);
            }

            List<Integer> childShapeLst = new ArrayList<Integer>();
            Integer shapeId;
            for (Iterator<?> it = sp.elementIterator(); it.hasNext(); ) {
                shapeId = processShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout,
                        defaultStyle, pgSlide, slideType, (Element) it.next(), groupShape, zoomXY[0] * zoomX, zoomXY[1] * zoomY);
                if (shapeId != null) {
                    childShapeLst.add(shapeId);
                }
            }

            if (parent == null) {
                pgSlide.appendShapes(groupShape);
            } else {
                parent.appendShapes(groupShape);
            }
            pgSlide.addGroupShape(grpShapeID, childShapeLst);

            return grpShapeID;
        } else if (name.equals("AlternateContent")) {
            Element choice = sp.element("Fallback");
            if (choice != null) {
                for (Iterator<?> it = choice.elementIterator(); it.hasNext(); ) {
                    processShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout,
                            defaultStyle, pgSlide, slideType, (Element) it.next(), parent, zoomX, zoomY);
                }
            }
        }
        RunAttr.instance().setSlide(false);

        return null;
    }

    public int processAutoShapeAndTextShape(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel,
                                            PGMaster pgMaster, PGLayout pgLayout, PGStyle defaultStyle, PGSlide pgSlide, byte slideType, Element sp,
                                            GroupShape parent, float zoomX, float zoomY, boolean addShape) throws Exception {

        int id = 0;
        Element temp = sp.element("nvSpPr");
        if (temp == null) {
            temp = sp.element("nvCxnSpPr");
        }
        temp = temp.element("cNvPr");
        id = Integer.parseInt(temp.attributeValue("id"));

        String type = ReaderKit.instance().getPlaceholderType(sp);
        int idx = ReaderKit.instance().getPlaceholderIdx(sp);
        if (slideType == PGSlide.Slide_Layout) {
            pgLayout.addShapeType(idx, type);
        } else if (type == null && pgMaster != null && idx >= 0) {
            type = pgLayout.getShapeType(idx);
        }

        int placeHolderID = -1;
        if (PGPlaceholderUtil.instance().isTitleOrBody(type)) {
            if (slideType == PGSlide.Slide_Master) {
                pgMaster.addTitleBodyID(idx, idx);
            } else if (slideType == PGSlide.Slide_Layout) {
                pgLayout.addTitleBodyID(idx, id);
            }
        } else if ((slideType == PGSlide.Slide_Master || slideType == PGSlide.Slide_Layout)
                && ReaderKit.instance().isUserDrawn(sp)) {
            placeHolderID = 0;
        }

        Rectangle rect = null;
        temp = sp.element("spPr");
        if (temp != null) {
            rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), zoomX, zoomY);
        }
        if (rect == null && pgLayout != null) {
            rect = pgLayout.getAnchor(type, idx);
            if (rect == null && pgMaster != null) {
                rect = pgMaster.getAnchor(type, idx);
            }
        }
        if (rect != null) {
            rect = processGrpSpRect(parent, rect);

            AbstractShape shape = null;

            if (addShape || (!addShape && !PGPlaceholderUtil.instance().isHeaderFooter(type))) {
                shape = processAutoShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, pgSlide, sp, id, idx, rect,
                        isRect(type, idx), parent, slideType, type, !addShape && PGPlaceholderUtil.instance().isTitleOrBody(type));
            }

            if (shape != null) {
                if (parent == null) {
                    pgSlide.appendShapes(shape);
                } else {
                    parent.appendShapes(shape);
                }

                shape.setPlaceHolderID(placeHolderID);
                processGrpRotation(parent, shape, sp.element("spPr"));
            }

            temp = sp.element("txBody");
            if (temp != null && addShape) {
                TextBox tb = new TextBox();

                tb.setBounds(rect);
                tb.setPlaceHolderID(placeHolderID);
                tb.setShapeID(id);

                SectionElement secElem = new SectionElement();

                secElem.setStartOffset(0);
                tb.setElement(secElem);

                IAttributeSet attr = secElem.getAttribute();

                AttrManage.instance().setPageWidth(attr, (int) (rect.width * KeyKt.PIXEL_TO_TWIPS));

                AttrManage.instance().setPageHeight(attr, (int) (rect.height * KeyKt.PIXEL_TO_TWIPS));

                IAttributeSet attrLayout = null;
                IAttributeSet attrMaster = null;
                if (pgLayout != null) {
                    attrLayout = pgLayout.getSectionAttr(type, idx);
                }
                if (pgMaster != null) {
                    attrMaster = pgMaster.getSectionAttr(type, idx);
                }
                SectionAttr.instance().setSectionAttribute(temp.element("bodyPr"), attr, attrLayout, attrMaster, false);
                int offset = ParaAttr.instance().processParagraph(control, pgMaster, pgLayout, defaultStyle,
                        secElem, sp.element("style"), temp, type, idx);
                secElem.setEndOffset(offset);
                if (tb.getElement() != null && tb.getElement().getText(null) != null
                        && tb.getElement().getText(null).length() > 0
                        && !"\n".equals(tb.getElement().getText(null))) {
                    processGrpRotation(parent, tb, sp.element("spPr"));

                    if (parent == null) {
                        pgSlide.appendShapes(tb);
                    } else {
                        parent.appendShapes(tb);
                    }
                } else if (shape != null) {

                    processGrpRotation(parent, shape, sp.element("spPr"));
                }

                Element wrap = temp.element("bodyPr");
                if (wrap != null) {

                    String value = wrap.attributeValue("wrap");
                    tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
                }
            }
        }

        return id;
    }

    public int processPicture(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster pgMaster,
                              PGLayout pgLayout, PGSlide pgSlide, Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception {

        Element e = sp.element("nvPicPr");
        int shapeID = 0;
        if (e != null && (e = e.element("cNvPr")) != null) {
            shapeID = Integer.parseInt(e.attributeValue("id"));
        }

        Element blipFill = sp.element("blipFill");
        if (blipFill == null) {
            Element alternateContent = sp.element("AlternateContent");
            if (alternateContent != null) {
                Element fallback = alternateContent.element("Fallback");
                if (fallback != null) {
                    blipFill = fallback.element("blipFill");
                }
            }
        }
        if (blipFill != null) {
            Element blip = blipFill.element("blip");
            if (blip != null && blip.attribute("embed") != null) {
                String id = blip.attributeValue("embed");
                if (id != null) {
                    Element spPr = sp.element("spPr");
                    if (spPr != null) {
                        Rectangle rect = ReaderKit.instance().getShapeAnchor(spPr.element("xfrm"), zoomX, zoomY);
                        if (rect == null && pgLayout != null) {

                            String type = ReaderKit.instance().getPlaceholderType(sp);

                            int idx = ReaderKit.instance().getPlaceholderIdx(sp);
                            rect = pgLayout.getAnchor(type, idx);
                            if (rect == null && pgMaster != null) {
                                rect = pgMaster.getAnchor(type, idx);
                            }
                        }
                        if (rect != null) {
                            rect = processGrpSpRect(parent, rect);
                            PackageRelationship imageShip = packagePart.getRelationship(id);
                            if (imageShip != null) {
                                BackgroundAndFill fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, spPr);
                                Line line = LineKit.createShapeLine(control, zipPackage, packagePart, pgMaster, sp);

                                PackagePart picPart = zipPackage.getPart(imageShip.getTargetURI());
                                PictureShape picShape = addPicture(control, picPart, pgSlide, shapeID, rect, sp.element("spPr"), parent, PictureEffectInfoFactory.getPictureEffectInfor(blipFill));
                                if (picShape != null) {
                                    picShape.setBackgroundAndFill(fill);
                                    picShape.setLine(line);
                                }
                            }
                        }
                    }
                }
            }
        }

        return shapeID;
    }

    public PictureShape addPicture(IControl control, PackagePart picPart, PGSlide pgSlide, int shapeID, Rectangle rect, Element spPr,
                                   GroupShape parent, PictureEffectInfo effectInfor) throws Exception {
        PictureShape picShape = null;
        if (picPart != null) {
            picShape = new PictureShape();
            picShape.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
            picShape.setBounds(rect);
            processGrpRotation(parent, picShape, spPr);
            picShape.setShapeID(shapeID);
            picShape.setPictureEffectInfor(effectInfor);
            if (parent == null) {
                pgSlide.appendShapes(picShape);
            } else {
                parent.appendShapes(picShape);
            }
        }

        return picShape;
    }

    public int processGraphicFrame(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster,
                                   PGLayout pgLayout, PGSlide pgSlide, Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception {

        Element nvGraphicFramePr = sp.element("nvGraphicFramePr");
        int shapeId = 0;
        if (nvGraphicFramePr != null && (nvGraphicFramePr = nvGraphicFramePr.element("cNvPr")) != null) {
            shapeId = Integer.parseInt(nvGraphicFramePr.attributeValue("id"));
        }

        Element xfrm = sp.element("xfrm");
        Rectangle rect = ReaderKit.instance().getShapeAnchor(xfrm, zoomX, zoomY);
        if (rect == null && pgLayout != null) {

            String type = ReaderKit.instance().getPlaceholderType(sp);

            int idx = ReaderKit.instance().getPlaceholderIdx(sp);
            rect = pgLayout.getAnchor(type, idx);
            if (rect == null && pgMaster != null) {
                rect = pgMaster.getAnchor(type, idx);
            }
        }
        if (rect != null) {
            rect = processGrpSpRect(parent, rect);
            Element graphic = sp.element("graphic");
            if (graphic != null) {
                Element graphicData = graphic.element("graphicData");
                if (graphicData != null && graphicData.attribute("uri") != null) {
                    String uri = graphicData.attributeValue("uri");
                    if (uri.equals(PackageRelationshipTypes.OLE_TYPE)) {
                        Element oleObj = graphicData.element("oleObj");
                        if (oleObj == null) {
                            Element alternateContent = graphicData.element("AlternateContent");
                            if (alternateContent != null) {
                                Element fallback = alternateContent.element("Fallback");
                                if (fallback != null) {
                                    oleObj = fallback.element("oleObj");
                                    if (oleObj != null) {
                                        Element pic = oleObj.element("pic");
                                        if (pic != null) {
                                            processPicture(control, zipPackage, packagePart, pgMaster, pgLayout,
                                                    pgSlide, pic, parent, zoomX, zoomY);
                                        }
                                    }
                                }
                            }
                        } else if (oleObj.attribute("spid") != null) {
                            String spid = oleObj.attributeValue("spid");
                            PackagePart picPart = PictureReader.instance().getOLEPart(zipPackage, packagePart, spid, false);
                            addPicture(control, picPart, pgSlide, shapeId, rect, sp.element("spPr"), parent, null);
                        }
                    } else if (uri.equals(PackageRelationshipTypes.CHART_TYPE)) {
                        Element chart = graphicData.element("chart");
                        if (chart != null && chart.attribute("id") != null) {
                            String id = chart.attributeValue("id");
                            PackageRelationship ship = packagePart.getRelationship(id);
                            if (ship != null) {
                                PackagePart chartPart = zipPackage.getPart(ship.getTargetURI());
                                AbstractChart abstrChart = ChartReader.instance().read(control, zipPackage, chartPart, pgMaster.getSchemeColor(), KeyKt.APPLICATION_TYPE_PPT);
                                if (abstrChart != null) {
                                    AChart shape = new AChart();
                                    shape.setAChart(abstrChart);
                                    shape.setBounds(rect);
                                    shape.setShapeID(shapeId);
                                    pgSlide.appendShapes(shape);
                                }
                            }
                        }
                    } else if (uri.equals(PackageRelationshipTypes.TABLE_TYPE)) {
                        Element tbl = graphicData.element("tbl");
                        if (tbl != null) {
                            Element temp = tbl.element("tblPr");
                            if (temp != null) {
                                TableShape table = TableReader.instance().getTable(control, zipPackage, packagePart, pgModel,
                                        pgMaster, tbl, rect);
                                if (table != null) {
                                    table.setBounds(rect);
                                    table.setShapeID(shapeId);
                                    pgSlide.appendShapes(table);
                                }
                            }
                        }
                    } else if (uri.equals(PackageRelationshipTypes.DIAGRAM_TYPE)) {
                        processSmartArt(pgSlide, graphicData, rect);
                    }
                }
            }
        }

        return shapeId;
    }

    private Rectangle processGrpSpRect(GroupShape parent, Rectangle rect) {
        if (parent != null) {
            rect.x += parent.getOffX();
            rect.y += parent.getOffY();
        }
        return rect;
    }

    private void processGrpRotation(IShape parent, IShape shape, Element spPr) {
        ReaderKit.instance().processRotation(spPr, shape);

    }

    private boolean isRect(String type, int idx) {
        if (type != null && (type.contains("Title") || type.contains("title")
                || type.contains("ctrTitle") || type.contains("subTitle") || type.contains("body")
                || type.contains("body") || type.contains("half") || type.contains("dt")
                || type.contains("ftr") || type.contains("sldNum"))) {
            return true;
        } else return idx > 0;
    }

    private BackgroundAndFill getBackgrouond(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster,
                                             PGLayout pgLayout, PGSlide pgSlide, Element sp, int shapeIDX, byte slideType, String phType, int shapeType) throws Exception {

        BackgroundAndFill fill = null;
        if (sp.attribute("useBgFill") != null) {
            String val = sp.attributeValue("useBgFill");
            if (val != null && val.length() > 0 && "1".equals(val)) {
                fill = pgSlide.getBackgroundAndFill();
                if (fill == null) {
                    if (pgLayout != null) {
                        fill = pgLayout.getBackgroundAndFill();
                    }
                    if (fill == null && pgMaster != null) {
                        fill = pgMaster.getBackgroundAndFill();
                    }
                }

                if (fill != null) {
                    fill.setSlideBackgroundFill(true);
                }

                return fill;
            }
        }

        Element spPr = sp.element("spPr");
        String spName = sp.getName();
        if (fill == null && spPr.element("noFill") == null && !spName.equals("cxnSp")) {
            fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, spPr);
            if (fill == null && shapeType != ShapeTypes.Arc && shapeType != ShapeTypes.BracketPair
                    && shapeType != ShapeTypes.LeftBracket && shapeType != ShapeTypes.RightBracket
                    && shapeType != ShapeTypes.BracePair && shapeType != ShapeTypes.LeftBrace
                    && shapeType != ShapeTypes.RightBrace && shapeType != ShapeTypes.ArbitraryPolygon) {
                fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, sp.element("style"));
                if (fill != null && fill.getFillType() == BackgroundAndFill.FILL_SOLID && (fill.getForegroundColor() & 0xFFFFFF) == 0) {
                    fill = null;
                }
            }
        }

        Integer shapeID = null;

        if (fill == null && slideType == PGSlide.Slide_Normal && PGPlaceholderUtil.instance().isTitleOrBody(phType)
                && pgLayout != null && pgLayout.getSlideMasterIndex() >= 0
                && shapeIDX >= 0) {
            PGSlide layoutSlide = pgModel.getSlideMaster(pgLayout.getSlideMasterIndex());
            shapeID = pgLayout.getTitleBodyID(shapeIDX);
            if (shapeID != null) {
                IShape[] shapes = layoutSlide.getShapes();
                for (int i = 0; i < shapes.length; i++) {
                    if (shapeID == shapes[i].getShapeID() && shapes[i] instanceof AutoShape) {
                        fill = ((AutoShape) shapes[i]).getBackgroundAndFill();
                        break;
                    }
                }
            }
        }

        if (fill == null && slideType == PGSlide.Slide_Normal
                && pgMaster != null
                && pgMaster.getSlideMasterIndex() >= 0
                && shapeIDX >= 0) {
            PGSlide masterSlide = pgModel.getSlideMaster(pgMaster.getSlideMasterIndex());
            IShape[] shapes = masterSlide.getShapes();
            if (pgMaster.getTitleBodyID(shapeIDX) != null) {
                shapeID = pgMaster.getTitleBodyID(shapeIDX);
                if (shapeID != null) {
                    for (int i = 0; i < shapes.length; i++) {
                        if (shapeID == shapes[i].getShapeID() && shapes[i] instanceof AutoShape) {
                            fill = ((AutoShape) shapes[i]).getBackgroundAndFill();
                            break;
                        }
                    }
                }
            }
        }

        return fill;
    }

    public AbstractShape processAutoShape(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster,
                                          PGLayout pgLayout, PGSlide pgSlide, Element sp, int id, int idx, Rectangle rect, boolean isRect,
                                          GroupShape parent, byte slideType, String phType, boolean hidden) throws Exception {
        AbstractShape shape = null;
        int shapeType = ShapeTypes.NotPrimitive;
        Element spPr = sp.element("spPr");
        if (spPr != null) {
            String val;
            Float[] values = null;
            boolean border = true;
            String name = ReaderKit.instance().getPlaceholderName(sp);
            String spName = sp.getName();
            if (spName.equals("cxnSp")) {
                border = true;
                shapeType = ShapeTypes.StraightConnector1;
            } else if (isRect || name.contains("Text Box") || name.contains("TextBox")) {
                shapeType = ShapeTypes.Rectangle;
            }

            Element prstGeom = spPr.element("prstGeom");
            if (prstGeom != null) {
                if (prstGeom.attribute("prst") != null) {
                    val = prstGeom.attributeValue("prst");
                    if (val != null && val.length() > 0) {
                        shapeType = AutoShapeTypes.instance().getAutoShapeType(val);
                    }
                }

                Element avLst = prstGeom.element("avLst");
                if (avLst != null) {
                    List<Element> gds = avLst.elements("gd");
                    if (gds.size() > 0) {
                        values = new Float[gds.size()];
                        for (int i = 0; i < gds.size(); i++) {
                            Element gd = gds.get(i);
                            val = gd.attributeValue("fmla");
                            val = val.substring(4);
                            values[i] = Float.parseFloat(val) / 100000;
                        }
                    }
                }
            } else if (spPr.element("custGeom") != null) {

                shapeType = ShapeTypes.ArbitraryPolygon;
            } else if (isRect) {
                shapeType = ShapeTypes.Rectangle;
            }

            BackgroundAndFill fill = getBackgrouond(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, pgSlide, sp, idx, slideType, phType, shapeType);
            Line line = LineKit.createShapeLine(control, zipPackage, packagePart, pgMaster, sp);

            Element ln = spPr.element("ln");
            Element style = sp.element("style");
            if (ln != null) {

                if (ln.element("noFill") != null) {
                    border = false;
                }
            } else if (border) {
                if (style == null || style.element("lnRef") == null) {
                    border = false;
                }
            }

            if (shapeType == ShapeTypes.Line
                    || shapeType == ShapeTypes.StraightConnector1
                    || shapeType == ShapeTypes.BentConnector2
                    || shapeType == ShapeTypes.BentConnector3
                    || shapeType == ShapeTypes.CurvedConnector2
                    || shapeType == ShapeTypes.CurvedConnector3
                    || shapeType == ShapeTypes.CurvedConnector4
                    || shapeType == ShapeTypes.CurvedConnector5) {
                if (!border) {
                    return shape;
                }
                LineShape lineShape = new LineShape();
                lineShape.setShapeType(shapeType);
                lineShape.setBounds(rect);
                lineShape.setShapeID(id);
                lineShape.setHidden(hidden);
                lineShape.setAdjustData(values);
                lineShape.setLine(line);

                if (ln != null) {
                    Element temp = ln.element("headEnd");
                    if (temp != null && temp.attribute("type") != null) {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None) {
                            lineShape.createStartArrow(arrowType,
                                    Arrow.getArrowSize(temp.attributeValue("w")),
                                    Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                    temp = ln.element("tailEnd");
                    if (temp != null && temp.attribute("type") != null) {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None) {
                            lineShape.createEndArrow(arrowType,
                                    Arrow.getArrowSize(temp.attributeValue("w")),
                                    Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                }

                return shape = lineShape;
            } else if (shapeType == ShapeTypes.ArbitraryPolygon) {
                ArbitraryPolygonShape arbitraryPolygonShape = new ArbitraryPolygonShape();
                BackgroundAndFill lineFill = null;
                if (line != null) {
                    lineFill = line.getBackgroundAndFill();
                }
                ArbitraryPolygonShapePath.processArbitraryPolygonShape(arbitraryPolygonShape, sp, fill, border, lineFill, ln, rect);

                arbitraryPolygonShape.setShapeType(shapeType);
                arbitraryPolygonShape.setShapeID(id);
                processGrpRotation(parent, arbitraryPolygonShape, spPr);
                arbitraryPolygonShape.setHidden(hidden);
                arbitraryPolygonShape.setLine(line);

                return shape = arbitraryPolygonShape;
            } else if (fill != null || line != null) {
                AutoShape autoShape = new AutoShape(shapeType);
                autoShape.setBounds(rect);
                autoShape.setShapeID(id);
                autoShape.setHidden(hidden);

                if (fill != null) {
                    autoShape.setBackgroundAndFill(fill);
                }
                if (line != null) {
                    autoShape.setLine(line);
                }
                autoShape.setAdjustData(values);

                return shape = autoShape;
            }
        }
        return shape;
    }

    private void processSmartArt(PGSlide pgslide, Element graphicData, Rectangle rect) {
        try {
            if (graphicData != null) {
                Element relIds = graphicData.element("relIds");
                String cs = relIds.attributeValue("dm");
                int id = Integer.parseInt(cs.substring("rId".length()));
                if (cs != null) {
                    SmartArt smartArt = pgslide.getSmartArt(cs);
                    if (smartArt != null) {
                        smartArt.setBounds(rect);
                        IShape[] shapes = smartArt.getShapes();
                        for (IShape shape : shapes) {
                            shape.setShapeID(id);
                        }

                        pgslide.appendShapes(smartArt);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
