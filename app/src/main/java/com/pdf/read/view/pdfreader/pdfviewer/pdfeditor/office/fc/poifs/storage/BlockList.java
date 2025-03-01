package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import java.io.IOException;

public interface BlockList {

    void zap(final int index);

    ListManagedBlock remove(final int index)
            throws IOException;

    ListManagedBlock[] fetchBlocks(final int startBlock, final int headerPropertiesStartBlock)
            throws IOException;

    void setBAT(final BlockAllocationTableReader bat)
            throws IOException;

    int blockCount();
}   

