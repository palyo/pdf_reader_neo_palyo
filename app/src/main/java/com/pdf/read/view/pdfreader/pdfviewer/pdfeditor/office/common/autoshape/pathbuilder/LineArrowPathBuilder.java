package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder;

import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.RectF;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.Arrow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;

public class LineArrowPathBuilder {
    private final static int SMALL = 5;
    private final static int MEDIUM = 9;
    private final static int LARGE = 13;

    static PointF p = new PointF();

    private static int getArrowWidth(Arrow arrow, int lineWidth) {
        if (lineWidth < 3) {
            return MEDIUM;
        } else {
            return lineWidth * 3;
        }
    }

    public static int getArrowLength(Arrow arrow, int lineWidth) {
        if (lineWidth < 3) {
            return MEDIUM;
        } else {
            return lineWidth * 3;
        }
    }

    public static ArrowPathAndTail getDirectLineArrowPath(float startX, float startY, float endX, float endY, Arrow arrow, int lineWidth) {
        return getDirectLineArrowPath(startX, startY, endX, endY, arrow, lineWidth, 1.0f);
    }

    public static ArrowPathAndTail getDirectLineArrowPath(float startX, float startY, float endX, float endY, Arrow arrow, int lineWidth, float zoom) {
        ArrowPathAndTail arrowPathAndTail = new ArrowPathAndTail();

        int width = getArrowWidth(arrow, lineWidth);
        int length = getArrowLength(arrow, lineWidth);

        float ratio = (float) (length * zoom / Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)));
        startY = endY - (endY - startY) * ratio;
        startX = endX - (endX - startX) * ratio;
        arrowPathAndTail.setArrowPath(buildArrowPath(startX, startY, endX, endY, width * zoom, length * zoom, arrow.getType()));
        arrowPathAndTail.setArrowTailCenter(startX, startY);
        return arrowPathAndTail;
    }

    public static ArrowPathAndTail getQuadBezArrowPath(float startX, float startY, float ctrlX, float ctrlY, float endX, float endY, Arrow arrow, int lineWidth) {
        return getQuadBezArrowPath(startX, startY,
                ctrlX, ctrlY,
                endX, endY,
                arrow, lineWidth, 1.f);
    }

    public static ArrowPathAndTail getQuadBezArrowPath(float startX, float startY, float ctrlX, float ctrlY, float endX, float endY, Arrow arrow, int lineWidth, float zoom) {
        ArrowPathAndTail arrowPathAndTail = new ArrowPathAndTail();

        float width = getArrowWidth(arrow, lineWidth) * zoom;
        float length = getArrowLength(arrow, lineWidth) * zoom;

        float r = 0.9f;
        float offRatio = 0.01f;
        PointF end = quadBezComputePoint(startX, startY, ctrlX, ctrlY, endX, endY, r);
        int dist = (int) Math.round(Math.sqrt(Math.pow(end.x - endX, 2) + Math.pow(end.y - endY, 2)));
        Boolean inc = null;
        while (Math.abs(dist - length) > 1 && r < 1.0f && r > 0) {
            if (dist - length > 1) {
                r += offRatio;
                if (inc != null && !inc) {
                    offRatio *= 0.1;
                    r -= offRatio;
                }
                inc = true;
            } else {
                r -= offRatio;
                if (inc != null && inc) {
                    offRatio *= 0.1;
                    r += offRatio;
                }

                inc = false;
            }

            end = quadBezComputePoint(startX, startY, ctrlX, ctrlY, endX, endY, r);
            dist = (int) Math.round(Math.sqrt((end.x - endX) * (end.x - endX) + (end.y - endY) * (end.y - endY)));
        }

        arrowPathAndTail.setArrowPath(buildArrowPath(end.x, end.y, endX, endY, width, length, arrow.getType()));
        arrowPathAndTail.setArrowTailCenter(end.x, end.y);
        return arrowPathAndTail;
    }

    public static ArrowPathAndTail getCubicBezArrowPath(float startX, float startY, float ctrl1X, float ctrl1Y, float ctrl2X, float ctrl2Y, float endX, float endY, Arrow arrow, int lineWidth) {
        return getCubicBezArrowPath(startX, startY,
                ctrl1X, ctrl1Y,
                ctrl2X, ctrl2Y,
                endX, endY,
                arrow, lineWidth, 1.f);
    }

    public static ArrowPathAndTail getCubicBezArrowPath(float startX, float startY, float ctrl1X, float ctrl1Y, float ctrl2X, float ctrl2Y, float endX, float endY, Arrow arrow, int lineWidth, float zoom) {
        ArrowPathAndTail arrowPathAndTail = new ArrowPathAndTail();

        int width = getArrowWidth(arrow, lineWidth);
        int length = getArrowLength(arrow, lineWidth);

        float r = 0.9f;
        float offRatio = 0.01f;
        PointF end = cubicBezComputePoint(startX, startY, ctrl1X, ctrl1Y, ctrl2X, ctrl2Y, endX, endY, r);
        int dist = (int) Math.round(Math.sqrt(Math.pow(end.x - endX, 2) + Math.pow(end.y - endY, 2)));
        Boolean inc = null;
        while (Math.abs(dist - length) > 1 && r < 1.0f && r > 0) {
            if (dist - length > 1) {
                r += offRatio;
                if (inc != null && !inc) {
                    offRatio *= 0.1;
                    r -= offRatio;
                }
                inc = true;
            } else {
                r -= offRatio;
                if (inc != null && inc) {
                    offRatio *= 0.1;
                    r += offRatio;
                }

                inc = false;
            }

            end = cubicBezComputePoint(startX, startY, ctrl1X, ctrl1Y, ctrl2X, ctrl2Y, endX, endY, r);
            dist = (int) Math.round(Math.sqrt((end.x - endX) * (end.x - endX) + (end.y - endY) * (end.y - endY)));
        }

        arrowPathAndTail.setArrowPath(buildArrowPath(end.x, end.y, endX, endY, width, length, arrow.getType()));
        arrowPathAndTail.setArrowTailCenter(end.x, end.y);
        return arrowPathAndTail;
    }

    private static PointF quadBezComputePoint(float startX, float startY, float ctrlX, float ctrlY, float endX, float endY, float fU) {
        float fBlend;
        float f1subu = 1.0f - fU;

        fBlend = f1subu * f1subu;
        p.x = fBlend * startX;
        p.y = fBlend * startY;

        fBlend = 2 * fU * f1subu;
        p.x += fBlend * ctrlX;
        p.y += fBlend * ctrlY;

        fBlend = fU * fU;
        p.x += fBlend * endX;
        p.y += fBlend * endY;

        return p;
    }

    private static PointF cubicBezComputePoint(float startX, float startY, float ctrl1X, float ctrl1Y, float ctrl2X, float ctrl2Y, float endX, float endY, float fU) {
        PointF p = new PointF();
        float fBlend;
        float f1subu = 1.0f - fU;

        fBlend = f1subu * f1subu * f1subu;
        p.x = fBlend * startX;
        p.y = fBlend * startY;

        fBlend = 3 * fU * f1subu * f1subu;
        p.x += fBlend * ctrl1X;
        p.y += fBlend * ctrl1Y;

        fBlend = 3 * fU * fU * f1subu;
        p.x += fBlend * ctrl2X;
        p.y += fBlend * ctrl2Y;

        fBlend = fU * fU * fU;
        p.x += fBlend * endX;
        p.y += fBlend * endY;

        return p;
    }

    private static Path buildArrowPath(float startX, float startY, float endX, float endY, float zoom) {
        Path path = new Path();
        path.moveTo(endX, endY);

        float pBaseX, pBaseY;
        float vecLineX, vecLineY;
        float vecLeftX, vecLeftY;
        float fLength;
        float th;
        float ta;

        int nWidth = (int) (15 * zoom);
        float fTheta = 1.0f;

        vecLineX = endX - startX;
        vecLineY = endY - startY;

        vecLeftX = -vecLineY;
        vecLeftY = vecLineX;

        fLength = (float) Math.sqrt(vecLineX * vecLineX + vecLineY * vecLineY);
        th = nWidth / (2.0f * fLength);
        ta = (float) (nWidth / (2.0f * (Math.tan(fTheta) / 2.0f) * fLength));

        pBaseX = (int) (endX + -ta * vecLineX);
        pBaseY = (int) (endY + -ta * vecLineY);

        path.lineTo(pBaseX + th * vecLeftX, pBaseY + th * vecLeftY / 2);
        path.lineTo(pBaseX + -th * vecLeftX, pBaseY + -th * vecLeftY / 2);

        path.close();

        return path;
    }

    private static Path buildArrowPath(float startX, float startY, float endX, float endY, float arrowWidth, float arrowLength, byte arrowType) {
        switch (arrowType) {
            case Arrow.Arrow_Triangle:
                return buildTriangleArrowPath(startX, startY, endX, endY,
                        arrowWidth);

            case Arrow.Arrow_Arrow:
                return buildArrowArrowPath(startX, startY, endX, endY,
                        arrowWidth);

            case Arrow.Arrow_Diamond:
                return buildDiamondArrowPath(startX, startY, endX, endY,
                        arrowWidth, arrowLength);

            case Arrow.Arrow_Stealth:
                return buildStealthArrowPath(startX, startY, endX, endY,
                        arrowWidth, arrowLength);

            case Arrow.Arrow_Oval:
                return buildOvalArrowPath(endX, endY, arrowWidth, arrowLength);
        }

        return new Path();
    }

    private static Path buildTriangleArrowPath(float startX, float startY, float endX, float endY, float arrowWidth) {
        Path path = new Path();
        path.moveTo(endX, endY);

        if (endY == startY) {
            path.lineTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        } else if (endX == startX) {
            path.lineTo(startX - arrowWidth / 2.0f, startY);
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        } else {
            float k = (endY - startY) / (endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float) (arrowWidth / 2.0f * Math.cos(angle));
            float offy = (float) (arrowWidth / 2.0f * Math.sin(angle));

            path.lineTo(startX + offx, startY + offy);
            path.lineTo(startX - offx, startY - offy);
        }

        path.close();

        return path;
    }

    private static Path buildArrowArrowPath(float startX, float startY, float endX, float endY, float arrowWidth) {
        Path path = new Path();

        if (endY == startY) {
            path.moveTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(endX, endY);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        } else if (endX == startX) {
            path.moveTo(startX - arrowWidth / 2.0f, startY);
            path.lineTo(endX, endY);
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        } else {
            float k = (endY - startY) / (endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float) (arrowWidth / 2.0f * Math.cos(angle));
            float offy = (float) (arrowWidth / 2.0f * Math.sin(angle));

            path.moveTo(startX + offx, startY + offy);
            path.lineTo(endX, endY);
            path.lineTo(startX - offx, startY - offy);
        }

        return path;
    }

    private static Path buildDiamondArrowPath(float startX, float startY, float endX, float endY, float arrowWidth, float arrowLength) {
        Path path = new Path();
        if (endY == startY || endX == startX) {
            path.moveTo(endX - arrowLength / 2.0f, endY);
            path.lineTo(endX, endY - arrowWidth / 2.0f);
            path.lineTo(endX + arrowLength / 2.0f, endY);
            path.lineTo(endX, endY + arrowWidth / 2.0f);
        } else {
            float k = (endY - startY) / (endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float) (arrowLength / 2.0f * Math.cos(angle));
            float offy = (float) (arrowWidth / 2.0f * Math.sin(angle));

            path.moveTo(startX, startY);
            path.lineTo(endX + offx, endY + offy);
            path.lineTo(endX + (endX - startX), endY + (endY - startY));
            path.lineTo(endX - offx, endY - offy);
        }

        path.close();
        return path;
    }

    private static Path buildStealthArrowPath(float startX, float startY, float endX, float endY, float arrowWidth, float arrowLength) {
        Path path = new Path();
        path.moveTo(endX, endY);

        if (endY == startY) {
            path.lineTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(startX + (endX - startX) / 4.f, endY);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        } else if (endX == startX) {
            path.lineTo(startX - arrowWidth / 2.0f, startY);
            path.lineTo(startX, startY + (endY - startY) / 4.f);
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        } else {
            float k = (endY - startY) / (endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float) (arrowLength / 2.0f * Math.cos(angle));
            float offy = (float) (arrowWidth / 2.0f * Math.sin(angle));

            path.lineTo(startX + offx, startY + offy);
            path.lineTo(startX + (endX - startX) / 4.f, startY + (endY - startY) / 4.f);
            path.lineTo(startX - offx, startY - offy);
        }

        path.close();

        return path;
    }

    private static Path buildOvalArrowPath(float endX, float endY, float arrowWidth, float arrowLength) {
        Path path = new Path();
        path.addOval(new RectF(endX - arrowLength / 2.0f, endY - arrowWidth / 2.0f, endX + arrowLength / 2.0f, endY + arrowWidth / 2.0f),
                Direction.CCW);
        return path;
    }

    public static PointF getReferencedPosition(Element head, PointF tail, byte arrowType) {
        float x = Integer.parseInt(head.attributeValue("x")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;
        float y = Integer.parseInt(head.attributeValue("y")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH;

        switch (arrowType) {
            case Arrow.Arrow_Triangle:
                x = x * 0.2f + tail.x * 0.8f;
                y = y * 0.2f + tail.y * 0.8f;
                break;

            case Arrow.Arrow_Arrow:
                break;
            case Arrow.Arrow_Stealth:
                x = x * 0.3f + tail.x * 0.7f;
                y = y * 0.3f + tail.y * 0.7f;
                break;
            case Arrow.Arrow_Diamond:
                break;

        }
        return new PointF(x, y);
    }

    public static PointF getReferencedPosition(float headX, float headY, float tailX, float tailY, byte arrowType) {
        switch (arrowType) {
            case Arrow.Arrow_Triangle:
                headX = headX * 0.2f + tailX * 0.8f;
                headY = headY * 0.2f + tailY * 0.8f;
                break;

            case Arrow.Arrow_Arrow:
                break;
            case Arrow.Arrow_Stealth:
                headX = headX * 0.3f + tailX * 0.7f;
                headY = headY * 0.3f + tailY * 0.7f;
                break;
            case Arrow.Arrow_Diamond:
                break;

        }
        return new PointF(headX, headY);
    }
}
