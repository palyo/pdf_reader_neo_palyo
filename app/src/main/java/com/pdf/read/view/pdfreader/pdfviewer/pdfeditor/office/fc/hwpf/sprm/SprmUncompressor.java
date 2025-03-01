package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public abstract class SprmUncompressor {
    protected SprmUncompressor() {
    }

    public static boolean getFlag(int x) {
        return x != 0;
    }

}
