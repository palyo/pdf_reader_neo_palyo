package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DirectoryEntry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.Entry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianConsts;

public class MutablePropertySet extends PropertySet {

    private final int OFFSET_HEADER =
            BYTE_ORDER_ASSERTION.length +
                    FORMAT_ASSERTION.length +
                    LittleEndianConsts.INT_SIZE +
                    ClassID.LENGTH +
                    LittleEndianConsts.INT_SIZE;

    public MutablePropertySet() {

        byteOrder = LittleEndian.getUShort(BYTE_ORDER_ASSERTION);

        format = LittleEndian.getUShort(FORMAT_ASSERTION);

        osVersion = (OS_WIN32 << 16) | 0x0A04;

        classID = new ClassID();

        sections = new LinkedList();
        sections.add(new MutableSection());
    }

    public MutablePropertySet(final PropertySet ps) {
        byteOrder = ps.getByteOrder();
        format = ps.getFormat();
        osVersion = ps.getOSVersion();
        setClassID(ps.getClassID());
        clearSections();
        if (sections == null)
            sections = new LinkedList();
        for (final Iterator i = ps.getSections().iterator(); i.hasNext(); ) {
            final MutableSection s = new MutableSection((Section) (i.next()));
            addSection(s);
        }
    }

    public void setByteOrder(final int byteOrder) {
        this.byteOrder = byteOrder;
    }

    public void setFormat(final int format) {
        this.format = format;
    }

    public void setOSVersion(final int osVersion) {
        this.osVersion = osVersion;
    }

    public void setClassID(final ClassID classID) {
        this.classID = classID;
    }

    public void clearSections() {
        sections = null;
    }

    public void addSection(final Section section) {
        if (sections == null)
            sections = new LinkedList();
        sections.add(section);
    }

    public void write(final OutputStream out)
            throws WritingNotSupportedException, IOException {

        final int nrSections = sections.size();
        int length = 0;

        length += TypeWriter.writeToStream(out, (short) getByteOrder());
        length += TypeWriter.writeToStream(out, (short) getFormat());
        length += TypeWriter.writeToStream(out, getOSVersion());
        length += TypeWriter.writeToStream(out, getClassID());
        length += TypeWriter.writeToStream(out, nrSections);
        int offset = OFFSET_HEADER;

        offset += nrSections * (ClassID.LENGTH + LittleEndian.INT_SIZE);
        final int sectionsBegin = offset;
        for (final ListIterator i = sections.listIterator(); i.hasNext(); ) {
            final MutableSection s = (MutableSection) i.next();
            final ClassID formatID = s.getFormatID();
            if (formatID == null)
                throw new HPSFRuntimeException();
            length += TypeWriter.writeToStream(out, s.getFormatID());
            length += TypeWriter.writeUIntToStream(out, offset);
            try {
                offset += s.getSize();
            } catch (HPSFRuntimeException ex) {
                final Throwable cause = ex.getReason();
                if (cause instanceof UnsupportedEncodingException) {
                    throw new IllegalPropertySetDataException(cause);
                }
                throw ex;
            }
        }

        offset = sectionsBegin;
        for (final ListIterator i = sections.listIterator(); i.hasNext(); ) {
            final MutableSection s = (MutableSection) i.next();
            offset += s.write(out);
        }
    }

    public InputStream toInputStream()
            throws IOException, WritingNotSupportedException {
        final ByteArrayOutputStream psStream = new ByteArrayOutputStream();
        write(psStream);
        psStream.close();
        final byte[] streamData = psStream.toByteArray();
        return new ByteArrayInputStream(streamData);
    }

    public void write(final DirectoryEntry dir, final String name)
            throws WritingNotSupportedException, IOException {

        try {
            final Entry e = dir.getEntry(name);
            e.delete();
        } catch (FileNotFoundException ex) {

        }

        dir.createDocument(name, toInputStream());
    }
}
