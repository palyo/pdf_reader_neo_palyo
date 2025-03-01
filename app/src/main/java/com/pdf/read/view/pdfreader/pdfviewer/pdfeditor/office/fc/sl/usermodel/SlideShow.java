package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

import java.io.IOException;

public interface SlideShow {
    Slide createSlide() throws IOException;

    MasterSheet createMasterSheet() throws IOException;

    Slide[] getSlides();

    MasterSheet[] getMasterSheet();

    Resources getResources();
}
