package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.model;

import java.util.ArrayList;
import java.util.List;

public class RangeCategorySeries extends CategorySeries {

    private final List<Double> mMaxValues = new ArrayList<Double>();

    public RangeCategorySeries(String title) {
        super(title);
    }

    public synchronized void add(double minValue, double maxValue) {
        super.add(minValue);
        mMaxValues.add(maxValue);
    }

    public synchronized void add(String category, double minValue, double maxValue) {
        super.add(category, minValue);
        mMaxValues.add(maxValue);
    }

    public synchronized void remove(int index) {
        super.remove(index);
        mMaxValues.remove(index);
    }

    public synchronized void clear() {
        super.clear();
        mMaxValues.clear();
    }

    public double getMinimumValue(int index) {
        return getValue(index);
    }

    public double getMaximumValue(int index) {
        return mMaxValues.get(index);
    }

    public XYSeries toXYSeries() {
        XYSeries xySeries = new XYSeries(getTitle());
        int length = getItemCount();
        for (int k = 0; k < length; k++) {
            xySeries.add(k + 1, getMinimumValue(k));
            xySeries.add(k + 1, getMaximumValue(k));
        }
        return xySeries;
    }
}
