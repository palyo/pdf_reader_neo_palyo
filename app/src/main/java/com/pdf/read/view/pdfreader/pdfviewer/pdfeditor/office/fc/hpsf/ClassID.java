package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;

public class ClassID {

    public static final int LENGTH = 16;
    protected byte[] bytes;

    public ClassID(final byte[] src, final int offset) {
        read(src, offset);
    }

    public ClassID() {
        bytes = new byte[LENGTH];
        for (int i = 0; i < LENGTH; i++)
            bytes[i] = 0x00;
    }

    public int length() {
        return LENGTH;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(final byte[] bytes) {
        System.arraycopy(bytes, 0, this.bytes, 0, this.bytes.length);
    }

    public byte[] read(final byte[] src, final int offset) {
        bytes = new byte[16];

        bytes[0] = src[3 + offset];
        bytes[1] = src[2 + offset];
        bytes[2] = src[1 + offset];
        bytes[3] = src[offset];

        bytes[4] = src[5 + offset];
        bytes[5] = src[4 + offset];

        bytes[6] = src[7 + offset];
        bytes[7] = src[6 + offset];

        System.arraycopy(src, 8 + offset, bytes, 8, 8);

        return bytes;
    }

    public void write(final byte[] dst, final int offset)
            throws ArrayStoreException {

        if (dst.length < 16)
            throw new ArrayStoreException
                    ("Destination byte[] must have room for at least 16 bytes, " +
                            "but has a length of only " + dst.length + ".");

        dst[offset] = bytes[3];
        dst[1 + offset] = bytes[2];
        dst[2 + offset] = bytes[1];
        dst[3 + offset] = bytes[0];

        dst[4 + offset] = bytes[5];
        dst[5 + offset] = bytes[4];

        dst[6 + offset] = bytes[7];
        dst[7 + offset] = bytes[6];

        System.arraycopy(bytes, 8, dst, 8 + offset, 8);
    }

    public boolean equals(final Object o) {
        if (o == null || !(o instanceof ClassID))
            return false;
        final ClassID cid = (ClassID) o;
        if (bytes.length != cid.bytes.length)
            return false;
        for (int i = 0; i < bytes.length; i++)
            if (bytes[i] != cid.bytes[i])
                return false;
        return true;
    }

    public int hashCode() {
        return new String(bytes).hashCode();
    }

    public String toString() {
        StringBuffer sbClassId = new StringBuffer(38);
        sbClassId.append('{');
        for (int i = 0; i < 16; i++) {
            sbClassId.append(HexDump.toHex(bytes[i]));
            if (i == 3 || i == 5 || i == 7 || i == 9)
                sbClassId.append('-');
        }
        sbClassId.append('}');
        return sbClassId.toString();
    }
}
