package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.font;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public abstract class TTFInput {

    private final Stack filePosStack = new Stack();

    private int tempFlags;

    public static final boolean checkZeroBit(int b, int bit, String name)
            throws IOException {
        if (flagBit(b, bit)) {
            System.err.println("Reserved bit " + bit + " in " + name
                    + " not 0.");
            return false;
        } else {
            return true;
        }
    }

    public static boolean flagBit(int b, int bit) {
        return (b & (1 << bit)) > 0;
    }

    public abstract void seek(long offset) throws IOException;

    abstract long getPointer() throws IOException;

    public void pushPos() throws IOException {
        filePosStack.push(Long.valueOf(getPointer()));
    }

    public void popPos() throws IOException {
        seek(((Long) filePosStack.pop()).longValue());
    }

    public abstract int readRawByte() throws IOException;

    public abstract int readByte() throws IOException;

    public abstract short readShort() throws IOException;

    public abstract int readUShort() throws IOException;

    public abstract long readULong() throws IOException;

    public abstract int readLong() throws IOException;

    public abstract byte readChar() throws IOException;

    public final short readFWord() throws IOException {
        return readShort();
    }

    public final int readUFWord() throws IOException {
        return readUShort();
    }

    public final double readFixed() throws IOException {
        int major = readShort();
        int minor = readShort();
        return (double) major + (double) minor / 16384d;
    }

    public final double readF2Dot14() throws IOException {
        int major = readByte();
        int minor = readByte();
        int fraction = minor + ((major & 0x3f) << 8);
        int mantissa = major >> 6;
        if (mantissa >= 2)
            mantissa -= 4;
        return (double) mantissa + (double) fraction / 16384d;
    }

    public final void checkShortZero() throws IOException {
        if (readShort() != 0) {
            System.err.println("Reserved bit should be 0.");
        }
    }

    public void readUShortFlags() throws IOException {
        tempFlags = readUShort();
    }

    public void readByteFlags() throws IOException {
        tempFlags = readByte();
    }

    public boolean flagBit(int bit) {
        return flagBit(tempFlags, bit);
    }

    public abstract void readFully(byte[] b) throws IOException;

    public int[] readFFFFTerminatedUShortArray() throws IOException {
        List values = new LinkedList();
        int ushort = -1;
        do {
            ushort = readUShort();
            values.add(Integer.valueOf(ushort));
        } while (ushort != 0xFFFF);
        int[] shorts = new int[values.size()];
        Iterator i = values.iterator();
        int j = 0;
        while (i.hasNext()) {
            shorts[j++] = ((Integer) i.next()).intValue();
        }
        return shorts;
    }

    public int[] readUShortArray(int n) throws IOException {
        int[] temp = new int[n];
        for (int i = 0; i < temp.length; i++)
            temp[i] = readUShort();
        return temp;
    }

    public short[] readShortArray(int n) throws IOException {
        short[] temp = new short[n];
        for (int i = 0; i < temp.length; i++)
            temp[i] = readShort();
        return temp;
    }

}
