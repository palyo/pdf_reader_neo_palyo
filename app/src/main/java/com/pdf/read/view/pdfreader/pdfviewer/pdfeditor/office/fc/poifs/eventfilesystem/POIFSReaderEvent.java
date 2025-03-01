package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.eventfilesystem;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.POIFSDocumentPath;

public class POIFSReaderEvent {
    private final DocumentInputStream stream;
    private final POIFSDocumentPath path;
    private final String documentName;

    POIFSReaderEvent(final DocumentInputStream stream,
                     final POIFSDocumentPath path, final String documentName) {
        this.stream = stream;
        this.path = path;
        this.documentName = documentName;
    }

    public DocumentInputStream getStream() {
        return stream;
    }

    public POIFSDocumentPath getPath() {
        return path;
    }

    public String getName() {
        return documentName;
    }
}   

