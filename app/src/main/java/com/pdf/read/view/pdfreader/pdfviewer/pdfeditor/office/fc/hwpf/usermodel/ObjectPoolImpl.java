package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DirectoryEntry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.Entry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POIUtils;

@Internal
public class ObjectPoolImpl implements ObjectsPool {
    private final DirectoryEntry _objectPool;

    public ObjectPoolImpl(DirectoryEntry _objectPool) {
        super();
        this._objectPool = _objectPool;
    }

    public Entry getObjectById(String objId) {
        if (_objectPool == null)
            return null;

        try {
            return _objectPool.getEntry(objId);
        } catch (FileNotFoundException exc) {
            return null;
        }
    }

    @Internal
    public void writeTo(DirectoryEntry directoryEntry) throws IOException {
        if (_objectPool != null)
            POIUtils.copyNodeRecursively(_objectPool, directoryEntry);
    }
}
