package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.charts;

import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.DataMarker;

public interface ScatterChartData extends ChartData {
    ScatterChartSerie addSerie(DataMarker xMarker, DataMarker yMarker);

    List<? extends ScatterChartSerie> getSeries();
}
