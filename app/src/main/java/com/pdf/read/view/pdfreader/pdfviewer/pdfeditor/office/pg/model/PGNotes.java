package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model;

public class PGNotes {
    private String notes;

    public PGNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void dispose() {
        notes = null;
    }
}
