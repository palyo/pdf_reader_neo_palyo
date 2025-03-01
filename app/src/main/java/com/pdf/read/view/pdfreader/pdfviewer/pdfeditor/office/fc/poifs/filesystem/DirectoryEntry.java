package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf.ClassID;

public interface DirectoryEntry
        extends Entry, Iterable<Entry> {

    Iterator<Entry> getEntries();

    boolean isEmpty();

    int getEntryCount();

    boolean hasEntry(final String name);

    Entry getEntry(final String name)
            throws FileNotFoundException;

    DocumentEntry createDocument(final String name,
                                 final InputStream stream)
            throws IOException;

    DocumentEntry createDocument(final String name, final int size,
                                 final POIFSWriterListener writer)
            throws IOException;

    DirectoryEntry createDirectory(final String name)
            throws IOException;

    ClassID getStorageClsid();

    void setStorageClsid(ClassID clsidStorage);

}   

