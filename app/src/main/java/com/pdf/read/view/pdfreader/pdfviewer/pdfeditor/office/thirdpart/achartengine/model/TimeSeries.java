package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.achartengine.model;

import java.util.Date;

public class TimeSeries extends XYSeries {

    public TimeSeries(String title) {
        super(title);
    }

    public synchronized void add(Date x, double y) {
        super.add(x.getTime(), y);
    }
}
