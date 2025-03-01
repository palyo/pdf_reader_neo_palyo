package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public interface CharIndexTranslator {

    int getByteIndex(int charPos);

    int getCharIndex(int bytePos);

    int getCharIndex(int bytePos, int startCP);

    boolean isIndexInTable(int bytePos);

    int lookIndexForward(int bytePos);

    int lookIndexBackward(int bytePos);

}
