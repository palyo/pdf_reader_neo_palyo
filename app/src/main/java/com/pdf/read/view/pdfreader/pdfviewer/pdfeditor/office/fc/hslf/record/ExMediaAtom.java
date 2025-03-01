package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class ExMediaAtom extends RecordAtom {

    public static final int fLoop = 1;
    public static final int fRewind = 2;
    public static final int fNarration = 4;

    private byte[] _header;

    private byte[] _recdata;

    ExMediaAtom() {
        _recdata = new byte[8];

        _header = new byte[8];
        LittleEndian.putShort(_header, 2, (short) getRecordType());
        LittleEndian.putInt(_header, 4, _recdata.length);
    }

    private ExMediaAtom(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _recdata = new byte[len - 8];
        System.arraycopy(source, start + 8, _recdata, 0, len - 8);
    }

    public long getRecordType() {
        return RecordTypes.ExMediaAtom.typeID;
    }

    public void writeOut(OutputStream out) throws IOException {
        out.write(_header);
        out.write(_recdata);
    }

    public int getObjectId() {
        return LittleEndian.getInt(_recdata, 0);
    }

    public void setObjectId(int id) {
        LittleEndian.putInt(_recdata, 0, id);
    }

    public int getMask() {
        return LittleEndian.getInt(_recdata, 4);
    }

    public void setMask(int mask) {
        LittleEndian.putInt(_recdata, 4, mask);
    }

    public boolean getFlag(int bit) {
        return (getMask() & bit) != 0;
    }

    public void setFlag(int bit, boolean value) {
        int mask = getMask();
        if (value) mask |= bit;
        else mask &= ~bit;
        setMask(mask);
    }

    public String toString() {
        String buf = "ExMediaAtom\n" +
                "\tObjectId: " + getObjectId() + "\n" +
                "\tMask    : " + getMask() + "\n" +
                "\t  fLoop        : " + getFlag(fLoop) + "\n" +
                "\t  fRewind   : " + getFlag(fRewind) + "\n" +
                "\t  fNarration    : " + getFlag(fNarration) + "\n";
        return buf;
    }

    public void dispose() {
        _header = null;
        _recdata = null;
    }
}
