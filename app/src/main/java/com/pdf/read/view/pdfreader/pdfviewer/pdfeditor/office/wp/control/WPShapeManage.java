package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.AbstractShape;

public class WPShapeManage {

    private final Map<Integer, AbstractShape> shapes;

    public WPShapeManage() {
        shapes = new HashMap<Integer, AbstractShape>(20);
    }

    public int addShape(AbstractShape shape) {
        int size = shapes.size();
        shapes.put(size, shape);
        return size;
    }

    public AbstractShape getShape(int index) {
        if (index < 0 || index >= shapes.size()) {
            return null;
        }
        return shapes.get(index);
    }

    public void dispose() {
        if (shapes != null) {
            Collection<AbstractShape> ass = shapes.values();
            if (ass != null) {
                for (AbstractShape as : ass) {
                    as.dispose();
                }
                shapes.clear();
            }
        }
    }

}
