package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.io.IOException;
import java.io.InputStream;

public class ASCIIHexInputStream extends DecodingInputStream {

    private final boolean ignoreIllegalChars;
    private final InputStream in;
    private boolean endReached;
    private int prev;
    private int lineNo;

    public ASCIIHexInputStream(InputStream input) {
        this(input, false);
    }

    public ASCIIHexInputStream(InputStream input, boolean ignore) {
        super();
        in = input;
        ignoreIllegalChars = ignore;
        endReached = false;
        prev = -1;
        lineNo = 1;
    }

    public int read() throws IOException {
        if (endReached) {
            return -1;
        }

        int b0 = readPart();
        if (b0 == -1) {
            return -1;
        }

        int b1 = readPart();
        if (b1 == -1) {
            b1 = 0;
        }

        int d = (b0 << 4 | b1) & 0x00FF;
        return d;
    }

    public int getLineNo() {
        return lineNo;
    }

    private int readPart() throws IOException {
        while (true) {
            int b = in.read();
            switch (b) {
                case -1:
                    endReached = true;
                    if (!ignoreIllegalChars) {
                        throw new EncodingException(
                                "missing '>' at end of ASCII HEX stream");
                    }
                    return -1;
                case '>':
                    endReached = true;
                    return -1;
                case '\r':
                    lineNo++;
                    prev = b;
                    break;
                case '\n':
                    if (prev != '\r') {
                        lineNo++;
                    }
                    prev = b;
                    break;
                case ' ':
                case '\t':
                case '\f':
                case 0:

                    prev = b;
                    break;
                case '0':
                    return 0;
                case '1':
                    return 1;
                case '2':
                    return 2;
                case '3':
                    return 3;
                case '4':
                    return 4;
                case '5':
                    return 5;
                case '6':
                    return 6;
                case '7':
                    return 7;
                case '8':
                    return 8;
                case '9':
                    return 9;
                case 'A':
                case 'a':
                    return 10;
                case 'B':
                case 'b':
                    return 11;
                case 'C':
                case 'c':
                    return 12;
                case 'D':
                case 'd':
                    return 13;
                case 'E':
                case 'e':
                    return 14;
                case 'F':
                case 'f':
                    return 15;
                default:
                    if (!ignoreIllegalChars) {
                        throw new EncodingException("Illegal char " + b
                                + " in HexStream");
                    }
                    prev = b;
                    break;
            }
        }
    }
}
