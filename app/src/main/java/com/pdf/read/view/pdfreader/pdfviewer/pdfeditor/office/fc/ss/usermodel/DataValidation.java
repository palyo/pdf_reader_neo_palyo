package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellRangeAddressList;

public interface DataValidation {
    DataValidationConstraint getValidationConstraint();

    int getErrorStyle();

    void setErrorStyle(int error_style);

    boolean getEmptyCellAllowed();

    void setEmptyCellAllowed(boolean allowed);

    boolean getSuppressDropDownArrow();

    void setSuppressDropDownArrow(boolean suppress);

    boolean getShowPromptBox();

    void setShowPromptBox(boolean show);

    boolean getShowErrorBox();

    void setShowErrorBox(boolean show);

    void createPromptBox(String title, String text);

    String getPromptBoxTitle();

    String getPromptBoxText();

    void createErrorBox(String title, String text);

    String getErrorBoxTitle();

    String getErrorBoxText();

    CellRangeAddressList getRegions();

    final class ErrorStyle {

        public static final int STOP = 0x00;

        public static final int WARNING = 0x01;

        public static final int INFO = 0x02;
    }

}
