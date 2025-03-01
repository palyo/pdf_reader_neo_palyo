package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import java.io.File;

public interface IReader {

    Object getModel() throws Exception;

    boolean searchContent(File file, String key) throws Exception;

    boolean isReaderFinish();

    void backReader() throws Exception;

    void abortReader();

    boolean isAborted();

    IControl getControl();

    void dispose();
}
