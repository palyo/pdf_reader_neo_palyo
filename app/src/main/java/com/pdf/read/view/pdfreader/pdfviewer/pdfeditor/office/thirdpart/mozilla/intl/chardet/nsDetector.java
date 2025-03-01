package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

public class nsDetector extends nsPSMDetector implements nsICharsetDetector {

    nsICharsetDetectionObserver mObserver = null;

    public nsDetector() {
        super();
    }

    public nsDetector(int langFlag) {
        super(langFlag);
    }

    public void Init(nsICharsetDetectionObserver aObserver) {

        mObserver = aObserver;

    }

    public boolean DoIt(byte[] aBuf, int aLen, boolean oDontFeedMe) {

        if (aBuf == null || oDontFeedMe)
            return false;

        this.HandleData(aBuf, aLen);
        return mDone;
    }

    public void Done() {
        this.DataEnd();
    }

    public void Report(String charset) {
        if (mObserver != null)
            mObserver.Notify(charset);
    }

    public boolean isAscii(byte[] aBuf, int aLen) {
        for (int i = 0; i < aLen; i++) {
            if ((0x0080 & aBuf[i]) != 0) {
                return false;
            }
        }
        return true;
    }
}
