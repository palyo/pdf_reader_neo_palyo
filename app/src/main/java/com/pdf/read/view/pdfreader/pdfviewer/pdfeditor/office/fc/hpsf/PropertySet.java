package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class PropertySet {

    public static final int OS_WIN16 = 0x0000;
    public static final int OS_MACINTOSH = 0x0001;
    public static final int OS_WIN32 = 0x0002;
    static final byte[] BYTE_ORDER_ASSERTION =
            new byte[]{(byte) 0xFE, (byte) 0xFF};
    static final byte[] FORMAT_ASSERTION =
            new byte[]{(byte) 0x00, (byte) 0x00};
    protected int byteOrder;
    protected int format;
    protected int osVersion;
    protected ClassID classID;
    protected List sections;

    protected PropertySet() {
    }

    public PropertySet(final InputStream stream)
            throws HPSFException,
            IOException {
        if (isPropertySetStream(stream)) {
            final int avail = stream.available();
            final byte[] buffer = new byte[avail];
            stream.read(buffer, 0, buffer.length);
            init(buffer, 0, buffer.length);
        } else
            throw new HPSFException();
    }

    public PropertySet(final byte[] stream, final int offset, final int length)
            throws HPSFException, UnsupportedEncodingException {
        if (isPropertySetStream(stream, offset, length))
            init(stream, offset, length);
        else
            throw new HPSFException();
    }

    public PropertySet(final byte[] stream)
            throws HPSFException, UnsupportedEncodingException {
        this(stream, 0, stream.length);
    }

    public static boolean isPropertySetStream(final InputStream stream)
            throws MarkUnsupportedException, IOException {

        final int BUFFER_SIZE = 50;

        if (!stream.markSupported())
            throw new MarkUnsupportedException(stream.getClass().getName());
        stream.mark(BUFFER_SIZE);

        final byte[] buffer = new byte[BUFFER_SIZE];
        final int bytes =
                stream.read(buffer, 0,
                        Math.min(buffer.length, stream.available()));
        final boolean isPropertySetStream =
                isPropertySetStream(buffer, 0, bytes);
        stream.reset();
        return isPropertySetStream;
    }

    public static boolean isPropertySetStream(final byte[] src,
                                              final int offset,
                                              final int length) {

        int o = offset;
        final int byteOrder = LittleEndian.getUShort(src, o);
        o += LittleEndian.SHORT_SIZE;
        byte[] temp = new byte[LittleEndian.SHORT_SIZE];
        LittleEndian.putShort(temp, (short) byteOrder);
        if (!Util.equal(temp, BYTE_ORDER_ASSERTION))
            return false;
        final int format = LittleEndian.getUShort(src, o);
        o += LittleEndian.SHORT_SIZE;
        temp = new byte[LittleEndian.SHORT_SIZE];
        LittleEndian.putShort(temp, (short) format);
        if (!Util.equal(temp, FORMAT_ASSERTION))
            return false;

        o += LittleEndian.INT_SIZE;

        o += ClassID.LENGTH;
        final long sectionCount = LittleEndian.getUInt(src, o);
        o += LittleEndian.INT_SIZE;
        return sectionCount >= 0;
    }

    public int getByteOrder() {
        return byteOrder;
    }

    public int getFormat() {
        return format;
    }

    public int getOSVersion() {
        return osVersion;
    }

    public ClassID getClassID() {
        return classID;
    }

    public int getSectionCount() {
        return sections.size();
    }

    public List getSections() {
        return sections;
    }

    private void init(final byte[] src, final int offset, final int length)
            throws UnsupportedEncodingException {

        int o = offset;
        byteOrder = LittleEndian.getUShort(src, o);
        o += LittleEndian.SHORT_SIZE;
        format = LittleEndian.getUShort(src, o);
        o += LittleEndian.SHORT_SIZE;
        osVersion = (int) LittleEndian.getUInt(src, o);
        o += LittleEndian.INT_SIZE;
        classID = new ClassID(src, o);
        o += ClassID.LENGTH;
        final int sectionCount = LittleEndian.getInt(src, o);
        o += LittleEndian.INT_SIZE;
        if (sectionCount < 0)
            throw new HPSFRuntimeException("Section count " + sectionCount +
                    " is negative.");

        sections = new ArrayList(sectionCount);

        for (int i = 0; i < sectionCount; i++) {
            final Section s = new Section(src, o);
            o += ClassID.LENGTH + LittleEndian.INT_SIZE;
            sections.add(s);
        }
    }

    public boolean isSummaryInformation() {
        if (sections.size() <= 0)
            return false;
        return Util.equal(((Section) sections.get(0)).getFormatID().getBytes(),
                SectionIDMap.SUMMARY_INFORMATION_ID);
    }

    public boolean isDocumentSummaryInformation() {
        if (sections.size() <= 0)
            return false;
        return Util.equal(((Section) sections.get(0)).getFormatID().getBytes(),
                SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[0]);
    }

    public Property[] getProperties()
            throws HPSFRuntimeException {
        return getFirstSection().getProperties();
    }

    protected Object getProperty(final int id) throws HPSFRuntimeException {
        return getFirstSection().getProperty(id);
    }

    protected boolean getPropertyBooleanValue(final int id)
            throws HPSFRuntimeException {
        return getFirstSection().getPropertyBooleanValue(id);
    }

    protected int getPropertyIntValue(final int id)
            throws HPSFRuntimeException {
        return getFirstSection().getPropertyIntValue(id);
    }

    public boolean wasNull() throws HPSFRuntimeException {
        return getFirstSection().wasNull();
    }

    public Section getFirstSection() {
        if (getSectionCount() < 1)
            throw new MissingSectionException("Property set does not contain any sections.");
        return ((Section) sections.get(0));
    }

    public Section getSingleSection() {
        final int sectionCount = getSectionCount();
        if (sectionCount != 1)
            throw new HPSFRuntimeException
                    ("Property set contains " + sectionCount + " sections.");
        return ((Section) sections.get(0));
    }

    public boolean equals(final Object o) {
        if (o == null || !(o instanceof PropertySet))
            return false;
        final PropertySet ps = (PropertySet) o;
        int byteOrder1 = ps.getByteOrder();
        int byteOrder2 = getByteOrder();
        ClassID classID1 = ps.getClassID();
        ClassID classID2 = getClassID();
        int format1 = ps.getFormat();
        int format2 = getFormat();
        int osVersion1 = ps.getOSVersion();
        int osVersion2 = getOSVersion();
        int sectionCount1 = ps.getSectionCount();
        int sectionCount2 = getSectionCount();
        if (byteOrder1 != byteOrder2 ||
                !classID1.equals(classID2) ||
                format1 != format2 ||
                osVersion1 != osVersion2 ||
                sectionCount1 != sectionCount2)
            return false;

        return Util.equals(getSections(), ps.getSections());
    }

    public int hashCode() {
        throw new UnsupportedOperationException("FIXME: Not yet implemented.");
    }

    public String toString() {
        final StringBuffer b = new StringBuffer();
        final int sectionCount = getSectionCount();
        b.append(getClass().getName());
        b.append('[');
        b.append("byteOrder: ");
        b.append(getByteOrder());
        b.append(", classID: ");
        b.append(getClassID());
        b.append(", format: ");
        b.append(getFormat());
        b.append(", OSVersion: ");
        b.append(getOSVersion());
        b.append(", sectionCount: ");
        b.append(sectionCount);
        b.append(", sections: [\n");
        final List sections = getSections();
        for (int i = 0; i < sectionCount; i++)
            b.append(sections.get(i).toString());
        b.append(']');
        b.append(']');
        return b.toString();
    }
}
