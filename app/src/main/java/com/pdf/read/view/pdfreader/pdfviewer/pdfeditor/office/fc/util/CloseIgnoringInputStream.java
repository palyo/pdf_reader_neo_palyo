package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.io.FilterInputStream;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSFileSystem;

public class CloseIgnoringInputStream extends FilterInputStream {
    public CloseIgnoringInputStream(InputStream in) {
        super(in);
    }

    public void close() {

    }
}
