package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.io.IOException;

public interface RouteListener {

    void routeFound(RoutedInputStream.Route input) throws IOException;
}
