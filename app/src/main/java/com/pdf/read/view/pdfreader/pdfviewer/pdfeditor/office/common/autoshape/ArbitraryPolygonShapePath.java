package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ArbitraryPolygonShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.Arrow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public class ArbitraryPolygonShapePath {
    public static void processArbitraryPolygonShape(ArbitraryPolygonShape arbitraryPolygonShape, Element sp, BackgroundAndFill fill, boolean border, BackgroundAndFill lineFill, Element ln, Rectangle rect) throws Exception {
        if (arbitraryPolygonShape == null) {
            return;
        }

        int lineWidth = 1;
        if (ln != null) {

            if (ln.attributeValue("w") != null) {
                lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
            }
        }

        Line line = arbitraryPolygonShape.createLine();
        line.setBackgroundAndFill(lineFill);
        line.setLineWidth(lineWidth);

        arbitraryPolygonShape.setBounds(rect);

        Element spPr = sp.element("spPr");
        List<Element> pathElements = spPr.element("custGeom").element("pathLst").elements("path");

        PointF headArrowTailCenter = null;
        PointF tailArrowTailCenter = null;
        ExtendPath pathExtend_Header = null;
        ExtendPath pathExtend_Tail = null;

        if (pathElements.size() == 1 && ln != null) {
            String width = pathElements.get(0).attributeValue("w");
            String height = pathElements.get(0).attributeValue("h");
            if (width != null && height != null) {
                float w = Integer.parseInt(width) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float h = Integer.parseInt(height) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                lineWidth *= Math.min(w / rect.width, h / rect.height);
            }

            Element temp = ln.element("headEnd");
            if (temp != null && temp.attribute("type") != null) {
                if (!"none".equals(temp.attributeValue("type"))) {
                    byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                    arbitraryPolygonShape.createStartArrow(arrowType,
                            getArrowSize(temp.attributeValue("w")),
                            getArrowSize(temp.attributeValue("len")));

                    Element pathElement1 = pathElements.get(0);
                    ArrowPathAndTail arrowPathAndTail = getPathHeadArrowPath(arbitraryPolygonShape.getStartArrow(), lineWidth,
                            pathElement1);
                    if (arrowPathAndTail != null) {
                        Path path1 = arrowPathAndTail.getArrowPath();
                        headArrowTailCenter = arrowPathAndTail.getArrowTailCenter();

                        if (path1 != null) {

                            pathExtend_Header = new ExtendPath();
                            pathExtend_Header.setArrowFlag(true);
                            pathExtend_Header.setPath(path1);
                            if (fill != null || border) {
                                if (border &&
                                        (pathElement1.attribute("stroke") == null
                                                || Integer.parseInt(pathElement1.attributeValue("stroke")) != 0)) {
                                    if (arrowType != Arrow.Arrow_Arrow) {
                                        pathExtend_Header.setBackgroundAndFill(lineFill);
                                    } else {
                                        pathExtend_Header.setLine(line);
                                    }
                                } else if (fill != null) {
                                    pathExtend_Header.setBackgroundAndFill(fill);
                                }
                            }
                        }
                    }
                }
            }

            temp = ln.element("tailEnd");
            if (temp != null && temp.attribute("type") != null) {
                if (!"none".equals(temp.attributeValue("type"))) {
                    byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                    arbitraryPolygonShape.createEndArrow(arrowType,
                            getArrowSize(temp.attributeValue("w")),
                            getArrowSize(temp.attributeValue("len")));

                    Element pathElement1 = pathElements.get(0);
                    ArrowPathAndTail arrowPathAndTail = getPathTailArrowPath(arbitraryPolygonShape.getEndArrow(), lineWidth,
                            pathElement1);
                    if (arrowPathAndTail != null) {
                        Path path1 = arrowPathAndTail.getArrowPath();
                        tailArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                        if (path1 != null) {

                            pathExtend_Tail = new ExtendPath();
                            pathExtend_Tail.setArrowFlag(true);
                            pathExtend_Tail.setPath(path1);
                            if (fill != null || border) {
                                if (border &&
                                        (pathElement1.attribute("stroke") == null
                                                || Integer.parseInt(pathElement1.attributeValue("stroke")) != 0)) {
                                    if (arrowType != Arrow.Arrow_Arrow) {
                                        pathExtend_Tail.setBackgroundAndFill(lineFill);
                                    } else {
                                        pathExtend_Tail.setLine(line);
                                    }
                                } else if (fill != null) {
                                    pathExtend_Tail.setBackgroundAndFill(fill);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int j = 0; j < pathElements.size(); j++) {
            ExtendPath pathExtend = new ExtendPath();
            Path path = getArrowPath(arbitraryPolygonShape, pathElements.get(j), rect, fill, border, headArrowTailCenter, tailArrowTailCenter);

            pathExtend.setPath(path);
            Element pathElement = pathElements.get(j);
            String width = pathElement.attributeValue("w");
            String height = pathElement.attributeValue("h");
            Matrix m = new Matrix();
            if (width != null && height != null) {
                float w = Integer.parseInt(width) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float h = Integer.parseInt(height) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                m.postScale(rect.width / w, rect.height / h);
                path.transform(m);
            }

            if (fill != null || border) {
                if (fill != null) {
                    if (pathElement.attribute("fill") != null
                            && "none".equals(pathElement.attributeValue("fill"))) {
                        pathExtend.setBackgroundAndFill(null);
                    } else {
                        pathExtend.setBackgroundAndFill(fill);
                    }
                }

                if (border) {
                    if (pathElement.attribute("stroke") != null
                            && Integer.parseInt(pathElement.attributeValue("stroke")) == 0) {
                        pathExtend.setLine(false);
                    } else {
                        pathExtend.setLine(line);
                    }
                }
            }

            arbitraryPolygonShape.appendPath(pathExtend);

            if (pathExtend_Header != null) {
                pathExtend_Header.getPath().transform(m);
                arbitraryPolygonShape.appendPath(pathExtend_Header);
            }

            if (pathExtend_Tail != null) {
                pathExtend_Tail.getPath().transform(m);
                arbitraryPolygonShape.appendPath(pathExtend_Tail);
            }

        }

    }

    private static Path getArrowPath(ArbitraryPolygonShape arbitraryPolygonShape, Element pathElement, Rectangle rect, BackgroundAndFill fill, boolean border, PointF headerArrowTailCenter, PointF tailArrowTailCenter) {
        Path path = new Path();
        boolean pathClosed = false;
        List<Element> eleList = pathElement.elements();
        int cnt = eleList.size();
        Element e = eleList.get(cnt - 1);

        for (int i = 0; i < cnt; i++) {
            e = eleList.get(i);
            if (headerArrowTailCenter != null
                    && i == 0
                    && e.getName().equals("moveTo")) {
                pathClosed = false;
                headerArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(e.element("pt"), headerArrowTailCenter, arbitraryPolygonShape.getStartArrowType());

                path.moveTo(headerArrowTailCenter.x, headerArrowTailCenter.y);
            } else if (tailArrowTailCenter != null && i == cnt - 1) {
                if (e.getName().equals("lnTo")) {
                    tailArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(e.element("pt"), tailArrowTailCenter, arbitraryPolygonShape.getEndArrowType());
                    path.lineTo(tailArrowTailCenter.x, tailArrowTailCenter.y);
                } else if (e.getName().equals("quadBezTo")) {
                    List ptList = e.elements();
                    if (ptList.size() != 2) {
                        break;
                    }

                    tailArrowTailCenter = LineArrowPathBuilder.getReferencedPosition((Element) ptList.get(1), tailArrowTailCenter, arbitraryPolygonShape.getEndArrowType());

                    path.quadTo(Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            tailArrowTailCenter.x,
                            tailArrowTailCenter.y);
                } else if (e.getName().equals("cubicBezTo")) {
                    List ptList = e.elements();
                    if (ptList.size() != 3) {
                        break;
                    }

                    tailArrowTailCenter = LineArrowPathBuilder.getReferencedPosition((Element) ptList.get(2), tailArrowTailCenter, arbitraryPolygonShape.getEndArrowType());

                    path.cubicTo(Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            tailArrowTailCenter.x,
                            tailArrowTailCenter.y);
                } else if (e.getName().equals("arcTo")) {
                    float wR = Integer.parseInt(e.attributeValue("wR")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                    float hR = Integer.parseInt(e.attributeValue("hR")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                    path.arcTo(new RectF((float) rect.getCenterX() - wR - rect.x, (float) rect.getCenterY() - hR - rect.y, (float) rect.getCenterX() + wR - rect.x, (float) rect.getCenterY() + hR - rect.y),
                            Integer.parseInt(e.attributeValue("stAng")) / 60000f,
                            Integer.parseInt(e.attributeValue("swAng")) / 60000f);
                }
            } else {
                if (e.getName().equals("moveTo")) {
                    pathClosed = false;
                    e = e.element("pt");
                    path.moveTo(Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                } else if (e.getName().equals("lnTo")) {
                    e = e.element("pt");
                    path.lineTo(Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                } else if (e.getName().equals("quadBezTo")) {
                    List ptList = e.elements();
                    if (ptList.size() != 2) {
                        break;
                    }
                    path.quadTo(Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                } else if (e.getName().equals("cubicBezTo")) {
                    List ptList = e.elements();
                    if (ptList.size() != 3) {
                        break;
                    }
                    path.cubicTo(Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(2)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH,
                            Integer.parseInt(((Element) ptList.get(2)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                } else if (e.getName().equals("arcTo")) {
                    float wR = Integer.parseInt(e.attributeValue("wR")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                    float hR = Integer.parseInt(e.attributeValue("hR")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                    path.arcTo(new RectF((float) rect.getCenterX() - wR - rect.x, (float) rect.getCenterY() - hR - rect.y, (float) rect.getCenterX() + wR - rect.x, (float) rect.getCenterY() + hR - rect.y),
                            Integer.parseInt(e.attributeValue("stAng")) / 60000f,
                            Integer.parseInt(e.attributeValue("swAng")) / 60000f);
                } else if (e.getName().equals("close")) {
                    pathClosed = true;
                    path.close();
                }
            }
        }

        return path;
    }

    public static ArrowPathAndTail getPathHeadArrowPath(Arrow arrow, int lineWidth, Element pathElement) {
        List<Element> eleList = pathElement.elements();
        if (eleList == null || eleList.size() < 2) {
            return null;
        }

        Element e = eleList.get(0).element("pt");

        ArrowPathAndTail path = null;
        float p0X = Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
        float p0Y = Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
        float p1X = 0f, p1Y = 0f;

        e = eleList.get(1);
        if (e.getName().equals("lnTo")) {
            e = e.element("pt");
            p1X = Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
            p1Y = Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

            path = LineArrowPathBuilder.getDirectLineArrowPath(p1X, p1Y, p0X, p0Y, arrow, lineWidth);

        } else if (e.getName().equals("quadBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 2) {
                float ctrX1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1X = Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1Y = Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                path = LineArrowPathBuilder.getQuadBezArrowPath(p1X, p1Y, ctrX1, ctrY1, p0X, p0Y, arrow, lineWidth);
            }

        } else if (e.getName().equals("cubicBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 3) {
                float ctrX1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrX2 = Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY2 = Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1X = Integer.parseInt(((Element) ptList.get(2)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1Y = Integer.parseInt(((Element) ptList.get(2)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                path = LineArrowPathBuilder.getCubicBezArrowPath(p1X, p1Y, ctrX2, ctrY2, ctrX1, ctrY1, p0X, p0Y, arrow, lineWidth);
            }
        }

        return path;
    }

    public static ArrowPathAndTail getPathTailArrowPath(Arrow arrow, int lineWidth, Element pathElement) {
        List<Element> eleList = pathElement.elements();
        ArrowPathAndTail arrowPathAndTail = null;

        int cnt = 0;
        if (eleList == null || (cnt = eleList.size()) < 2 || eleList.get(cnt - 1).getName().equals("close")) {
            return null;
        }

        Element e = eleList.get(cnt - 2);
        float p0X = 0f, p0Y = 0;
        if (e.getName().equals("lnTo")) {
            e = e.element("pt");
            p0X = Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
            p0Y = Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
        } else if (e.getName().equals("quadBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 2) {
                p0X = Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p0Y = Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
            }

        } else if (e.getName().equals("cubicBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 3) {
                p0X = Integer.parseInt(((Element) ptList.get(2)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p0Y = Integer.parseInt(((Element) ptList.get(2)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
            }
        }

        e = eleList.get(cnt - 1);
        float p1X = 0f, p1Y = 0;
        if (e.getName().equals("lnTo")) {
            e = e.element("pt");
            p1X = Integer.parseInt(e.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
            p1Y = Integer.parseInt(e.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

            arrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(p0X, p0Y, p1X, p1Y, arrow, lineWidth);
        } else if (e.getName().equals("quadBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 2) {
                float ctrX1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1X = Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1Y = Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                arrowPathAndTail = LineArrowPathBuilder.getQuadBezArrowPath(p0X, p0Y, ctrX1, ctrY1, p1X, p1Y, arrow, lineWidth);
            }

        } else if (e.getName().equals("cubicBezTo")) {
            List ptList = e.elements();
            if (ptList.size() == 3) {
                float ctrX1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY1 = Integer.parseInt(((Element) ptList.get(0)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrX2 = Integer.parseInt(((Element) ptList.get(1)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                float ctrY2 = Integer.parseInt(((Element) ptList.get(1)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1X = Integer.parseInt(((Element) ptList.get(2)).attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
                p1Y = Integer.parseInt(((Element) ptList.get(2)).attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

                arrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(p0X, p0Y, ctrX1, ctrY1, ctrX2, ctrY2, p1X, p1Y, arrow, lineWidth);
            }
        }
        return arrowPathAndTail;
    }

    public static int getArrowSize(String size) {
        if (size == null || size.equals("med")) {
            return 1;
        }
        if (size.equals("sm")) {
            return 0;
        } else if (size.equals("lg")) {
            return 2;
        } else {
            return 1;
        }
    }
}
