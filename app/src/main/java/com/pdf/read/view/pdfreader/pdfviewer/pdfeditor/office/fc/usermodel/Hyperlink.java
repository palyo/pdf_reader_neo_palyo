package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.usermodel;

public interface Hyperlink {

    int LINK_URL = 1;

    int LINK_DOCUMENT = 2;

    int LINK_EMAIL = 3;

    int LINK_FILE = 4;

    String getAddress();

    void setAddress(String address);

    String getLabel();

    void setLabel(String label);

    int getType();
}
