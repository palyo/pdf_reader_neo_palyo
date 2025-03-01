package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.IOUtils;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;

public class RawDataBlock
        implements ListManagedBlock {
    private static final POILogger log = POILogFactory.getLogger(RawDataBlock.class);
    private final byte[] _data;
    private final boolean _eof;
    private final boolean _hasData;

    public RawDataBlock(final InputStream stream)
            throws IOException {
        this(stream, POIFSConstants.SMALLER_BIG_BLOCK_SIZE);
    }

    public RawDataBlock(final InputStream stream, int blockSize)
            throws IOException {
        _data = new byte[blockSize];
        int count = IOUtils.readFully(stream, _data);
        _hasData = (count > 0);

        if (count == -1) {
            _eof = true;
        } else if (count != blockSize) {

            _eof = true;
            String type = " byte" + ((count == 1) ? ("")
                    : ("s"));

            log.log(POILogger.ERROR,
                    "Unable to read entire block; " + count
                            + type + " read before EOF; expected "
                            + blockSize + " bytes. Your document "
                            + "was either written by software that "
                            + "ignores the spec, or has been truncated!"
            );
        } else {
            _eof = false;
        }
    }

    public boolean eof() {
        return _eof;
    }

    public boolean hasData() {
        return _hasData;
    }

    public String toString() {
        return "RawDataBlock of size " + _data.length;
    }

    public byte[] getData()
            throws IOException {
        if (!hasData()) {
            throw new IOException("Cannot return empty data");
        }
        return _data;
    }

    public int getBigBlockSize() {
        return _data.length;
    }

}

