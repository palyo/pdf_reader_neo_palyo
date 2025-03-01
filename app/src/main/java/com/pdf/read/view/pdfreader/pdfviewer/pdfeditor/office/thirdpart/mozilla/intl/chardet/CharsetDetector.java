package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class CharsetDetector {

    public static boolean found = false;
    public static String charsetStr;

    private CharsetDetector() {
    }

    public static String detect(BufferedInputStream imp) throws Exception {
        found = false;
        charsetStr = "ASCII";

        nsDetector det = new nsDetector(nsPSMDetector.ALL);

        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                found = true;
                charsetStr = charset;
            }
        });

        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;
        int count = 0;
        while ((len = imp.read(buf, 0, buf.length)) != -1 && count <= 50) {

            if (count == 0) {

                if ((buf[0] == -1 && buf[1] == -2)
                        || (buf[1] == -2 && buf[0] == -1)) {
                    charsetStr = "Unicode";
                    return charsetStr;
                } else if (buf[0] == -17 && buf[1] == -69 && buf[2] == -65) {
                    charsetStr = "UTF-8";
                    return charsetStr;
                }
            }

            if (isAscii) {
                isAscii = det.isAscii(buf, len);
            }

            if (!isAscii && !done) {
                done = det.DoIt(buf, len, false);
            }
            count++;
        }
        det.DataEnd();

        if (isAscii) {
            return "ASCII";
        }

        if (!found) {

            return null;
        }

        return charsetStr;
    }

    public static String detect(String fileName) throws Exception {

        FileInputStream file = new FileInputStream(fileName);
        BufferedInputStream imp = new BufferedInputStream(file);
        String charset = detect(imp);
        imp.close();

        return charset;
    }
}
