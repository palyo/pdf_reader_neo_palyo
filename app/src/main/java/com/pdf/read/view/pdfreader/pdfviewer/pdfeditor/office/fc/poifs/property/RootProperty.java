package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.SmallDocumentBlock;

public final class RootProperty extends DirectoryProperty {
    private static final String NAME = "Root Entry";

    RootProperty() {
        super(NAME);

        setNodeColor(_NODE_BLACK);
        setPropertyType(PropertyConstants.ROOT_TYPE);
        setStartBlock(POIFSConstants.END_OF_CHAIN);
    }

    RootProperty(final int index, final byte[] array,
                 final int offset) {
        super(index, array, offset);
    }

    public void setSize(int size) {
        super.setSize(SmallDocumentBlock.calcSize(size));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
