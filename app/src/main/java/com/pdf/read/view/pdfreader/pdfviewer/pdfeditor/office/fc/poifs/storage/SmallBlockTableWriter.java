package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSBigBlockSize;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.BATManaged;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.RootProperty;

public class SmallBlockTableWriter
        implements BlockWritable, BATManaged {
    private final BlockAllocationTableWriter _sbat;
    private final List _small_blocks;
    private final int _big_block_count;
    private final RootProperty _root;

    public SmallBlockTableWriter(final POIFSBigBlockSize bigBlockSize,
                                 final List documents,
                                 final RootProperty root) {
        _sbat = new BlockAllocationTableWriter(bigBlockSize);
        _small_blocks = new ArrayList();
        _root = root;
        Iterator iter = documents.iterator();

        while (iter.hasNext()) {
            POIFSDocument doc = (POIFSDocument) iter.next();
            BlockWritable[] blocks = doc.getSmallBlocks();

            if (blocks.length != 0) {
                doc.setStartBlock(_sbat.allocateSpace(blocks.length));
                Collections.addAll(_small_blocks, blocks);
            } else {
                doc.setStartBlock(POIFSConstants.END_OF_CHAIN);
            }
        }
        _sbat.simpleCreateBlocks();
        _root.setSize(_small_blocks.size());
        _big_block_count = SmallDocumentBlock.fill(bigBlockSize, _small_blocks);
    }

    public int getSBATBlockCount() {
        return (_big_block_count + 15) / 16;
    }

    public BlockAllocationTableWriter getSBAT() {
        return _sbat;
    }

    public int countBlocks() {
        return _big_block_count;
    }

    public void setStartBlock(int start_block) {
        _root.setStartBlock(start_block);
    }

    public void writeBlocks(final OutputStream stream)
            throws IOException {
        Iterator iter = _small_blocks.iterator();

        while (iter.hasNext()) {
            ((BlockWritable) iter.next()).writeBlocks(stream);
        }
    }

}
