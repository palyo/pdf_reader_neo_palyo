package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.Reader.drawing;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.AutoShapeDataKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfoFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.GroupShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.PictureShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.SmartArt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.io.SAXReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationship;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.ZipPackage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.ParaAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.RunAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.SectionAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.PictureReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.ReaderKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.Reader.SchemeColorUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font.Font;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttrManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttributeSetImpl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IAttributeSet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.LeafElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.ParagraphElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Sheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.drawing.AnchorPoint;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.drawing.CellAnchor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.drawing.TextParagraph;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.style.CellStyle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.ModelUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.AbstractChart;

public class DrawingReader {
    private static final DrawingReader reader = new DrawingReader();
    private Sheet sheet;
    private Map<String, Integer> drawingList;
    private Map<String, AbstractChart> chartList;
    private Map<String, SmartArt> smartArtList;
    private int offset;

    public static DrawingReader instance() {
        return reader;
    }

    public static short getVerticalByString(String anchor) {
        if (anchor == null || anchor.equalsIgnoreCase("ctr")) {
            return CellStyle.VERTICAL_CENTER;
        } else if (anchor.equalsIgnoreCase("t")) {
            return CellStyle.VERTICAL_TOP;
        } else if (anchor.equalsIgnoreCase("b")) {
            return CellStyle.VERTICAL_BOTTOM;
        } else if (anchor.equalsIgnoreCase("just")) {
            return CellStyle.VERTICAL_JUSTIFY;
        } else if (anchor.equalsIgnoreCase("dist")) {
            return CellStyle.VERTICAL_JUSTIFY;
        } else {
            return CellStyle.VERTICAL_CENTER;
        }

    }

    public static short getHorizontalByString(String algn) {
        if (algn == null || algn.equalsIgnoreCase("l")) {
            return CellStyle.ALIGN_LEFT;
        } else if (algn.equalsIgnoreCase("ctr")) {
            return CellStyle.ALIGN_CENTER;
        } else if (algn.equalsIgnoreCase("r")) {
            return CellStyle.ALIGN_RIGHT;
        } else if (algn.equalsIgnoreCase("just")) {
            return CellStyle.ALIGN_JUSTIFY;
        } else {
            return CellStyle.ALIGN_GENERAL;
        }
    }

    private static Font getFont(Element rPr) {
        Font font = new Font();

        if (rPr.attributeValue("sz") != null) {
            font.setFontSize(Integer.parseInt(rPr.attributeValue("sz")) / 100);
        }

        if (rPr.attributeValue("b") != null
                && Integer.parseInt(rPr.attributeValue("b")) != 0) {
            font.setBold(true);
        }

        if (rPr.attributeValue("i") != null
                && Integer.parseInt(rPr.attributeValue("i")) != 0) {
            font.setItalic(true);
        }

        if (rPr.attributeValue("u") != null) {
            if (rPr.attributeValue("u").equalsIgnoreCase("sng")) {
                font.setUnderline(Font.U_SINGLE);
            } else if (rPr.attributeValue("u").equalsIgnoreCase("dbl")) {
                font.setUnderline(Font.U_DOUBLE);
            }
        }

        if (rPr.attributeValue("strike") != null
                && !rPr.attributeValue("strike").equalsIgnoreCase("noStrike")) {
            font.setStrikeline(true);
        }

        Element solidFill = rPr.element("solidFill");
        if (solidFill != null) {
        }
        font.setColorIndex(8);

        return font;
    }

    public static TextParagraph getTextParagraph(Element paragraph) {
        TextParagraph textParagraph = new TextParagraph();

        Element ele = paragraph.element("pPr");
        if (ele != null) {

            textParagraph.setHorizontalAlign(
                    getHorizontalByString(ele.attributeValue("algn")));

        }

        Iterator<Element> iter = paragraph.elements("r").iterator();
        Element textRun;
        Font font = null;
        String run = "";
        while (iter.hasNext()) {
            textRun = iter.next();
            if (font == null && (ele = textRun.element("rPr")) != null) {

                font = getFont(ele);
            }

            if (textRun.element("t") != null) {
                run += textRun.element("t").getText();
            }
        }
        textParagraph.setFont(font);
        textParagraph.setTextRun(run);

        return textParagraph;
    }

