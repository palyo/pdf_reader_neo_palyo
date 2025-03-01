package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape;

import android.graphics.Path;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line;

public class ExtendPath {
    private Path path;
    private BackgroundAndFill fill;
    private boolean hasLine;
    private Line line;
    private boolean isArrow;

    public ExtendPath() {
        path = new Path();
        fill = null;
    }

    public ExtendPath(ExtendPath extendPath) {
        path = new Path(extendPath.getPath());
        fill = extendPath.getBackgroundAndFill();
        hasLine = extendPath.hasLine();
        line = extendPath.getLine();
        isArrow = extendPath.isArrowPath();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public BackgroundAndFill getBackgroundAndFill() {
        return fill;
    }

    public void setBackgroundAndFill(BackgroundAndFill fill) {
        this.fill = fill;
    }

    public boolean hasLine() {
        return hasLine;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(boolean hasLine) {
        this.hasLine = hasLine;
        if (hasLine && line == null) {
            line = new Line();
        }
    }

    public void setLine(Line line) {
        this.line = line;
        hasLine = line != null;
    }

    public void setArrowFlag(boolean isArrow) {
        this.isArrow = isArrow;
    }

    public boolean isArrowPath() {
        return isArrow;
    }

    public void dispose() {
        path = null;
        if (fill != null) {
            fill.dispose();
        }
    }
}
