package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.nio.charset.StandardCharsets;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class FontEntityAtom extends RecordAtom {
    private byte[] _header;

    private byte[] _recdata;

    private FontEntityAtom(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _recdata = new byte[len - 8];
        System.arraycopy(source, start + 8, _recdata, 0, len - 8);
    }

    public FontEntityAtom() {
        _recdata = new byte[68];

        _header = new byte[8];
        LittleEndian.putShort(_header, 2, (short) getRecordType());
        LittleEndian.putInt(_header, 4, _recdata.length);
    }

    public long getRecordType() {
        return RecordTypes.FontEntityAtom.typeID;
    }

    public String getFontName() {
        String name = null;
        int i = 0;
        while (i < 64) {

            if (_recdata[i] == 0 && _recdata[i + 1] == 0) {
                name = new String(_recdata, 0, i, StandardCharsets.UTF_16LE);
                break;
            }
            i += 2;
        }
        return name;
    }

    public void setFontName(String name) {

        if (!name.endsWith("\000")) {
            name = name + "\000";
        }

        if (name.length() > 32) {
            throw new RuntimeException(
                    "The length of the font name, including null termination, must not exceed 32 characters");
        }

        byte[] bytes = name.getBytes(StandardCharsets.UTF_16LE);
        System.arraycopy(bytes, 0, _recdata, 0, bytes.length);
    }

    public int getFontIndex() {
        return LittleEndian.getShort(_header, 0) >> 4;
    }

    public void setFontIndex(int idx) {
        LittleEndian.putShort(_header, 0, (short) idx);
    }

    public int getCharSet() {
        return _recdata[64];
    }

    public void setCharSet(int charset) {
        _recdata[64] = (byte) charset;
    }

    public int getFontFlags() {
        return _recdata[65];
    }

    public void setFontFlags(int flags) {
        _recdata[65] = (byte) flags;
    }

    public int getFontType() {
        return _recdata[66];
    }

    public void setFontType(int type) {
        _recdata[66] = (byte) type;
    }

    public int getPitchAndFamily() {
        return _recdata[67];
    }

    public void setPitchAndFamily(int val) {
        _recdata[67] = (byte) val;
    }

    public void dispose() {
        _header = null;
        _recdata = null;
    }
}