    public void read(IControl control, ZipPackage zipPackage, PackagePart drawingPart, Sheet sheet) throws Exception {
        this.sheet = sheet;

        Map<String, Integer> schemeColor = SchemeColorUtil.getSchemeColor(sheet.getWorkbook());

        PackageRelationshipCollection chartRelCollection =
                drawingPart.getRelationshipsByType(PackageRelationshipTypes.CHART_PART);

        chartList = new HashMap<String, AbstractChart>();
        Iterator<PackageRelationship> iter = chartRelCollection.iterator();
        PackageRelationship rel;
        PackagePart part;
        while (iter.hasNext()) {
            rel = iter.next();
            part = zipPackage.getPart(rel.getTargetURI());

            chartList.put(rel.getId(), ChartReader.instance().read(control, zipPackage, part, schemeColor, KeyKt.APPLICATION_TYPE_SS));
        }

        smartArtList = new HashMap<String, SmartArt>();
        PackageRelationshipCollection smartArtDataCollection = drawingPart.getRelationshipsByType(PackageRelationshipTypes.DIAGRAM_DATA);
        if (smartArtDataCollection != null && smartArtDataCollection.size() > 0) {
            int cnt = smartArtDataCollection.size();
            for (int i = 0; i < cnt; i++) {
                rel = smartArtDataCollection.getRelationship(i);
                smartArtList.put(rel.getId(), SmartArtReader.instance().read(control, zipPackage, drawingPart, zipPackage.getPart(rel.getTargetURI()), schemeColor, sheet));
            }
        }

        PackageRelationshipCollection imageRelCollection =
                drawingPart.getRelationshipsByType(PackageRelationshipTypes.IMAGE_PART);

        drawingList = new HashMap<String, Integer>(10);

        iter = imageRelCollection.iterator();
        while (iter.hasNext()) {
            rel = iter.next();
            part = zipPackage.getPart(rel.getTargetURI());
            drawingList.put(rel.getId(), control.getSysKit().getPictureManage().addPicture(part));
        }

        SAXReader saxreader = new SAXReader();
        InputStream in = drawingPart.getInputStream();
        Document drawingDoc = saxreader.read(in);
        in.close();

        getCellAnchors(control, zipPackage, drawingPart, drawingDoc.getRootElement());

        dispose();
    }

    private void getCellAnchors(IControl control, ZipPackage zipPackage, PackagePart drawingPart,
                                Element root) throws Exception {
        if (root == null || !root.hasContent()) {
            return;
        }

        @SuppressWarnings("unchecked")
        Iterator<Element> iter = root.elementIterator();
        Element shapeElement;
        CellAnchor anchor = null;
        while (iter.hasNext()) {
            shapeElement = iter.next();

            if (shapeElement.getName().equalsIgnoreCase("twoCellAnchor")) {
                anchor = getTwoCellAnchor(shapeElement);
            } else if (shapeElement.getName().equalsIgnoreCase("oneCellAnchor")) {
                anchor = getOneCellAnchor(shapeElement);
            }

            for (Iterator<?> it = shapeElement.elementIterator(); it.hasNext(); ) {
                processShape(control, zipPackage, drawingPart, (Element) it.next(), null, 1.0f, 1.0f, ModelUtil.instance().getCellAnchor(sheet, anchor));
            }
        }
    }

    private AnchorPoint getCellAnchor(Element cellAnchorElement) {
        if (cellAnchorElement == null) {
            return null;
        }

        AnchorPoint cellAnchor = new AnchorPoint();

        Element ele = cellAnchorElement.element("col");
        int val = Integer.parseInt(ele.getText());
        cellAnchor.setColumn((short) val);

        ele = cellAnchorElement.element("colOff");
        long off = Long.parseLong(ele.getText());
        cellAnchor.setDX((int) (off * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH));

        ele = cellAnchorElement.element("row");
        val = Integer.parseInt(ele.getText());
        cellAnchor.setRow(val);

        ele = cellAnchorElement.element("rowOff");
        off = Long.parseLong(ele.getText());
        cellAnchor.setDY((int) (off * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH));

        return cellAnchor;
    }

    private CellAnchor getTwoCellAnchor(Element cellAnchor) {
        CellAnchor anchor = new CellAnchor(CellAnchor.TWOCELLANCHOR);

        anchor.setStart(getCellAnchor(cellAnchor.element("from")));

        anchor.setEnd(getCellAnchor(cellAnchor.element("to")));

        return anchor;
    }

