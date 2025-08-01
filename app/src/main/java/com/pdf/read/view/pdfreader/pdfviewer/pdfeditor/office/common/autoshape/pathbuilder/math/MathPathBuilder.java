package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.math;

import android.graphics.Path;
import android.graphics.Rect;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AutoShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;

public class MathPathBuilder {
    private static final Path path = new Path();

    public static Path getMathPath(AutoShape shape, Rect rect) {
        path.reset();
        switch (shape.getShapeType()) {
            case ShapeTypes.MathPlus:
                return getMathPlusPath(shape, rect);

            case ShapeTypes.MathMinus:
                return getMathMinusPath(shape, rect);

            case ShapeTypes.MathMultiply:
                return getMathMultiplyPath(shape, rect);

            case ShapeTypes.MathDivide:
                return getMathDividePath(shape, rect);

            case ShapeTypes.MathEqual:
                return getMathEqualPath(shape, rect);

            case ShapeTypes.MathNotEqual:
                return getMathNotEqualPath(shape, rect);
        }

        return null;

    }

    private static Path getMathPlusPath(AutoShape shape, Rect rect) {
        float x = Math.min(rect.width(), rect.height()) * 0.24f / 2;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1) {
            if (values[0] != null) {
                x = Math.min(rect.width(), rect.height()) * values[0] / 2;
            }
        }
        float left = rect.left + (float) rect.width() / 8;
        float right = rect.right - (float) rect.width() / 8;
        float top = rect.top + (float) rect.height() / 8;
        float bottom = rect.bottom - (float) rect.height() / 8;

