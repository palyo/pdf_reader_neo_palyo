package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianConsts;

public final class POIFSBigBlockSize {
    private final int bigBlockSize;
    private final short headerValue;

    POIFSBigBlockSize(int bigBlockSize, short headerValue) {
        this.bigBlockSize = bigBlockSize;
        this.headerValue = headerValue;
    }

    public int getBigBlockSize() {
        return bigBlockSize;
    }

    public short getHeaderValue() {
        return headerValue;
    }

    public int getPropertiesPerBlock() {
        return bigBlockSize / POIFSConstants.PROPERTY_SIZE;
    }

    public int getBATEntriesPerBlock() {
        return bigBlockSize / LittleEndianConsts.INT_SIZE;
    }

    public int getXBATEntriesPerBlock() {
        return getBATEntriesPerBlock() - 1;
    }

    public int getNextXBATChainOffset() {
        return getXBATEntriesPerBlock() * LittleEndianConsts.INT_SIZE;
    }
}
