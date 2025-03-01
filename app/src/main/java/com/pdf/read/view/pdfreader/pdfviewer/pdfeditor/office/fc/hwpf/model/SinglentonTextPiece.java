package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public class SinglentonTextPiece extends TextPiece {

    public SinglentonTextPiece(StringBuilder buffer) throws IOException {
        super(0, buffer.length(), buffer.toString().getBytes(StandardCharsets.UTF_16LE),
                new PieceDescriptor(new byte[8], 0), 0);
    }

    @Override
    public int bytesLength() {
        return getStringBuilder().length() * 2;
    }

    @Override
    public int characterLength() {
        return getStringBuilder().length();
    }

    @Override
    public int getCP() {
        return 0;
    }

    @Override
    public int getEnd() {
        return characterLength();
    }

    @Override
    public int getStart() {
        return 0;
    }

    public String toString() {
        return "SinglentonTextPiece (" + characterLength() + " chars)";
    }
}
