package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.renderers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.util.MathHelper;

public class DialRenderer extends DefaultRenderer {

    private final List<Type> visualTypes = new ArrayList<Type>();

    private String mChartTitle = "";

    private float mChartTitleTextSize = 15;

    private double mAngleMin = 330;

    private double mAngleMax = 30;

    private double mMinValue = MathHelper.NULL_VALUE;

    private double mMaxValue = -MathHelper.NULL_VALUE;

    private double mMinorTickSpacing = MathHelper.NULL_VALUE;

    private double mMajorTickSpacing = MathHelper.NULL_VALUE;

    public String getChartTitle() {
        return mChartTitle;
    }

    public void setChartTitle(String title) {
        mChartTitle = title;
    }

    public float getChartTitleTextSize() {
        return mChartTitleTextSize;
    }

    public void setChartTitleTextSize(float textSize) {
        mChartTitleTextSize = textSize;
    }

    public double getAngleMin() {
        return mAngleMin;
    }

    public void setAngleMin(double min) {
        mAngleMin = min;
    }

    public double getAngleMax() {
        return mAngleMax;
    }

    public void setAngleMax(double max) {
        mAngleMax = max;
    }

    public double getMinValue() {
        return mMinValue;
    }

    public void setMinValue(double min) {
        mMinValue = min;
    }

    public boolean isMinValueSet() {
        return mMinValue != MathHelper.NULL_VALUE;
    }

    public double getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(double max) {
        mMaxValue = max;
    }

    public boolean isMaxValueSet() {
        return mMaxValue != -MathHelper.NULL_VALUE;
    }

    public double getMinorTicksSpacing() {
        return mMinorTickSpacing;
    }

    public void setMinorTicksSpacing(double spacing) {
        mMinorTickSpacing = spacing;
    }

    public double getMajorTicksSpacing() {
        return mMajorTickSpacing;
    }

    public void setMajorTicksSpacing(double spacing) {
        mMajorTickSpacing = spacing;
    }

    public Type getVisualTypeForIndex(int index) {
        if (index < visualTypes.size()) {
            return visualTypes.get(index);
        }
        return Type.NEEDLE;
    }

    public void setVisualTypes(Type[] types) {
        visualTypes.clear();
        visualTypes.addAll(Arrays.asList(types));
    }

    public enum Type {
        NEEDLE, ARROW
    }

}
