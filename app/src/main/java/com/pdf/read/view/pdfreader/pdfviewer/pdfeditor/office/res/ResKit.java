package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.res;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ResKit {

    private static final ResKit kit = new ResKit();

    private Map<String, String> res;

    public ResKit() {
        try {
            res = new HashMap<String, String>();

            Class cls = Class.forName("com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.res.ResConstant");

            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                res.put(field.getName(), (String) field.get(null));
            }
        } catch (Exception e) {

        }
    }

    public static ResKit instance() {
        return kit;
    }

    public boolean hasResName(String resName) {
        return res.containsKey(resName);
    }

    public String getLocalString(String resName) {
        return res.get(resName);
    }
}
