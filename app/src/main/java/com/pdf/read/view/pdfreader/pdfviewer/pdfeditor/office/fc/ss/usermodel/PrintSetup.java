package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface PrintSetup {

    short LETTER_PAPERSIZE = 1;

    short LETTER_SMALL_PAGESIZE = 2;

    short TABLOID_PAPERSIZE = 3;

    short LEDGER_PAPERSIZE = 4;

    short LEGAL_PAPERSIZE = 5;

    short STATEMENT_PAPERSIZE = 6;

    short EXECUTIVE_PAPERSIZE = 7;

    short A3_PAPERSIZE = 8;

    short A4_PAPERSIZE = 9;

    short A4_SMALL_PAPERSIZE = 10;

    short A5_PAPERSIZE = 11;

    short B4_PAPERSIZE = 12;

    short B5_PAPERSIZE = 13;

    short FOLIO8_PAPERSIZE = 14;

    short QUARTO_PAPERSIZE = 15;

    short TEN_BY_FOURTEEN_PAPERSIZE = 16;

    short ELEVEN_BY_SEVENTEEN_PAPERSIZE = 17;

    short NOTE8_PAPERSIZE = 18;

    short ENVELOPE_9_PAPERSIZE = 19;

    short ENVELOPE_10_PAPERSIZE = 20;

    short ENVELOPE_DL_PAPERSIZE = 27;

    short ENVELOPE_CS_PAPERSIZE = 28;
    short ENVELOPE_C5_PAPERSIZE = 28;

    short ENVELOPE_C3_PAPERSIZE = 29;

    short ENVELOPE_C4_PAPERSIZE = 30;

    short ENVELOPE_C6_PAPERSIZE = 31;

    short ENVELOPE_MONARCH_PAPERSIZE = 37;

    short A4_EXTRA_PAPERSIZE = 53;

    short A4_TRANSVERSE_PAPERSIZE = 55;

    short A4_PLUS_PAPERSIZE = 60;

    short LETTER_ROTATED_PAPERSIZE = 75;

    short A4_ROTATED_PAPERSIZE = 77;

    short getPaperSize();

    void setPaperSize(short size);

    short getScale();

    void setScale(short scale);

    short getPageStart();

    void setPageStart(short start);

    short getFitWidth();

    void setFitWidth(short width);

    short getFitHeight();

    void setFitHeight(short height);

    boolean getLeftToRight();

    void setLeftToRight(boolean ltor);

    boolean getLandscape();

    void setLandscape(boolean ls);

    boolean getValidSettings();

    void setValidSettings(boolean valid);

    boolean getNoColor();

    void setNoColor(boolean mono);

    boolean getDraft();

    void setDraft(boolean d);

    boolean getNotes();

    void setNotes(boolean printnotes);

    boolean getNoOrientation();

    void setNoOrientation(boolean orientation);

    boolean getUsePage();

    void setUsePage(boolean page);

    short getHResolution();

    void setHResolution(short resolution);

    short getVResolution();

    void setVResolution(short resolution);

    double getHeaderMargin();

    void setHeaderMargin(double headermargin);

    double getFooterMargin();

    void setFooterMargin(double footermargin);

    short getCopies();

    void setCopies(short copies);

}
