package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DirectoryEntry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentEntry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.Entry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSFileSystem;

@Internal
public class POIUtils {

    @Internal
    public static void copyNodeRecursively(Entry entry, DirectoryEntry target)
            throws IOException {

        DirectoryEntry newTarget = null;
        if (entry.isDirectoryEntry()) {
            newTarget = target.createDirectory(entry.getName());
            Iterator<Entry> entries = ((DirectoryEntry) entry).getEntries();

            while (entries.hasNext()) {
                copyNodeRecursively(entries.next(), newTarget);
            }
        } else {
            DocumentEntry dentry = (DocumentEntry) entry;
            DocumentInputStream dstream = new DocumentInputStream(dentry);
            target.createDocument(dentry.getName(), dstream);
            dstream.close();
        }
    }

    public static void copyNodes(DirectoryEntry sourceRoot,
                                 DirectoryEntry targetRoot, List<String> excepts)
            throws IOException {
        Iterator<Entry> entries = sourceRoot.getEntries();
        while (entries.hasNext()) {
            Entry entry = entries.next();
            if (!excepts.contains(entry.getName())) {
                copyNodeRecursively(entry, targetRoot);
            }
        }
    }

    public static void copyNodes(POIFSFileSystem source,
                                 POIFSFileSystem target, List<String> excepts) throws IOException {

        copyNodes(source.getRoot(), target.getRoot(), excepts);
    }
}
