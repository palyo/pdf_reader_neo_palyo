package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.io.IOException;

public interface PromptListener {

    void promptFound(RoutedInputStream.Route route) throws IOException;
}
