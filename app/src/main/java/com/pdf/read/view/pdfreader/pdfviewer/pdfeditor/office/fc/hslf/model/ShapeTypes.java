package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.exceptions.HSLFException;

public final class ShapeTypes implements com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes {
    public static HashMap types;

    static {
        types = new HashMap();
        try {
            Field[] f = com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes.class.getFields();
            for (int i = 0; i < f.length; i++) {
                Object val = f[i].get(null);
                if (val instanceof Integer) {
                    types.put(val, f[i].getName());
                }
            }
        } catch (IllegalAccessException e) {
            throw new HSLFException("Failed to initialize shape types");
        }
    }

    public static String typeName(int type) {
        String name = (String) types.get(Integer.valueOf(type));
        return name;
    }
}
