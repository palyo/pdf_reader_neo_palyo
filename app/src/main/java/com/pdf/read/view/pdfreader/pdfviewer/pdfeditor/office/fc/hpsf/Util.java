package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;

public class Util {

    public static final long EPOCH_DIFF = 11644473600000L;

    public static boolean equal(final byte[] a, final byte[] b) {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    public static void copy(final byte[] src, final int srcOffset,
                            final int length, final byte[] dst,
                            final int dstOffset) {
        if (length >= 0) System.arraycopy(src, srcOffset, dst, dstOffset, length);
    }

    public static byte[] cat(final byte[][] byteArrays) {
        int capacity = 0;
        for (int i = 0; i < byteArrays.length; i++)
            capacity += byteArrays[i].length;
        final byte[] result = new byte[capacity];
        int r = 0;
        for (int i = 0; i < byteArrays.length; i++)
            for (int j = 0; j < byteArrays[i].length; j++)
                result[r++] = byteArrays[i][j];
        return result;
    }

    public static byte[] copy(final byte[] src, final int offset,
                              final int length) {
        final byte[] result = new byte[length];
        copy(src, offset, length, result, 0);
        return result;
    }

    public static Date filetimeToDate(final int high, final int low) {
        final long filetime = ((long) high) << 32 | (low & 0xffffffffL);
        return filetimeToDate(filetime);
    }

    public static Date filetimeToDate(final long filetime) {
        final long ms_since_16010101 = filetime / (1000 * 10);
        final long ms_since_19700101 = ms_since_16010101 - EPOCH_DIFF;
        return new Date(ms_since_19700101);
    }

    public static long dateToFileTime(final Date date) {
        long ms_since_19700101 = date.getTime();
        long ms_since_16010101 = ms_since_19700101 + EPOCH_DIFF;
        return ms_since_16010101 * (1000 * 10);
    }

    public static boolean equals(Collection<?> c1, Collection<?> c2) {
        Object[] o1 = c1.toArray();
        Object[] o2 = c2.toArray();
        return internalEquals(o1, o2);
    }

    public static boolean equals(Object[] c1, Object[] c2) {
        final Object[] o1 = c1.clone();
        final Object[] o2 = c2.clone();
        return internalEquals(o1, o2);
    }

    private static boolean internalEquals(Object[] o1, Object[] o2) {
        for (int i1 = 0; i1 < o1.length; i1++) {
            final Object obj1 = o1[i1];
            boolean matchFound = false;
            for (int i2 = 0; !matchFound && i2 < o1.length; i2++) {
                final Object obj2 = o2[i2];
                if (obj1.equals(obj2)) {
                    matchFound = true;
                    o2[i2] = null;
                }
            }
            if (!matchFound)
                return false;
        }
        return true;
    }

    public static byte[] pad4(final byte[] ba) {
        final int PAD = 4;
        final byte[] result;
        int l = ba.length % PAD;
        if (l == 0)
            result = ba;
        else {
            l = PAD - l;
            result = new byte[ba.length + l];
            System.arraycopy(ba, 0, result, 0, ba.length);
        }
        return result;
    }

    public static char[] pad4(final char[] ca) {
        final int PAD = 4;
        final char[] result;
        int l = ca.length % PAD;
        if (l == 0)
            result = ca;
        else {
            l = PAD - l;
            result = new char[ca.length + l];
            System.arraycopy(ca, 0, result, 0, ca.length);
        }
        return result;
    }

    public static char[] pad4(final String s) {
        return pad4(s.toCharArray());
    }

    public static String toString(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        try {
            sw.close();
            return sw.toString();
        } catch (IOException e) {
            String b = t.getMessage() + "\n" +
                    "Could not create a stacktrace. Reason: " +
                    e.getMessage();
            return b;
        }
    }
}
