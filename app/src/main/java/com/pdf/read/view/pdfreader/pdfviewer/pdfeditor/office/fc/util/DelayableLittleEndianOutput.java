package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

public interface DelayableLittleEndianOutput extends LittleEndianOutput {

    LittleEndianOutput createDelayedOutput(int size);
}
