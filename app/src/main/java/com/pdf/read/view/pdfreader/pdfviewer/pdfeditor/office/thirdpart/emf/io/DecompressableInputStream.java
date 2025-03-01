package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public class DecompressableInputStream extends DecodingInputStream {

    private final InputStream in;
    private boolean decompress;
    private InflaterInputStream iis;
    private byte[] b = null;

    private int len = 0;

    private int i = 0;

    public DecompressableInputStream(InputStream input) {
        super();
        in = input;
        decompress = false;

        try {
            len = in.available();
            b = new byte[len];
            in.read(b);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public int read() throws IOException {

        if (i >= len) {
            return -1;
        }
        return b[i++] & 0x000000FF;
    }

    public long skip(long n) throws IOException {

        i += n;
        return n;
    }

    public void startDecompressing() throws IOException {
        decompress = true;
        iis = new InflaterInputStream(in);
    }
}
