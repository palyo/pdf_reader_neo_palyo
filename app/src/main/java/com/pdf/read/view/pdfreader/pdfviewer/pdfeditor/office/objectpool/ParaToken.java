package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.objectpool;

public class ParaToken {

    private boolean isFree;

    private IMemObj obj;

    public ParaToken(IMemObj obj) {
        this.obj = obj;
    }

    public void free() {
        obj.free();
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        this.isFree = free;
    }

    public void dispose() {
        obj = null;
    }
}
