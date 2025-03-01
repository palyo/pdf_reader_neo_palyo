package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ChartAxis;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ChartAxisFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ChartData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ChartDataFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ChartLegend;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts.ManuallyPositionable;

public interface Chart extends ManuallyPositionable {

    ChartDataFactory getChartDataFactory();

    ChartAxisFactory getChartAxisFactory();

    ChartLegend getOrCreateLegend();

    void deleteLegend();

    List<? extends ChartAxis> getAxis();

    void plot(ChartData data, ChartAxis... axis);
}