    private CellAnchor getOneCellAnchor(Element cellAnchor) {

        AnchorPoint from = getCellAnchor(cellAnchor.element("from"));

        CellAnchor anchor = new CellAnchor(CellAnchor.ONECELLANCHOR);

        anchor.setStart(from);

        Element ext = cellAnchor.element("ext");

        anchor.setWidth((int) (Long.parseLong(ext.attributeValue("cx")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH));

        anchor.setHeight((int) (Long.parseLong(ext.attributeValue("cy")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH));

        return anchor;
    }

    private TextBox getTextBoxData(IControl control, Element sp, Rectangle rect) {
        TextBox tb = new TextBox();

        SectionElement secElem = new SectionElement();

        secElem.setStartOffset(0);
        tb.setElement(secElem);

        IAttributeSet attr = secElem.getAttribute();

        AttrManage.instance().setPageWidth(attr, Math.round(rect.width * KeyKt.PIXEL_TO_TWIPS));

        AttrManage.instance().setPageHeight(attr, Math.round(rect.height * KeyKt.PIXEL_TO_TWIPS));

        Element temp = sp.element("txBody");
        if (temp != null) {
            IAttributeSet attrLayout = new AttributeSetImpl();

            AttrManage.instance().setPageMarginLeft(attrLayout, Math.round(ShapeKit.DefaultMargin_Pixel * KeyKt.PIXEL_TO_TWIPS * 2));

            AttrManage.instance().setPageMarginRight(attrLayout, Math.round(ShapeKit.DefaultMargin_Pixel * KeyKt.PIXEL_TO_TWIPS * 2));

            AttrManage.instance().setPageMarginTop(attrLayout, Math.round(ShapeKit.DefaultMargin_Pixel * KeyKt.PIXEL_TO_TWIPS));

            AttrManage.instance().setPageMarginBottom(attrLayout, Math.round(ShapeKit.DefaultMargin_Pixel * KeyKt.PIXEL_TO_TWIPS));

            Element bodyPr = temp.element("bodyPr");
            SectionAttr.instance().setSectionAttribute(bodyPr, attr, attrLayout, null, false);
            if (bodyPr != null) {

                String value = bodyPr.attributeValue("wrap");
                tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
            }

            int offset = processParagraph(control, secElem, temp);
            secElem.setEndOffset(offset);
        }

        tb.setBounds(rect);

        if (tb.getElement() != null && tb.getElement().getText(null) != null
                && tb.getElement().getText(null).length() > 0
                && !"\n".equals(tb.getElement().getText(null))) {
            ReaderKit.instance().processRotation(sp.element("spPr"), tb);
            return tb;
        }
        return null;
    }

    private int processParagraph(IControl control, SectionElement secElem, Element txBody) {
        offset = 0;
        List<Element> ps = txBody.elements("p");
        for (Element p : ps) {
            Element pPr = p.element("pPr");

            ParagraphElement paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            IAttributeSet attrLayout = null;

            ParaAttr.instance().setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, -1, -1, 0, false, false);

            paraElem = processRun(control, secElem, paraElem, p, attrLayout);

            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
        }
        return offset;
    }

    private ParagraphElement processRun(IControl control, SectionElement secElem, ParagraphElement paraElem, Element p,
                                        IAttributeSet attrLayout) {

        List<Element> rs = p.elements("r");
        LeafElement leaf = null;

        if (rs.size() == 0) {
            leaf = new LeafElement("\n");
            Element ele = p.element("pPr");
            if (ele != null) {
                ele = ele.element("rPr");
                if (ele != null) {

                    RunAttr.instance().setRunAttribute(sheet, ele, leaf.getAttribute(), attrLayout);
                }
            }
            leaf.setStartOffset(offset);
            offset++;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return paraElem;
        }

        for (Element r : rs) {
            if (r.getName().equalsIgnoreCase("r")) {
                Element t = r.element("t");
                if (t != null) {
                    String text = t.getText();
                    int len = text.length();
                    if (len >= 0) {
                        leaf = new LeafElement(text);

                        RunAttr.instance().setRunAttribute(sheet, r.element("rPr"), leaf.getAttribute(), attrLayout);

                        leaf.setStartOffset(offset);
                        offset += len;

                        leaf.setEndOffset(offset);
                        paraElem.appendLeaf(leaf);
                    }
                }
            } else if (r.getName().equalsIgnoreCase("br")) {

                if (leaf != null) {
                    leaf.setText(leaf.getText(null) + "\n");
                    offset++;
                }
                paraElem.setEndOffset(offset);
                secElem.appendParagraph(paraElem, WPModelConstant.MAIN);

                paraElem = new ParagraphElement();
                paraElem.setStartOffset(offset);
                attrLayout = null;
                Element pPr = p.element("pPr");
                ParaAttr.instance().setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, -1, -1, 0, false, false);
            }

        }
        if (leaf != null) {
            leaf.setText(leaf.getText(null) + "\n");
            offset++;
        }
        return paraElem;
    }

