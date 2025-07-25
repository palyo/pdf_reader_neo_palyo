package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class TypeWriter {
    public static int writeToStream(final OutputStream out, final short n)
            throws IOException {
        final int length = LittleEndian.SHORT_SIZE;
        byte[] buffer = new byte[length];
        LittleEndian.putShort(buffer, 0, n);
        out.write(buffer, 0, length);
        return length;
    }

    public static int writeToStream(final OutputStream out, final int n)
            throws IOException {
        final int l = LittleEndian.INT_SIZE;
        final byte[] buffer = new byte[l];
        LittleEndian.putInt(buffer, 0, n);
        out.write(buffer, 0, l);
        return l;

    }

    public static int writeToStream(final OutputStream out, final long n)
            throws IOException {
        final int l = LittleEndian.LONG_SIZE;
        final byte[] buffer = new byte[l];
        LittleEndian.putLong(buffer, 0, n);
        out.write(buffer, 0, l);
        return l;

    }

    public static void writeUShortToStream(final OutputStream out, final int n)
            throws IOException {
        int high = n & 0xFFFF0000;
        if (high != 0)
            throw new IllegalPropertySetDataException
                    ("Value " + n + " cannot be represented by 2 bytes.");
        writeToStream(out, (short) n);
    }

    public static int writeUIntToStream(final OutputStream out, final long n)
            throws IOException {
        long high = n & 0xFFFFFFFF00000000L;
        if (high != 0 && high != 0xFFFFFFFF00000000L)
            throw new IllegalPropertySetDataException
                    ("Value " + n + " cannot be represented by 4 bytes.");
        return writeToStream(out, (int) n);
    }

    public static int writeToStream(final OutputStream out, final ClassID n)
            throws IOException {
        byte[] b = new byte[16];
        n.write(b, 0);
        out.write(b, 0, b.length);
        return b.length;
    }

    public static void writeToStream(final OutputStream out,
                                     final Property[] properties,
                                     final int codepage)
            throws IOException, UnsupportedVariantTypeException {

        if (properties == null)
            return;

        for (int i = 0; i < properties.length; i++) {
            final Property p = properties[i];
            writeUIntToStream(out, p.getID());
            writeUIntToStream(out, p.getSize());
        }

        for (int i = 0; i < properties.length; i++) {
            final Property p = properties[i];
            long type = p.getType();
            writeUIntToStream(out, type);
            VariantSupport.write(out, (int) type, p.getValue(), codepage);
        }
    }

    public static int writeToStream(final OutputStream out, final double n)
            throws IOException {
        final int l = LittleEndian.DOUBLE_SIZE;
        final byte[] buffer = new byte[l];
        LittleEndian.putDouble(buffer, 0, n);
        out.write(buffer, 0, l);
        return l;
    }
}
