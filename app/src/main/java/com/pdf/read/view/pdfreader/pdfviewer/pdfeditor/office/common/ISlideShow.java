package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common;

public interface ISlideShow {
    byte SlideShow_Begin = 0;
    byte SlideShow_Exit = SlideShow_Begin + 1;
    byte SlideShow_PreviousStep = SlideShow_Exit + 1;
    byte SlideShow_NextStep = SlideShow_PreviousStep + 1;
    byte SlideShow_PreviousSlide = SlideShow_NextStep + 1;
    byte SlideShow_NextSlide = SlideShow_PreviousSlide + 1;

    void exit();
}
