package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSBigBlockSize;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.DocumentProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.Property;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.BlockWritable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.DataInputBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.DocumentBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.ListManagedBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.RawDataBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.SmallDocumentBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;

public final class POIFSDocument implements BATManaged, BlockWritable {
    private static final DocumentBlock[] EMPTY_BIG_BLOCK_ARRAY = {};
    private static final SmallDocumentBlock[] EMPTY_SMALL_BLOCK_ARRAY = {};
    private final POIFSBigBlockSize _bigBigBlockSize;
    private final DocumentProperty _property;

    private final SmallBlockStore _small_store;
    private int _size;
    private BigBlockStore _big_store;

    public POIFSDocument(String name, RawDataBlock[] blocks, int length) throws IOException {
        _size = length;
        if (blocks.length == 0) {
            _bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        } else {
            _bigBigBlockSize = (blocks[0].getBigBlockSize() == POIFSConstants.SMALLER_BIG_BLOCK_SIZE ?
                    POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS :
                    POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS
            );
        }

        _big_store = new BigBlockStore(_bigBigBlockSize, convertRawBlocksToBigBlocks(blocks));
        _property = new DocumentProperty(name, _size);
        _small_store = new SmallBlockStore(_bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
        _property.setDocument(this);
    }

    public POIFSDocument(String name, SmallDocumentBlock[] blocks, int length) {
        _size = length;

        if (blocks.length == 0) {
            _bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        } else {
            _bigBigBlockSize = blocks[0].getBigBlockSize();
        }

        _big_store = new BigBlockStore(_bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
        _property = new DocumentProperty(name, _size);
        _small_store = new SmallBlockStore(_bigBigBlockSize, blocks);
        _property.setDocument(this);
    }

    public POIFSDocument(String name, POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks, int length) throws IOException {
        _size = length;
        _bigBigBlockSize = bigBlockSize;
        _property = new DocumentProperty(name, _size);
        _property.setDocument(this);
        if (Property.isSmall(_size)) {
            _big_store = new BigBlockStore(bigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
            _small_store = new SmallBlockStore(bigBlockSize, convertRawBlocksToSmallBlocks(blocks));
        } else {
            _big_store = new BigBlockStore(bigBlockSize, convertRawBlocksToBigBlocks(blocks));
            _small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
        }
    }

    public POIFSDocument(String name, ListManagedBlock[] blocks, int length) throws IOException {
        this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, blocks, length);
    }

    public POIFSDocument(String name, POIFSBigBlockSize bigBlockSize, InputStream stream) throws IOException {
        List<DocumentBlock> blocks = new ArrayList<DocumentBlock>();

        _size = 0;
        _bigBigBlockSize = bigBlockSize;
        while (true) {
            DocumentBlock block = new DocumentBlock(stream, bigBlockSize);
            int blockSize = block.size();

            if (blockSize > 0) {
                blocks.add(block);
                _size += blockSize;
            }
            if (block.partiallyRead()) {
                break;
            }
        }
        DocumentBlock[] bigBlocks = blocks.toArray(new DocumentBlock[blocks.size()]);

        _big_store = new BigBlockStore(bigBlockSize, bigBlocks);
        _property = new DocumentProperty(name, _size);
        _property.setDocument(this);
        if (_property.shouldUseSmallBlocks()) {
            _small_store = new SmallBlockStore(bigBlockSize, SmallDocumentBlock.convert(bigBlockSize, bigBlocks, _size));
            _big_store = new BigBlockStore(bigBlockSize, new DocumentBlock[0]);
        } else {
            _small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
        }
    }

    public POIFSDocument(String name, InputStream stream) throws IOException {
        this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, stream);
    }

    public POIFSDocument(String name, int size, POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, POIFSWriterListener writer) {
        _size = size;
        _bigBigBlockSize = bigBlockSize;
        _property = new DocumentProperty(name, _size);
        _property.setDocument(this);
        if (_property.shouldUseSmallBlocks()) {
            _small_store = new SmallBlockStore(_bigBigBlockSize, path, name, size, writer);
            _big_store = new BigBlockStore(_bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
        } else {
            _small_store = new SmallBlockStore(_bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
            _big_store = new BigBlockStore(_bigBigBlockSize, path, name, size, writer);
        }
    }

    public POIFSDocument(String name, int size, POIFSDocumentPath path, POIFSWriterListener writer) {
        this(name, size, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, path, writer);
    }

    private static DocumentBlock[] convertRawBlocksToBigBlocks(ListManagedBlock[] blocks) throws IOException {
        DocumentBlock[] result = new DocumentBlock[blocks.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new DocumentBlock((RawDataBlock) blocks[i]);
        }
        return result;
    }

    private static SmallDocumentBlock[] convertRawBlocksToSmallBlocks(ListManagedBlock[] blocks) {
        if (blocks instanceof SmallDocumentBlock[]) {
            return (SmallDocumentBlock[]) blocks;
        }
        SmallDocumentBlock[] result = new SmallDocumentBlock[blocks.length];
        System.arraycopy(blocks, 0, result, 0, blocks.length);
        return result;
    }

    public BlockWritable[] getSmallBlocks() {
        return _small_store.getBlocks();
    }

    public int getSize() {
        return _size;
    }

    void read(byte[] buffer, int offset) {
        int len = buffer.length;

        DataInputBlock currentBlock = getDataInputBlock(offset);

        int blockAvailable = currentBlock.available();
        if (blockAvailable > len) {
            currentBlock.readFully(buffer, 0, len);
            return;
        }

        int remaining = len;
        int writePos = 0;
        int currentOffset = offset;
        while (remaining > 0) {
            boolean blockIsExpiring = remaining >= blockAvailable;
            int reqSize;
            if (blockIsExpiring) {
                reqSize = blockAvailable;
            } else {
                reqSize = remaining;
            }
            currentBlock.readFully(buffer, writePos, reqSize);
            remaining -= reqSize;
            writePos += reqSize;
            currentOffset += reqSize;
            if (blockIsExpiring) {
                if (currentOffset == _size) {
                    if (remaining > 0) {
                        throw new IllegalStateException("reached end of document stream unexpectedly");
                    }
                    currentBlock = null;
                    break;
                }
                currentBlock = getDataInputBlock(currentOffset);
                blockAvailable = currentBlock.available();
            }
        }
    }

    DataInputBlock getDataInputBlock(int offset) {
        if (offset >= _size) {
            if (offset > _size) {
                throw new RuntimeException("Request for Offset " + offset + " doc size is " + _size);
            }
            return null;
        }
        if (_property.shouldUseSmallBlocks()) {
            return SmallDocumentBlock.getDataInputBlock(_small_store.getBlocks(), offset);
        }
        return DocumentBlock.getDataInputBlock(_big_store.getBlocks(), offset);
    }

    DocumentProperty getDocumentProperty() {
        return _property;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        _big_store.writeBlocks(stream);
    }

    public int countBlocks() {
        return _big_store.countBlocks();
    }

    public void setStartBlock(int index) {
        _property.setStartBlock(index);
    }

    public Object[] getViewableArray() {
        Object[] results = new Object[1];
        String result;

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            BlockWritable[] blocks = null;

            if (_big_store.isValid()) {
                blocks = _big_store.getBlocks();
            } else if (_small_store.isValid()) {
                blocks = _small_store.getBlocks();
            }
            if (blocks != null) {
                for (int k = 0; k < blocks.length; k++) {
                    blocks[k].writeBlocks(output);
                }
                byte[] data = output.toByteArray();

                if (data.length > _property.getSize()) {
                    byte[] tmp = new byte[_property.getSize()];

                    System.arraycopy(data, 0, tmp, 0, tmp.length);
                    data = tmp;
                }
                output = new ByteArrayOutputStream();
                HexDump.dump(data, 0, output, 0);
                result = output.toString();
            } else {
                result = "<NO DATA>";
            }
        } catch (IOException e) {
            result = e.getMessage();
        }
        results[0] = result;
        return results;
    }

    public Iterator getViewableIterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    public boolean preferArray() {
        return true;
    }

    public String getShortDescription() {

        String buffer = "Document: \"" + _property.getName() + "\"" +
                " size = " + getSize();
        return buffer;
    }

    private static final class SmallBlockStore {
        private final POIFSDocumentPath _path;
        private final String _name;
        private final int _size;
        private final POIFSWriterListener _writer;
        private final POIFSBigBlockSize _bigBlockSize;
        private SmallDocumentBlock[] _smallBlocks;

        SmallBlockStore(POIFSBigBlockSize bigBlockSize, SmallDocumentBlock[] blocks) {
            _bigBlockSize = bigBlockSize;
            _smallBlocks = blocks;
            this._path = null;
            this._name = null;
            this._size = -1;
            this._writer = null;
        }

        SmallBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path,
                        String name, int size, POIFSWriterListener writer) {
            _bigBlockSize = bigBlockSize;
            _smallBlocks = new SmallDocumentBlock[0];
            this._path = path;
            this._name = name;
            this._size = size;
            this._writer = writer;
        }

        boolean isValid() {
            return _smallBlocks.length > 0 || _writer != null;
        }

        SmallDocumentBlock[] getBlocks() {
            if (isValid() && _writer != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream(_size);
                DocumentOutputStream dstream = new DocumentOutputStream(stream, _size);

                _writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, _path, _name, _size));
                _smallBlocks = SmallDocumentBlock.convert(_bigBlockSize, stream.toByteArray(), _size);
            }
            return _smallBlocks;
        }
    }

    private static final class BigBlockStore {
        private final POIFSDocumentPath _path;
        private final String _name;
        private final int _size;
        private final POIFSWriterListener _writer;
        private final POIFSBigBlockSize _bigBlockSize;
        private DocumentBlock[] bigBlocks;

        BigBlockStore(POIFSBigBlockSize bigBlockSize, DocumentBlock[] blocks) {
            _bigBlockSize = bigBlockSize;
            bigBlocks = blocks;
            _path = null;
            _name = null;
            _size = -1;
            _writer = null;
        }

        BigBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path,
                      String name, int size, POIFSWriterListener writer) {
            _bigBlockSize = bigBlockSize;
            bigBlocks = new DocumentBlock[0];
            _path = path;
            _name = name;
            _size = size;
            _writer = writer;
        }

        boolean isValid() {
            return bigBlocks.length > 0 || _writer != null;
        }

        DocumentBlock[] getBlocks() {
            if (isValid() && _writer != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream(_size);
                DocumentOutputStream dstream = new DocumentOutputStream(stream, _size);

                _writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, _path, _name, _size));
                bigBlocks = DocumentBlock.convert(_bigBlockSize, stream.toByteArray(), _size);
            }
            return bigBlocks;
        }

        void writeBlocks(OutputStream stream) throws IOException {
            if (isValid()) {
                if (_writer != null) {
                    DocumentOutputStream dstream = new DocumentOutputStream(stream, _size);

                    _writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, _path, _name, _size));
                    dstream.writeFiller(countBlocks() * _bigBlockSize.getBigBlockSize(),
                            DocumentBlock.getFillByte());
                } else {
                    for (int k = 0; k < bigBlocks.length; k++) {
                        bigBlocks[k].writeBlocks(stream);
                    }
                }
            }
        }

        int countBlocks() {

            if (isValid()) {
                if (_writer == null) {
                    return bigBlocks.length;
                }
                return (_size + _bigBlockSize.getBigBlockSize() - 1)
                        / _bigBlockSize.getBigBlockSize();
            }
            return 0;
        }
    }
}
