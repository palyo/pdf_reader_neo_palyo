package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.pathbuilder.starAndBanner.star;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AutoShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;

public class EarlyStarPathBuilder {
    private static final Matrix sm = new Matrix();

    public static Path getStarPath(AutoShape shape, Rect rect) {
        switch (shape.getShapeType()) {
            case ShapeTypes.Star4:
                return getStar4Path(shape, rect);

            case ShapeTypes.Star5:
            case ShapeTypes.Star:
                return getStar5Path(shape, rect);

            case ShapeTypes.Star8:
                return getStar8Path(shape, rect);

            case ShapeTypes.Star16:
                return getStar16Path(shape, rect);

            case ShapeTypes.Star24:
                return getStar24Path(shape, rect);

            case ShapeTypes.Star32:
                return getStar32Path(shape, rect);
        }

        return null;
    }

    private static Path getStar4Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;
        if (values != null && values.length == 1) {
            if (values[0] > 0.5f) {
                values[0] = 0.5f;
            }
            a = len * (0.5f - values[0]);
            b = len * (0.5f - values[0]);
        } else {
            a = len * 0.125f;
            b = len * 0.125f;
        }

        float outA = width / 2;
        float outB = height / 2;

        Path path = StarPathBuilder.getStarPath((int) outA, (int) outB, (int) a, (int) b, 4);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        path.offset(rect.centerX(), rect.centerY());

        return path;
    }

    private static Path getStar5Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;

        width = width * 1.05146f;
        height = height * 1.10557f;

        a = width * 0.2f;
        b = height * 0.2f;

        float outA = width / 2;
        float outB = height / 2;

        Path path = StarPathBuilder.getStarPath((int) outA, (int) outB, (int) a, (int) b, 5);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);

        height = height * rect.height() / len;
        path.offset(rect.centerX(), rect.centerY() + (height - rect.height()) / 2);

        return path;
    }

    private static Path getStar8Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;
        if (values != null && values.length == 1) {
            if (values[0] > 0.5f) {
                values[0] = 0.5f;
            }
            a = width * (0.5f - values[0]);
            b = height * (0.5f - values[0]);
        } else {
            a = width * 0.375f;
            b = height * 0.375f;
        }

        float outA = width / 2;
        float outB = height / 2;

        Path path = StarPathBuilder.getStarPath((int) outA, (int) outB, (int) a, (int) b, 8);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        path.offset(rect.centerX(), rect.centerY());

        return path;
    }

    private static Path getStar16Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;
        if (values != null && values.length == 1) {
            if (values[0] > 0.5f) {
                values[0] = 0.5f;
            }
            a = width * (0.5f - values[0]);
            b = height * (0.5f - values[0]);
        } else {
            a = width * 0.375f;
            b = height * 0.375f;
        }

        Path path = StarPathBuilder.getStarPath((int) width / 2, (int) height / 2, (int) a, (int) b, 16);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        path.offset(rect.centerX(), rect.centerY());

        return path;
    }

    private static Path getStar24Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;
        if (values != null && values.length == 1) {
            if (values[0] > 0.5f) {
                values[0] = 0.5f;
            }
            a = width * (0.5f - values[0]);
            b = height * (0.5f - values[0]);
        } else {
            a = width * 0.375f;
            b = height * 0.375f;
        }

        Path path = StarPathBuilder.getStarPath((int) width / 2, (int) height / 2, (int) a, (int) b, 24);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        path.offset(rect.centerX(), rect.centerY());
        return path;
    }

    private static Path getStar32Path(AutoShape shape, Rect rect) {
        Float[] values = shape.getAdjustData();

        float len = Math.min(rect.width(), rect.height());
        float width = len, height = len;
        float a = 0, b = 0;
        if (values != null && values.length == 1) {
            if (values[0] > 0.5f) {
                values[0] = 0.5f;
            }
            a = width * (0.5f - values[0]);
            b = height * (0.5f - values[0]);
        } else {
            a = width * 0.375f;
            b = height * 0.375f;
        }

        Path path = StarPathBuilder.getStarPath((int) width / 2, (int) height / 2, (int) a, (int) b, 32);

        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        path.offset(rect.centerX(), rect.centerY());

        return path;
    }
}
