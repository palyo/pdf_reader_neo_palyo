package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;

public interface BlockWritable {

    void writeBlocks(final OutputStream stream)
            throws IOException;
}   

