package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.tools;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.AbstractChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.RoundChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.XYChart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.model.XYSeries;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.util.MathHelper;

public class FitZoom extends AbstractTool {

    public FitZoom(AbstractChart chart) {
        super(chart);
    }

    public void apply() {
        if (mChart instanceof XYChart) {
            if (((XYChart) mChart).getDataset() == null) {
                return;
            }
            int scales = mRenderer.getScalesCount();
            if (mRenderer.isInitialRangeSet()) {
                for (int i = 0; i < scales; i++) {
                    if (mRenderer.isInitialRangeSet(i)) {
                        mRenderer.setRange(mRenderer.getInitialRange(i), i);
                    }
                }
            } else {
                XYSeries[] series = ((XYChart) mChart).getDataset().getSeries();
                double[] range = null;
                int length = series.length;
                if (length > 0) {
                    for (int i = 0; i < scales; i++) {
                        range = new double[]{MathHelper.NULL_VALUE, -MathHelper.NULL_VALUE,
                                MathHelper.NULL_VALUE, -MathHelper.NULL_VALUE};
                        for (int j = 0; j < length; j++) {
                            if (i == series[j].getScaleNumber()) {
                                range[0] = Math.min(range[0], series[j].getMinX());
                                range[1] = Math.max(range[1], series[j].getMaxX());
                                range[2] = Math.min(range[2], series[j].getMinY());
                                range[3] = Math.max(range[3], series[j].getMaxY());
                            }
                        }
                        double marginX = Math.abs(range[1] - range[0]) / 40;
                        double marginY = Math.abs(range[3] - range[2]) / 40;
                        mRenderer.setRange(new double[]{range[0] - marginX, range[1] + marginX,
                                range[2] - marginY, range[3] + marginY}, i);
                    }
                }
            }
        } else {
            DefaultRenderer renderer = ((RoundChart) mChart).getRenderer();
            renderer.setScale(renderer.getOriginalScale());
        }
    }
}
