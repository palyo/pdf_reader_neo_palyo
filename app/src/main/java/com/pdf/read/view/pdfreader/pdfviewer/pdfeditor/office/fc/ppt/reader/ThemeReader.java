package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader;

import android.graphics.Color;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.io.SAXReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;

public class ThemeReader {
    private static final ThemeReader themeReader = new ThemeReader();

    public static ThemeReader instance() {
        return themeReader;
    }

    public Map<String, Integer> getThemeColorMap(PackagePart themePart) throws Exception {

        SAXReader saxreader = new SAXReader();
        InputStream in = themePart.getInputStream();
        Document poiTheme = saxreader.read(in);
        Element root = poiTheme.getRootElement();
        if (root != null) {
            Element themeElements = root.element("themeElements");
            if (themeElements != null) {
                Element clrScheme = themeElements.element("clrScheme");

                Map<String, Integer> colorMap = new HashMap<String, Integer>();
                for (Iterator<?> it = clrScheme.elementIterator(); it.hasNext(); ) {
                    Element clr = (Element) it.next();
                    String name = clr.getName();
                    Element srgbClr = clr.element("srgbClr");
                    Element sysClr = clr.element("sysClr");
                    if (srgbClr != null) {
                        colorMap.put(name, Color.parseColor("#" + srgbClr.attributeValue("val")));
                    } else if (sysClr != null) {
                        colorMap.put(name, Color.parseColor("#" + sysClr.attributeValue("lastClr")));
                    } else {
                        colorMap.put(name, Color.WHITE);
                    }
                }
                return colorMap;
            }
        }
        in.close();
        return null;
    }
}
