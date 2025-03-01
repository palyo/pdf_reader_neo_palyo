package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.objectpool;

import java.util.Vector;

public class AllocPool {

    private final IMemObj prototype;

    private final Vector<IMemObj> free;

    public AllocPool(IMemObj prototype, int capacity, int capacityIncrement) {
        this.prototype = prototype;
        free = new Vector<IMemObj>(capacity, capacityIncrement);
    }

    public synchronized IMemObj allocObject() {
        if (free.size() > 0) {
            IMemObj obj = free.remove(0);
            return obj;
        }
        IMemObj obj = prototype.getCopy();
        return obj;
    }

    public synchronized void free(IMemObj obj) {
        free.add(obj);
    }

    public synchronized void dispose() {
        if (free != null) {
            free.clear();
        }
    }
}
