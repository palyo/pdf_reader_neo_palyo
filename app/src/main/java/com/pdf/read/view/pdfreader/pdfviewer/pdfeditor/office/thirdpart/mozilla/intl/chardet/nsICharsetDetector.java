package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

public interface nsICharsetDetector {

    void Init(nsICharsetDetectionObserver observer);

    boolean DoIt(byte[] aBuf, int aLen, boolean oDontFeedMe);

    void Done();
}

