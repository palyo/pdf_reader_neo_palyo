package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

import java.io.File;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;

public class POIFSDocumentPath {
    private static final POILogger log = POILogFactory.getLogger(POIFSDocumentPath.class);

    private String[] components;
    private int hashcode = 0;

    public POIFSDocumentPath(final String[] components)
            throws IllegalArgumentException {
        if (components == null) {
            this.components = new String[0];
        } else {
            this.components = new String[components.length];
            for (int j = 0; j < components.length; j++) {
                if ((components[j] == null)
                        || (components[j].length() == 0)) {
                    throw new IllegalArgumentException(
                            "components cannot contain null or empty strings");
                }
                this.components[j] = components[j];
            }
        }
    }

    public POIFSDocumentPath() {
        this.components = new String[0];
    }

    public POIFSDocumentPath(final POIFSDocumentPath path,
                             final String[] components)
            throws IllegalArgumentException {
        if (components == null) {
            this.components = new String[path.components.length];
        } else {
            this.components =
                    new String[path.components.length + components.length];
        }
        System.arraycopy(path.components, 0, this.components, 0, path.components.length);
        if (components != null) {
            for (int j = 0; j < components.length; j++) {
                if (components[j] == null) {
                    throw new IllegalArgumentException(
                            "components cannot contain null");
                }
                if (components[j].length() == 0) {
                    log.log(POILogger.WARN, "Directory under " + path + " has an empty name, " +
                            "not all OLE2 readers will handle this file correctly!");
                }

                this.components[j + path.components.length] =
                        components[j];
            }
        }
    }

    public boolean equals(final Object o) {
        boolean rval = false;

        if ((o != null) && (o.getClass() == this.getClass())) {
            if (this == o) {
                rval = true;
            } else {
                POIFSDocumentPath path = (POIFSDocumentPath) o;

                if (path.components.length == this.components.length) {
                    rval = true;
                    for (int j = 0; j < this.components.length; j++) {
                        if (!path.components[j]
                                .equals(this.components[j])) {
                            rval = false;
                            break;
                        }
                    }
                }
            }
        }
        return rval;
    }

    public int hashCode() {
        if (hashcode == 0) {
            for (int j = 0; j < components.length; j++) {
                hashcode += components[j].hashCode();
            }
        }
        return hashcode;
    }

    public int length() {
        return components.length;
    }

    public String getComponent(int n)
            throws ArrayIndexOutOfBoundsException {
        return components[n];
    }

    public POIFSDocumentPath getParent() {
        final int length = components.length - 1;

        if (length < 0) {
            return null;
        }
        POIFSDocumentPath parent = new POIFSDocumentPath(null);

        parent.components = new String[length];
        System.arraycopy(components, 0, parent.components, 0, length);
        return parent;
    }

    public String toString() {
        final StringBuffer b = new StringBuffer();
        final int l = length();

        b.append(File.separatorChar);
        for (int i = 0; i < l; i++) {
            b.append(getComponent(i));
            if (i < l - 1) {
                b.append(File.separatorChar);
            }
        }
        return b.toString();
    }
}   

