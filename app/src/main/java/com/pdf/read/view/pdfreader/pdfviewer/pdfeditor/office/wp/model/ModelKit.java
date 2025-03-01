package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;

public class ModelKit {
    private static final ModelKit kit = new ModelKit();

    public static ModelKit instance() {
        return kit;
    }

    public long getArea(long offset) {
        return offset & WPModelConstant.AREA_MASK;
    }

}
