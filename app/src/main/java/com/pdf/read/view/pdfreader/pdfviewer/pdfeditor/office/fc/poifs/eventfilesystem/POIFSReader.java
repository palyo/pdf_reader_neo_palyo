package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.eventfilesystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSDocumentPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.DirectoryProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.Property;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property.PropertyTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.BlockAllocationTableReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.BlockList;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.HeaderBlock;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.RawDataBlockList;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.SmallBlockTableReader;

public class POIFSReader {
    private final POIFSReaderRegistry registry;
    private boolean registryClosed;

    public POIFSReader() {
        registry = new POIFSReaderRegistry();
        registryClosed = false;
    }

    public static void main(String[] args)
            throws IOException {
        if (args.length == 0) {
            System.err
                    .println("at least one argument required: input filename(s)");
            System.exit(1);
        }

        for (int j = 0; j < args.length; j++) {
            POIFSReader reader = new POIFSReader();
            POIFSReaderListener listener = new SampleListener();

            reader.registerListener(listener);
            System.out.println("reading " + args[j]);
            FileInputStream istream = new FileInputStream(args[j]);

            reader.read(istream);
            istream.close();
        }
    }

    public void read(final InputStream stream)
            throws IOException {
        registryClosed = true;

        HeaderBlock header_block = new HeaderBlock(stream);

        RawDataBlockList data_blocks = new RawDataBlockList(stream, header_block.getBigBlockSize());

        new BlockAllocationTableReader(header_block.getBigBlockSize(),
                header_block.getBATCount(),
                header_block.getBATArray(),
                header_block.getXBATCount(),
                header_block.getXBATIndex(),
                data_blocks);

        PropertyTable properties =
                new PropertyTable(header_block, data_blocks);

        processProperties(SmallBlockTableReader
                        .getSmallDocumentBlocks(
                                header_block.getBigBlockSize(),
                                data_blocks, properties.getRoot(),
                                header_block.getSBATStart()),
                data_blocks, properties.getRoot()
                        .getChildren(), new POIFSDocumentPath());
    }

    public void registerListener(final POIFSReaderListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (registryClosed) {
            throw new IllegalStateException();
        }
        registry.registerListener(listener);
    }

    public void registerListener(final POIFSReaderListener listener,
                                 final String name) {
        registerListener(listener, null, name);
    }

    public void registerListener(final POIFSReaderListener listener,
                                 final POIFSDocumentPath path,
                                 final String name) {
        if ((listener == null) || (name == null) || (name.length() == 0)) {
            throw new NullPointerException();
        }
        if (registryClosed) {
            throw new IllegalStateException();
        }
        registry.registerListener(listener,
                (path == null) ? new POIFSDocumentPath()
                        : path, name);
    }

    private void processProperties(final BlockList small_blocks,
                                   final BlockList big_blocks,
                                   final Iterator properties,
                                   final POIFSDocumentPath path)
            throws IOException {
        while (properties.hasNext()) {
            Property property = (Property) properties.next();
            String name = property.getName();

            if (property.isDirectory()) {
                POIFSDocumentPath new_path = new POIFSDocumentPath(path,
                        new String[]
                                {
                                        name
                                });

                processProperties(
                        small_blocks, big_blocks,
                        ((DirectoryProperty) property).getChildren(), new_path);
            } else {
                int startBlock = property.getStartBlock();
                Iterator listeners = registry.getListeners(path, name);

                if (listeners.hasNext()) {
                    int size = property.getSize();
                    POIFSDocument document = null;

                    if (property.shouldUseSmallBlocks()) {
                        document =
                                new POIFSDocument(name, small_blocks
                                        .fetchBlocks(startBlock, -1), size);
                    } else {
                        document =
                                new POIFSDocument(name, big_blocks
                                        .fetchBlocks(startBlock, -1), size);
                    }
                    while (listeners.hasNext()) {
                        POIFSReaderListener listener =
                                (POIFSReaderListener) listeners.next();

                        listener.processPOIFSReaderEvent(
                                new POIFSReaderEvent(
                                        new DocumentInputStream(document), path,
                                        name));
                    }
                } else {

                    if (property.shouldUseSmallBlocks()) {
                        small_blocks.fetchBlocks(startBlock, -1);
                    } else {
                        big_blocks.fetchBlocks(startBlock, -1);
                    }
                }
            }
        }
    }

    private static class SampleListener
            implements POIFSReaderListener {

        SampleListener() {
        }

        public void processPOIFSReaderEvent(final POIFSReaderEvent event) {
            DocumentInputStream istream = event.getStream();
            POIFSDocumentPath path = event.getPath();
            String name = event.getName();

            try {
                byte[] data = new byte[istream.available()];

                istream.read(data);
                int pathLength = path.length();

                for (int k = 0; k < pathLength; k++) {
                    System.out.print("/" + path.getComponent(k));
                }
                System.out.println("/" + name + ": " + data.length
                        + " bytes read");
            } catch (IOException ignored) {
            }
        }
    }
}       

