package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;

public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    private static final char[] _hexcodes = "0123456789ABCDEF".toCharArray();
    private static final int[] _shifts =
            {
                    60, 56, 52, 48, 44, 40, 36, 32, 28, 24, 20, 16, 12, 8, 4, 0
            };

    private HexDump() {

    }

    public static void dump(final byte[] data, final long offset,
                            final OutputStream stream, final int index, final int length)
            throws IOException, ArrayIndexOutOfBoundsException,
            IllegalArgumentException {
        if (data.length == 0) {
            stream.write(("No Data" + System.getProperty("line.separator")).getBytes());
            stream.flush();
            return;
        }
        if ((index < 0) || (index >= data.length)) {
            throw new ArrayIndexOutOfBoundsException(
                    "illegal index: " + index + " into array of length "
                            + data.length);
        }
        if (stream == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
        }

        long display_offset = offset + index;
        StringBuffer buffer = new StringBuffer(74);

        int data_length = Math.min(data.length, index + length);
        for (int j = index; j < data_length; j += 16) {
            int chars_read = data_length - j;

            if (chars_read > 16) {
                chars_read = 16;
            }
            buffer.append(
                    dump(display_offset)
            ).append(' ');
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    buffer.append(dump(data[k + j]));
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (int k = 0; k < chars_read; k++) {
                if ((data[k + j] >= ' ') && (data[k + j] < 127)) {
                    buffer.append((char) data[k + j]);
                } else {
                    buffer.append('.');
                }
            }
            buffer.append(EOL);
            stream.write(buffer.toString().getBytes());
            stream.flush();
            buffer.setLength(0);
            display_offset += chars_read;
        }

    }

    public synchronized static void dump(final byte[] data, final long offset,
                                         final OutputStream stream, final int index)
            throws IOException, ArrayIndexOutOfBoundsException,
            IllegalArgumentException {
        dump(data, offset, stream, index, data.length - index);
    }

    public static String dump(final byte[] data, final long offset,
                              final int index) {
        StringBuffer buffer;
        if ((index < 0) || (index >= data.length)) {
            throw new ArrayIndexOutOfBoundsException(
                    "illegal index: " + index + " into array of length "
                            + data.length);
        }
        long display_offset = offset + index;
        buffer = new StringBuffer(74);

        for (int j = index; j < data.length; j += 16) {
            int chars_read = data.length - j;

            if (chars_read > 16) {
                chars_read = 16;
            }
            buffer.append(dump(display_offset)).append(' ');
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    buffer.append(dump(data[k + j]));
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (int k = 0; k < chars_read; k++) {
                if ((data[k + j] >= ' ') && (data[k + j] < 127)) {
                    buffer.append((char) data[k + j]);
                } else {
                    buffer.append('.');
                }
            }
            buffer.append(EOL);
            display_offset += chars_read;
        }
        return buffer.toString();
    }

    private static String dump(final long value) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(0);
        for (int j = 0; j < 8; j++) {
            buf.append(_hexcodes[((int) (value >> _shifts[j + _shifts.length - 8])) & 15]);
        }
        return buf.toString();
    }

    private static String dump(final byte value) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(0);
        for (int j = 0; j < 2; j++) {
            buf.append(_hexcodes[(value >> _shifts[j + 6]) & 15]);
        }
        return buf.toString();
    }

    public static String toHex(final byte[] value) {
        StringBuffer retVal = new StringBuffer();
        retVal.append('[');
        for (int x = 0; x < value.length; x++) {
            if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(final short[] value) {
        StringBuffer retVal = new StringBuffer();
        retVal.append('[');
        for (int x = 0; x < value.length; x++) {
            retVal.append(toHex(value[x]));
            retVal.append(", ");
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(final byte[] value, final int bytesPerLine) {
        final int digits =
                (int) Math.round(Math.log(value.length) / Math.log(10) + 0.5);
        final StringBuffer formatString = new StringBuffer();
        for (int i = 0; i < digits; i++)
            formatString.append('0');
        formatString.append(": ");
        final DecimalFormat format = new DecimalFormat(formatString.toString());
        StringBuffer retVal = new StringBuffer();
        retVal.append(format.format(0));
        int i = -1;
        for (int x = 0; x < value.length; x++) {
            if (++i == bytesPerLine) {
                retVal.append('\n');
                retVal.append(format.format(x));
                i = 0;
            }
            retVal.append(toHex(value[x]));
            retVal.append(", ");
        }
        return retVal.toString();
    }

    public static String toHex(final short value) {
        return toHex(value, 4);
    }

    public static String toHex(final byte value) {
        return toHex(value, 2);
    }

    public static String toHex(final int value) {
        return toHex(value, 8);
    }

    public static String toHex(final long value) {
        return toHex(value, 16);
    }

    private static String toHex(final long value, final int digits) {
        StringBuffer result = new StringBuffer(digits);
        for (int j = 0; j < digits; j++) {
            result.append(_hexcodes[(int) ((value >> _shifts[j + (16 - digits)]) & 15)]);
        }
        return result.toString();
    }

    public static void dump(InputStream in, PrintStream out, int start, int bytesToDump) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        if (bytesToDump == -1) {
            int c = in.read();
            while (c != -1) {
                buf.write(c);
                c = in.read();
            }
        } else {
            int bytesRemaining = bytesToDump;
            while (bytesRemaining-- > 0) {
                int c = in.read();
                if (c == -1) {
                    break;
                }
                buf.write(c);
            }
        }

        byte[] data = buf.toByteArray();
        dump(data, 0, out, start, data.length);
    }

    private static char[] toHexChars(long pValue, int nBytes) {
        int charPos = 2 + nBytes * 2;

        char[] result = new char[charPos];

        long value = pValue;
        do {
            result[--charPos] = _hexcodes[(int) (value & 0x0F)];
            value >>>= 4;
        } while (charPos > 1);

        result[0] = '0';
        result[1] = 'x';
        return result;
    }

    public static char[] longToHex(long value) {
        return toHexChars(value, 8);
    }

    public static char[] intToHex(int value) {
        return toHexChars(value, 4);
    }

    public static char[] shortToHex(int value) {
        return toHexChars(value, 2);
    }

    public static char[] byteToHex(int value) {
        return toHexChars(value, 1);
    }

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] b = new byte[(int) file.length()];
        in.read(b);
        System.out.println(HexDump.dump(b, 0, 0));
        in.close();
    }
}
