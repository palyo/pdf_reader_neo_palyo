package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.ExtendPath;

public class ArbitraryPolygonShape extends LineShape {
    private final List<ExtendPath> paths;

    public ArbitraryPolygonShape() {
        paths = new ArrayList<ExtendPath>();
    }

    public void appendPath(ExtendPath path) {
        this.paths.add(path);
    }

    public List<ExtendPath> getPaths() {
        return paths;
    }
}
