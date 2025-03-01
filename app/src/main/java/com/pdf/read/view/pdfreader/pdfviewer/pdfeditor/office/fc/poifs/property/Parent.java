package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property;

import java.io.IOException;
import java.util.Iterator;

public interface Parent
        extends Child {

    Iterator getChildren();

    void addChild(final Property property)
            throws IOException;

    void setPreviousChild(final Child child);

    void setNextChild(final Child child);

}

