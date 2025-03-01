package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem;

public interface Entry {

    String getName();

    boolean isDirectoryEntry();

    boolean isDocumentEntry();

    DirectoryEntry getParent();

    boolean delete();

    boolean renameTo(final String newName);
}   

