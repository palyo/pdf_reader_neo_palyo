package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.model.CategorySeries;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.renderers.DialRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.renderers.DialRenderer.Type;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.util.MathHelper;

public class DialChart extends RoundChart {

    private static final int NEEDLE_RADIUS = 10;

    private final DialRenderer mRenderer;

    public DialChart(CategorySeries dataset, DialRenderer renderer) {
        super(dataset, renderer);
        mRenderer = renderer;
    }

    @Override
    public void draw(Canvas canvas, IControl control, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(mRenderer.getLabelsTextSize());
        int legendSize = mRenderer.getLegendHeight();
        if (mRenderer.isShowLegend() && legendSize == 0) {
            legendSize = height / 5;
        }
        int left = x;
        int top = y;
        int right = x + width;

        int sLength = mDataset.getItemCount();
        double total = 0;
        String[] titles = new String[sLength];
        for (int i = 0; i < sLength; i++) {
            total += mDataset.getValue(i);
            titles[i] = mDataset.getCategory(i);
        }

        if (mRenderer.isFitLegend()) {
            legendSize = drawLegend(canvas, mRenderer, titles, left, y, width, height,
                    paint, true);
        }
        int bottom = y + height - legendSize;
        drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);

        int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
        int radius = (int) (mRadius * 0.35 * mRenderer.getScale());
        int centerX = (left + right) / 2;
        int centerY = (bottom + top) / 2;
        float shortRadius = radius * 0.9f;
        float longRadius = radius * 1.1f;
        double min = mRenderer.getMinValue();
        double max = mRenderer.getMaxValue();
        double angleMin = mRenderer.getAngleMin();
        double angleMax = mRenderer.getAngleMax();
        if (!mRenderer.isMinValueSet() || !mRenderer.isMaxValueSet()) {
            int count = mRenderer.getSeriesRendererCount();
            for (int i = 0; i < count; i++) {
                double value = mDataset.getValue(i);
                if (!mRenderer.isMinValueSet()) {
                    min = Math.min(min, value);
                }
                if (!mRenderer.isMaxValueSet()) {
                    max = Math.max(max, value);
                }
            }
        }
        if (min == max) {
            min = min * 0.5;
            max = max * 1.5;
        }

        paint.setColor(mRenderer.getLabelsColor());
        double minorTicks = mRenderer.getMinorTicksSpacing();
        double majorTicks = mRenderer.getMajorTicksSpacing();
        if (minorTicks == MathHelper.NULL_VALUE) {
            minorTicks = (max - min) / 30;
        }
        if (majorTicks == MathHelper.NULL_VALUE) {
            majorTicks = (max - min) / 10;
        }
        drawTicks(canvas, min, max, angleMin, angleMax, centerX, centerY, longRadius, radius,
                minorTicks, paint, false);
        drawTicks(canvas, min, max, angleMin, angleMax, centerX, centerY, longRadius, shortRadius,
                majorTicks, paint, true);

        int count = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < count; i++) {
            double angle = getAngleForValue(mDataset.getValue(i), angleMin, angleMax, min, max);
            paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
            boolean type = mRenderer.getVisualTypeForIndex(i) == Type.ARROW;
            drawNeedle(canvas, angle, centerX, centerY, shortRadius, type, paint);
        }
        drawLegend(canvas, mRenderer, titles, left, y, width, height, paint, false);
    }

    private double getAngleForValue(double value, double minAngle, double maxAngle, double min,
                                    double max) {
        double angleDiff = maxAngle - minAngle;
        double diff = max - min;
        return Math.toRadians(minAngle + (value - min) * angleDiff / diff);
    }

    private void drawTicks(Canvas canvas, double min, double max, double minAngle, double maxAngle,
                           int centerX, int centerY, double longRadius, double shortRadius, double ticks, Paint paint,
                           boolean labels) {
        for (double i = min; i <= max; i += ticks) {
            double angle = getAngleForValue(i, minAngle, maxAngle, min, max);
            double sinValue = Math.sin(angle);
            double cosValue = Math.cos(angle);
            int x1 = Math.round(centerX + (float) (shortRadius * sinValue));
            int y1 = Math.round(centerY + (float) (shortRadius * cosValue));
            int x2 = Math.round(centerX + (float) (longRadius * sinValue));
            int y2 = Math.round(centerY + (float) (longRadius * cosValue));
            canvas.drawLine(x1, y1, x2, y2, paint);
            if (labels) {
                paint.setTextAlign(Align.LEFT);
                if (x1 <= x2) {
                    paint.setTextAlign(Align.RIGHT);
                }
                String text = i + "";
                if (Math.round(i) == (long) i) {
                    text = (long) i + "";
                }
                canvas.drawText(text, x1, y1, paint);
            }
        }
    }

    private void drawNeedle(Canvas canvas, double angle, int centerX, int centerY, double radius,
                            boolean arrow, Paint paint) {
        double diff = Math.toRadians(90);
        int needleSinValue = (int) (NEEDLE_RADIUS * Math.sin(angle - diff));
        int needleCosValue = (int) (NEEDLE_RADIUS * Math.cos(angle - diff));
        int needleX = (int) (radius * Math.sin(angle));
        int needleY = (int) (radius * Math.cos(angle));
        int needleCenterX = centerX + needleX;
        int needleCenterY = centerY + needleY;
        float[] points;
        if (arrow) {
            int arrowBaseX = centerX + (int) (radius * 0.85 * Math.sin(angle));
            int arrowBaseY = centerY + (int) (radius * 0.85 * Math.cos(angle));
            points = new float[]{arrowBaseX - needleSinValue, arrowBaseY - needleCosValue,
                    needleCenterX, needleCenterY, arrowBaseX + needleSinValue, arrowBaseY + needleCosValue};
            float width = paint.getStrokeWidth();
            paint.setStrokeWidth(5);
            canvas.drawLine(centerX, centerY, needleCenterX, needleCenterY, paint);
            paint.setStrokeWidth(width);
        } else {
            points = new float[]{centerX - needleSinValue, centerY - needleCosValue, needleCenterX,
                    needleCenterY, centerX + needleSinValue, centerY + needleCosValue};
        }
        drawPath(canvas, points, paint, true);
    }

}
