package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.codec;

public interface BinaryDecoder extends Decoder {

    byte[] decode(byte[] source) throws DecoderException;
}  

