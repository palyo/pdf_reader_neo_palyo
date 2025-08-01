package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.font;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class TTFFont {

    private final Map entry = new HashMap();

    public abstract int getFontVersion();

    void newTable(String tag, TTFInput input) throws IOException {
        entry.put(tag, initTable(tag, input));
    }

    private Object initTable(String name, TTFInput input) throws IOException {
        TTFTable table = null;
        for (int i = 0; i < TTFTable.TT_TAGS.length; i++) {
            if (name.equals(TTFTable.TT_TAGS[i])) {
                try {
                    table = (TTFTable) TTFTable.TABLE_CLASSES[i].newInstance();
                    table.init(this, input);
                    return table;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        System.err.println("Table '" + name + "' ignored.");
        return null;
    }

    public void show() {
        System.out.println("Tables:");
        for (Iterator i = entry.values().iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
    }

    public TTFTable getTable(String tag) throws IOException {
        TTFTable table = (TTFTable) entry.get(tag);
        if (!table.isRead())
            table.read();
        return table;
    }

    public void readAll() throws IOException {
        Iterator i = entry.values().iterator();
        while (i.hasNext()) {
            TTFTable table = (TTFTable) i.next();
            if ((table != null) && (!table.isRead()))
                table.read();
        }
    }

    public void close() throws IOException {
    }
}