    private PictureShape getImageData(Element sp, Rectangle rect) {

        Element ele = sp.element("blipFill");
        if (ele == null) {
            return null;
        }

        PictureEffectInfo effectInfor = PictureEffectInfoFactory.getPictureEffectInfor(ele);

        ele = ele.element("blip");
        if (ele != null && ele.attributeValue("embed") != null) {
            String id = ele.attributeValue("embed");
            if (id != null && drawingList.get(ele.attributeValue("embed")) != null) {
                PictureShape picShape = new PictureShape();

                int index = drawingList.get(ele.attributeValue("embed"));
                picShape.setBounds(rect);
                picShape.setPictureIndex(index);
                picShape.setPictureEffectInfor(effectInfor);
                ReaderKit.instance().processRotation(sp.element("spPr"), picShape);
                return picShape;
            }
        }
        return null;
    }

    private AChart getChart(Element chart, Rectangle rect) {
        if (chart != null) {
            String id = chart.attributeValue("id");
            if (id != null) {
                AChart chartData = new AChart();
                chartData.setBounds(rect);
                chartData.setAChart(chartList.get(id));
                return chartData;
            }
        }
        return null;
    }

    private SmartArt getSmartArt(Element graphicData, Rectangle rect) {
        try {
            if (graphicData != null) {
                Element relIds = graphicData.element("relIds");
                String cs = relIds.attributeValue("dm");
                int id = Integer.parseInt(cs.substring("rId".length()));
                if (cs != null) {
                    SmartArt smartArt = smartArtList.get(cs);
                    smartArt.setBounds(rect);
                    return smartArt;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void processShape(IControl control, ZipPackage zipPackage, PackagePart drawingPart,
                              Element sp, GroupShape parent, float zoomX, float zoomY, Rectangle rect2) throws Exception {
        Rectangle rect = null;
        String name = sp.getName();
        if (name.equals("grpSp")) {

            GroupShape groupShape = null;
            float[] zoomXY = null;
            Element grpSpPr = sp.element("grpSpPr");
            if (grpSpPr != null) {
                Rectangle childRect = null;
                rect = ReaderKit.instance().getShapeAnchor(grpSpPr.element("xfrm"), zoomX, zoomY);
                if (rect.width == 0 || rect.height == 0) {
                    return;
                }
                rect = processGrpSpRect(parent, rect);

                zoomXY = ReaderKit.instance().getAnchorFitZoom(grpSpPr.element("xfrm"));
                childRect = ReaderKit.instance().getChildShapeAnchor(grpSpPr.element("xfrm"), zoomXY[0] * zoomX, zoomXY[1] * zoomY);

                groupShape = new GroupShape();
                groupShape.setBounds(rect);
                groupShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);
                ReaderKit.instance().processRotation(grpSpPr, groupShape);
            }

            Rectangle groupshapeRect = null;
            if (parent == null) {
                groupshapeRect = rect2;
            } else {
                groupshapeRect = new Rectangle();
                Rectangle parRect = parent.getBounds();
                groupshapeRect.x = rect2.x + (rect.x - parRect.x) * rect2.width / parRect.width;
                groupshapeRect.y = rect2.y + (rect.y - parRect.y) * rect2.height / parRect.height;
                groupshapeRect.width = rect2.width * rect.width / parRect.width;
                groupshapeRect.height = rect2.height * rect.height / parRect.height;
            }

            for (Iterator<?> it = sp.elementIterator(); it.hasNext(); ) {
                processShape(control, zipPackage, drawingPart, (Element) it.next(), groupShape, zoomXY[0] * zoomX, zoomXY[1] * zoomY, groupshapeRect);
            }

            groupShape.setBounds(groupshapeRect);

            if (parent == null) {
                sheet.appendShapes(groupShape);
            } else {
                parent.appendShapes(groupShape);
            }
        } else if (name.equals("AlternateContent")) {
            Element choice = sp.element("Choice");
            if (choice != null) {
                for (Iterator<?> it = choice.elementIterator(); it.hasNext(); ) {
                    processShape(control, zipPackage, drawingPart, (Element) it.next(), parent, zoomX, zoomY, null);
                }
            }
        } else {
            AbstractShape shape = null;
            if (parent == null) {
                rect = rect2;
            } else {
                Element temp = sp.element("spPr");
                if (temp == null) {
                    return;
                }

                rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), zoomX, zoomY);
                rect = processGrpSpRect(parent, rect);

                Rectangle parRect = parent.getBounds();
                rect.x = rect2.x + (rect.x - parRect.x) * rect2.width / parRect.width;
                rect.y = rect2.y + (rect.y - parRect.y) * rect2.height / parRect.height;
                rect.width = rect2.width * rect.width / parRect.width;
                rect.height = rect2.height * rect.height / parRect.height;
            }

            if (sheet.getSheetType() == Sheet.TYPE_WORKSHEET && rect == null) {
                return;
            }

            if (name.equals("sp") || name.equals("cxnSp")) {
                shape = AutoShapeDataKit.getAutoShape(control, zipPackage, drawingPart, sp,
                        rect, SchemeColorUtil.getSchemeColor(sheet.getWorkbook()), KeyKt.APPLICATION_TYPE_SS);
                if (shape != null) {
                    if (parent == null) {
                        sheet.appendShapes(shape);
                    } else {
                        parent.appendShapes(shape);
                    }
                }

                TextBox textBox = getTextBoxData(control, sp, rect);
                if (textBox != null) {
                    if (parent == null) {
                        sheet.appendShapes(textBox);
                    } else {
                        parent.appendShapes(textBox);
                    }
                }
            } else if (name.equals("pic")) {
                shape = getImageData(sp, rect);
                if (shape != null) {
                    AutoShapeDataKit.processPictureShape(control, zipPackage, drawingPart,
                            sp.element("spPr"), SchemeColorUtil.getSchemeColor(sheet.getWorkbook()), (PictureShape) shape);

                    if (parent == null) {
                        sheet.appendShapes(shape);
                    } else {
                        parent.appendShapes(shape);
                    }
                }
            } else if (name.equals("graphicFrame")) {
                Element graphic = sp.element("graphic");
                if (graphic != null) {
                    Element graphicData = graphic.element("graphicData");
                    if (graphicData != null && graphicData.attribute("uri") != null) {
                        String uri = graphicData.attributeValue("uri");
                        if (uri.equals(PackageRelationshipTypes.CHART_TYPE)) {
                            shape = getChart(graphicData.element("chart"), rect);
                        } else if (uri.equals(PackageRelationshipTypes.DIAGRAM_TYPE)) {
                            shape = getSmartArt(graphicData, rect);
                        }
                        if (shape != null) {
                            ReaderKit.instance().processRotation(sp.element("spPr"), shape);
                            if (parent == null) {
                                sheet.appendShapes(shape);
                            } else {
                                parent.appendShapes(shape);
                            }
                        }
                    }
                }
            }

            if (shape != null && Math.abs(shape.getRotation()) > 1) {
                rect = ModelUtil.processRect(shape.getBounds(), shape.getRotation());
                shape.setBounds(rect);
            }
        }
    }

    private Rectangle processGrpSpRect(GroupShape parent, Rectangle rect) {
        if (parent != null) {
            rect.x += parent.getOffX();
            rect.y += parent.getOffY();
        }
        return rect;
    }

    public void processOLEPicture(IControl control, ZipPackage zipPackage, PackagePart sheetPart,
                                  Sheet sheet, Element oleObjects) throws Exception {
        this.sheet = sheet;

        if (oleObjects != null) {
            List<Element> oles = oleObjects.elements("oleObject");
            for (Element oleObject : oles) {
                String spid = oleObject.attributeValue("shapeId");
                if (spid != null) {
                    PackagePart picPart = PictureReader.instance().getOLEPart(zipPackage, sheetPart, spid, true);
                    if (picPart != null) {
                        CellAnchor anchor = PictureReader.instance().getExcelShapeAnchor(spid);
                        if (anchor != null) {
                            PictureShape picShape = new PictureShape();
                            picShape.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
                            picShape.setBounds(ModelUtil.instance().getCellAnchor(sheet, anchor));
                            sheet.appendShapes(picShape);
                        }
                    }
                }
            }
        }
    }

    private void dispose() {
        sheet = null;

        if (drawingList != null) {
            drawingList.clear();
            drawingList = null;
        }

        if (chartList != null) {
            chartList.clear();
            chartList = null;
        }

        if (smartArtList != null) {
            smartArtList.clear();
            smartArtList = null;
        }
    }
}
