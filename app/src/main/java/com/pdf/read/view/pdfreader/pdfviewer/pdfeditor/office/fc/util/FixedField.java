package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian.BufferUnderrunException;

public interface FixedField {

    void readFromBytes(byte[] data)
            throws ArrayIndexOutOfBoundsException;

    void readFromStream(InputStream stream)
            throws IOException, BufferUnderrunException;

    void writeToBytes(byte[] data)
            throws ArrayIndexOutOfBoundsException;

    String toString();
}   

