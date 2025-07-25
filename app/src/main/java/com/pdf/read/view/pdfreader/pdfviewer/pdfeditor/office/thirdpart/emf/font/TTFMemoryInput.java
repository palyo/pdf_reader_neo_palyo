package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.font;

public class TTFMemoryInput extends TTFInput {

    private final byte[] data;

    private int pointer;

    public TTFMemoryInput(byte[] data) {
        this.data = data;
    }

    public void seek(long offset) {
        pointer = (int) offset;
    }

    long getPointer() {
        return pointer;
    }

    public byte readChar() {
        return data[pointer++];
    }

    public int readRawByte() {
        return data[pointer++] & 0x00ff;
    }

    public int readByte() {
        return data[pointer++] & 0x00ff;
    }

    public short readShort() {
        int result = data[pointer++];
        return (short) ((result << 8) | data[pointer++]);
    }

    public int readUShort() {
        return (data[pointer++] << 8) | data[pointer++];
    }

    public int readLong() {
        int result = data[pointer++];
        return (short) ((result << 24) | data[pointer++] << 16
                | data[pointer++] << 8 | data[pointer++]);
    }

    public long readULong() {
        byte[] temp = new byte[4];
        readFully(temp);
        long l = 0;
        for (int i = 0; i < temp.length; i++) {
            l |= (long) (temp[3 - i] & 255) << (8 * i);
        }
        return l;
    }

    public void readFully(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            b[i] = data[pointer++];
        }
    }
}
