package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;
import java.nio.charset.Charset;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.EMFInputStream;

public class TextA extends Text {

    public TextA(Point pos, String string, int options, Rectangle bounds, int[] widths) {
        super(pos, string, options, bounds, widths);
    }

    public static TextA read(EMFInputStream emf) throws IOException {
        Point pos = emf.readPOINTL();
        int sLen = emf.readDWORD();

        emf.readDWORD();
        int options = emf.readDWORD();
        Rectangle bounds = emf.readRECTL();

        emf.readDWORD();

        String csn = Charset.defaultCharset().name();
        String string = new String(emf.readBYTE(sLen), csn);
        if ((sLen) % 4 != 0)
            for (int i = 0; i < 4 - (sLen) % 4; i++)
                emf.readBYTE();
        int[] widths = new int[sLen];
        for (int i = 0; i < sLen; i++)
            widths[i] = emf.readDWORD();
        return new TextA(pos, string, options, bounds, widths);
    }

    public String toString() {
        StringBuffer widthsS = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            widthsS.append(",");
            widthsS.append(widths[i]);
        }
        widthsS.append(']');
        widthsS.setCharAt(0, '[');
        return "  TextA\n" + "    pos: " + pos + "\n    options: " + options + "\n    bounds: "
                + bounds + "\n    string: " + string + "\n    widths: " + widthsS;
    }
}
