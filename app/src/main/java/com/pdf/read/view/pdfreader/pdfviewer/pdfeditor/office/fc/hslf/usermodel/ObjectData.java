package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.ExOleObjStg;

public class ObjectData {
    private ExOleObjStg storage;

    public ObjectData(ExOleObjStg storage) {
        this.storage = storage;
    }

    public InputStream getData() {
        return storage.getData();
    }

    public void setData(byte[] data) throws IOException {
        storage.setData(data);
    }

    public ExOleObjStg getExOleObjStg() {
        return storage;
    }

    public void dispose() {
        if (storage != null) {
            storage.dispose();
            storage = null;
        }
    }
}
