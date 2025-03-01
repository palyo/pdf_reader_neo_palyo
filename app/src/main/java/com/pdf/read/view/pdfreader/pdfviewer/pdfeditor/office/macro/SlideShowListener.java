package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ISlideShow;

public interface SlideShowListener {

    byte SlideShow_Begin = ISlideShow.SlideShow_Begin;

    byte SlideShow_Exit = ISlideShow.SlideShow_Exit;

    byte SlideShow_PreviousStep = ISlideShow.SlideShow_PreviousStep;

    byte SlideShow_NextStep = ISlideShow.SlideShow_NextStep;

    byte SlideShow_PreviousSlide = ISlideShow.SlideShow_PreviousSlide;

    byte SlideShow_NextSlide = ISlideShow.SlideShow_NextSlide;

    void exit();
}
