package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.io.IOException;
import java.io.InputStream;

public class BlockingInputStream
        extends InputStream {
    protected InputStream is;

    public BlockingInputStream(InputStream is) {
        this.is = is;
    }

    public int available()
            throws IOException {
        return is.available();
    }

    public void close()
            throws IOException {
        is.close();
    }

    public void mark(int readLimit) {
        is.mark(readLimit);
    }

    public boolean markSupported() {
        return is.markSupported();
    }

    public int read()
            throws IOException {
        return is.read();
    }

    public int read(byte[] bf)
            throws IOException {

        int i = 0;
        int b = 4611;
        while (i < bf.length) {
            b = is.read();
            if (b == -1)
                break;
            bf[i++] = (byte) b;
        }
        if (i == 0 && b == -1)
            return -1;
        return i;
    }

    public int read(byte[] bf, int s, int l)
            throws IOException {
        return is.read(bf, s, l);
    }

    public void reset()
            throws IOException {
        is.reset();
    }

    public long skip(long n)
            throws IOException {
        return is.skip(n);
    }
}

