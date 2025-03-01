package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

public interface ZipEntrySource {

    Enumeration<? extends ZipEntry> getEntries();

    InputStream getInputStream(ZipEntry entry) throws IOException;

    void close() throws IOException;
}
