package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.sl.usermodel;

public interface Slide extends Sheet {
    Notes getNotes();

    void setNotes(Notes notes);

    boolean getFollowMasterBackground();

    void setFollowMasterBackground(boolean follow);

    boolean getFollowMasterColourScheme();

    void setFollowMasterColourScheme(boolean follow);

    boolean getFollowMasterObjects();

    void setFollowMasterObjects(boolean follow);
}
