package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

public class POIFSWriterEvent {
    private final DocumentOutputStream stream;
    private final POIFSDocumentPath path;
    private final String documentName;
    private final int limit;

    POIFSWriterEvent(final DocumentOutputStream stream,
                     final POIFSDocumentPath path, final String documentName,
                     final int limit) {
        this.stream = stream;
        this.path = path;
        this.documentName = documentName;
        this.limit = limit;
    }

    public DocumentOutputStream getStream() {
        return stream;
    }

    public POIFSDocumentPath getPath() {
        return path;
    }

    public String getName() {
        return documentName;
    }

    public int getLimit() {
        return limit;
    }
}   

