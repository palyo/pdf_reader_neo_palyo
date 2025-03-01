package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSBigBlockSize;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.RootProperty;

public final class SmallBlockTableReader {

    public static BlockList getSmallDocumentBlocks(
            final POIFSBigBlockSize bigBlockSize,
            final RawDataBlockList blockList, final RootProperty root,
            final int sbatStart)
            throws IOException {

        ListManagedBlock[] smallBlockBlocks =
                blockList.fetchBlocks(root.getStartBlock(), -1);

        BlockList list = new SmallDocumentBlockList(
                SmallDocumentBlock.extract(bigBlockSize, smallBlockBlocks));

        new BlockAllocationTableReader(bigBlockSize,
                blockList.fetchBlocks(sbatStart, -1),
                list);
        return list;
    }
}