        path.moveTo(left, rect.exactCenterY() - x);
        path.lineTo(rect.exactCenterX() - x, rect.exactCenterY() - x);
        path.lineTo(rect.exactCenterX() - x, top);
        path.lineTo(rect.exactCenterX() + x, top);
        path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() - x);
        path.lineTo(right, rect.exactCenterY() - x);
        path.lineTo(right, rect.exactCenterY() + x);
        path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + x);
        path.lineTo(rect.exactCenterX() + x, bottom);
        path.lineTo(rect.exactCenterX() - x, bottom);
        path.lineTo(rect.exactCenterX() - x, rect.exactCenterY() + x);
        path.lineTo(left, rect.exactCenterY() + x);
        path.close();
        return path;
    }

    private static Path getMathMinusPath(AutoShape shape, Rect rect) {
        float x = rect.height() * 0.24f / 2;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1) {
            if (values[0] != null) {
                x = rect.height() * values[0] / 2;
            }
        }
        float left = rect.left + (float) rect.width() / 8;
        float right = rect.right - (float) rect.width() / 8;
        float top = rect.exactCenterY() - x;
        float bottom = rect.exactCenterY() + x;

        path.addRect(left, top, right, bottom, Path.Direction.CW);
        return path;
    }

    private static Path getMathMultiplyPath(AutoShape shape, Rect rect) {
        float d = rect.height() * 0.24f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1) {
            if (values[0] != null) {
                d = rect.height() * values[0];
            }
        }
        float k = (float) rect.height() / rect.width();
        float b = d * (float) Math.sqrt(k * k + 1) / 2;
        float c = (float) Math.sqrt(rect.width() * rect.width() +
                rect.height() * rect.height()) * (float) Math.sqrt(1 / (k * k) + 1) / 4;
        float x0 = (float) (rect.right + rect.left) / 2;
        float y0 = rect.exactCenterY();
        float x1 = (c - b) / (k + 1 / k);
        float y1 = k * x1 + b;
        float x2 = (c + b) / (k + 1 / k);
        float y2 = k * x2 - b;

        path.moveTo(x0, y0 - b);
        path.lineTo(x0 + x1, y0 - y1);
        path.lineTo(x0 + x2, y0 - y2);
        path.lineTo(x0 + b / k, y0);
        path.lineTo(x0 + x2, y0 + y2);
        path.lineTo(x0 + x1, y0 + y1);
        path.lineTo(x0, y0 + b);
        path.lineTo(x0 - x1, y0 + y1);
        path.lineTo(x0 - x2, y0 + y2);
        path.lineTo(x0 - b / k, y0);
        path.lineTo(x0 - x2, y0 - y2);
        path.lineTo(x0 - x1, y0 - y1);
        path.close();
        return path;
    }

    private static Path getMathDividePath(AutoShape shape, Rect rect) {
        float x1 = rect.height() * 0.2352f / 2;
        float x2 = rect.height() * 0.0588f;
        float r = rect.height() * 0.1176f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 3) {
            if (values[0] != null) {
                x1 = rect.height() * values[0] / 2;
            }
            if (values[1] != null) {
                x2 = rect.height() * values[1];
            }
            if (values[2] != null) {
                r = rect.height() * values[2];
            }
        }

        path.addRect(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() - x1,
                rect.right - (float) rect.width() / 8,
                rect.exactCenterY() + x1, Path.Direction.CW);

        path.moveTo(rect.exactCenterX() + r,
                rect.exactCenterY() - x1 - x2 - r);
        path.addCircle(rect.exactCenterX(),
                rect.exactCenterY() - x1 - x2 - r, r, Path.Direction.CW);

        path.moveTo(rect.exactCenterX(),
                rect.exactCenterY() + x1 + x2 + r);
        path.addCircle(rect.exactCenterX(),
                rect.exactCenterY() + x1 + x2 + r, r, Path.Direction.CW);
        return path;
    }

    private static Path getMathEqualPath(AutoShape shape, Rect rect) {
        float x1 = rect.height() * 0.2352f;
        float x2 = rect.height() * 0.1176f / 2;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2) {
            if (values[0] != null) {
                x1 = rect.height() * values[0];
            }
            if (values[1] != null) {
                x2 = rect.height() * values[1] / 2;
            }
        }

        path.reset();
        path.addRect(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() - x2 - x1,
                rect.right - (float) rect.width() / 8,
                rect.exactCenterY() - x2, Path.Direction.CW);

        path.moveTo(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() + x2);
        path.addRect(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() + x2,
                rect.right - (float) rect.width() / 8,
                rect.exactCenterY() + x2 + x1, Path.Direction.CW);
        return path;
    }

    private static Path getMathNotEqualPath(AutoShape shape, Rect rect) {
        float d1 = rect.height() * 0.2352f;
        float angle = 110;
        float d2 = rect.height() * 0.1176f / 2;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 3) {
            if (values[0] != null) {
                d1 = rect.height() * values[0];
            }
            if (values[1] != null) {
                angle = values[1] * 10 / 6;
            }
            if (values[2] != null) {
                d2 = rect.height() * values[2] / 2;
            }
        }
        float k = -(float) Math.tan(Math.toRadians(angle));
        float b = d1 * (float) Math.sqrt(k * k + 1) / 2;
        float c = (float) rect.height() / 2 - (b - (float) rect.height() / 2) / (k * k);

        float x0 = rect.exactCenterX();
        float y0 = rect.exactCenterY();
        float x1 = ((float) rect.height() / 2 - b) / k;
        float y1 = (float) rect.height() / 2;
        float x2 = (b + c) / (k + 1 / k);
        float y2 = k * x2 - b;
        float y3 = d2 + d1;
        float x3 = (y3 - b) / k;
        float y4 = y3;
        float x4 = (y4 + b) / k;
        float y5 = d2;
        float x5 = (d2 - b) / k;
        float y6 = y5;
        float x6 = (y6 + b) / k;

        path.reset();
        path.moveTo(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() - d2 - d1);
        if (k >= 0) {
            path.lineTo(x0 + x3, y0 - y3);
            path.lineTo(x0 + x1, y0 - y1);
            path.lineTo(x0 + x2, y0 - y2);
            path.lineTo(x0 + x4, y0 - y4);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() - d2 - d1);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() - d2);
            path.lineTo(x0 + x6, y0 - y6);
            path.lineTo(x0 - x5, y0 + y5);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() + d2);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() + d2 + d1);
            path.lineTo(x0 - x3, y0 + y3);
            path.lineTo(x0 - x1, y0 + y1);
            path.lineTo(x0 - x2, y0 + y2);
            path.lineTo(x0 - x4, y0 + y4);
            path.lineTo(rect.left + (float) rect.width() / 8,
                    rect.exactCenterY() + d2 + d1);
            path.lineTo(rect.left + (float) rect.width() / 8,
                    rect.exactCenterY() + d2);
            path.lineTo(x0 - x6, y0 + y6);
            path.lineTo(x0 + x5, y0 - y5);
        } else {
            path.lineTo(x0 + x4, y0 - y4);
            path.lineTo(x0 + x2, y0 - y2);
            path.lineTo(x0 + x1, y0 - y1);
            path.lineTo(x0 + x3, y0 - y3);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() - d2 - d1);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() - d2);
            path.lineTo(x0 + x5, y0 - y5);
            path.lineTo(x0 - x6, y0 + y6);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() + d2);
            path.lineTo(rect.right - (float) rect.width() / 8,
                    rect.exactCenterY() + d2 + d1);
            path.lineTo(x0 - x4, y0 + y4);
            path.lineTo(x0 - x2, y0 + y2);
            path.lineTo(x0 - x1, y0 + y1);
            path.lineTo(x0 - x3, y0 + y3);
            path.lineTo(rect.left + (float) rect.width() / 8,
                    rect.exactCenterY() + d2 + d1);
            path.lineTo(rect.left + (float) rect.width() / 8,
                    rect.exactCenterY() + d2);
            path.lineTo(x0 - x5, y0 + y5);
            path.lineTo(x0 + x6, y0 - y6);
        }
        path.lineTo(rect.left + (float) rect.width() / 8,
                rect.exactCenterY() - d2);
        path.close();
        return path;
    }
}
