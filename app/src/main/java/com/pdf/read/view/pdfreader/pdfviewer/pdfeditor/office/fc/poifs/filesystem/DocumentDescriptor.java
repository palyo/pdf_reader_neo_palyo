package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

public class DocumentDescriptor {
    private final POIFSDocumentPath path;
    private final String name;
    private int hashcode = 0;

    public DocumentDescriptor(final POIFSDocumentPath path, final String name) {
        if (path == null) {
            throw new NullPointerException("path must not be null");
        }
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.path = path;
        this.name = name;
    }

    public boolean equals(final Object o) {
        boolean rval = false;

        if ((o != null) && (o.getClass() == this.getClass())) {
            if (this == o) {
                rval = true;
            } else {
                DocumentDescriptor descriptor = (DocumentDescriptor) o;

                rval = this.path.equals(descriptor.path)
                        && this.name.equals(descriptor.name);
            }
        }
        return rval;
    }

    public int hashCode() {
        if (hashcode == 0) {
            hashcode = path.hashCode() ^ name.hashCode();
        }
        return hashcode;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(40 * (path.length() + 1));

        for (int j = 0; j < path.length(); j++) {
            buffer.append(path.getComponent(j)).append("/");
        }
        buffer.append(name);
        return buffer.toString();
    }
}   

