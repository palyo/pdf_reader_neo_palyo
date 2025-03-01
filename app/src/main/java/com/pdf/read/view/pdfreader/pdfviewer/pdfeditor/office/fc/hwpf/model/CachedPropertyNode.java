package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.lang.ref.SoftReference;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm.SprmBuffer;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class CachedPropertyNode extends PropertyNode<CachedPropertyNode> {
    private SoftReference<Object> _propCache;

    public CachedPropertyNode(int start, int end, SprmBuffer buf) {
        super(start, end, buf);
    }

    private void fillCache(Object ref) {
        _propCache = new SoftReference<Object>(ref);
    }

    private Object getCacheContents() {
        return _propCache == null ? null : _propCache.get();
    }

    public SprmBuffer getSprmBuf() {
        return (SprmBuffer) _buf;
    }

}
