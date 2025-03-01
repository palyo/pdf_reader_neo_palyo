package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DirectoryEntry;

public abstract class SpecialPropertySet extends MutablePropertySet {
    private final MutablePropertySet delegate;

    public SpecialPropertySet(final PropertySet ps) {
        delegate = new MutablePropertySet(ps);
    }

    public SpecialPropertySet(final MutablePropertySet ps) {
        delegate = ps;
    }

    public abstract PropertyIDMap getPropertySetIDMap();

    public int getByteOrder() {
        return delegate.getByteOrder();
    }

    public void setByteOrder(final int byteOrder) {
        delegate.setByteOrder(byteOrder);
    }

    public int getFormat() {
        return delegate.getFormat();
    }

    public void setFormat(final int format) {
        delegate.setFormat(format);
    }

    public int getOSVersion() {
        return delegate.getOSVersion();
    }

    public void setOSVersion(final int osVersion) {
        delegate.setOSVersion(osVersion);
    }

    public ClassID getClassID() {
        return delegate.getClassID();
    }

    public void setClassID(final ClassID classID) {
        delegate.setClassID(classID);
    }

    public int getSectionCount() {
        return delegate.getSectionCount();
    }

    public List getSections() {
        return delegate.getSections();
    }

    public boolean isSummaryInformation() {
        return delegate.isSummaryInformation();
    }

    public boolean isDocumentSummaryInformation() {
        return delegate.isDocumentSummaryInformation();
    }

    public Section getFirstSection() {
        return delegate.getFirstSection();
    }

    public void addSection(final Section section) {
        delegate.addSection(section);
    }

    public void clearSections() {
        delegate.clearSections();
    }

    public InputStream toInputStream() throws IOException, WritingNotSupportedException {
        return delegate.toInputStream();
    }

    public void write(final DirectoryEntry dir, final String name) throws WritingNotSupportedException, IOException {
        delegate.write(dir, name);
    }

    public void write(final OutputStream out) throws WritingNotSupportedException, IOException {
        delegate.write(out);
    }

    public boolean equals(final Object o) {
        return delegate.equals(o);
    }

    public Property[] getProperties() throws HPSFRuntimeException {
        return delegate.getProperties();
    }

    protected Object getProperty(final int id) throws HPSFRuntimeException {
        return delegate.getProperty(id);
    }

    protected boolean getPropertyBooleanValue(final int id) throws HPSFRuntimeException {
        return delegate.getPropertyBooleanValue(id);
    }

    protected int getPropertyIntValue(final int id) throws HPSFRuntimeException {
        return delegate.getPropertyIntValue(id);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public String toString() {
        return delegate.toString();
    }

    public boolean wasNull() throws HPSFRuntimeException {
        return delegate.wasNull();
    }
}
