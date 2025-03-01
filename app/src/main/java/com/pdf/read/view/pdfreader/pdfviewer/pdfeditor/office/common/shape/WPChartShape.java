package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.chart.AbstractChart;

public class WPChartShape extends WPAutoShape {

    private AbstractChart chart;

    public AbstractChart getAChart() {
        return chart;
    }

    public void setAChart(AbstractChart chart) {
        this.chart = chart;
    }
}
