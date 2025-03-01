package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.codec;

public interface BinaryEncoder extends Encoder {

    byte[] encode(byte[] source) throws EncoderException;
}  

