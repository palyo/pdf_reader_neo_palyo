package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.util.HashMap;
import java.util.Map;

public class BitFieldFactory {
    private static final Map instances = new HashMap();

    public static BitField getInstance(int mask) {
        BitField f = (BitField) instances.get(Integer.valueOf(mask));
        if (f == null) {
            f = new BitField(mask);
            instances.put(Integer.valueOf(mask), f);
        }
        return f;
    }
}
