package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class TextBytesAtom extends RecordAtom {
    private static final long _type = 4008L;
    private byte[] _header;
    private byte[] _text;

    private TextBytesAtom(byte[] source, int start, int len) {

        if (len < 8) {
            len = 8;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _text = new byte[len - 8];
        System.arraycopy(source, start + 8, _text, 0, len - 8);
    }

    public TextBytesAtom() {
        _header = new byte[8];
        LittleEndian.putUShort(_header, 0, 0);
        LittleEndian.putUShort(_header, 2, (int) _type);
        LittleEndian.putInt(_header, 4, 0);

        _text = new byte[]{};
    }

    public String getText() {
        return StringUtil.getFromCompressedUnicode(_text, 0, _text.length);
    }

    public void setText(byte[] b) {

        _text = b;

        LittleEndian.putInt(_header, 4, _text.length);
    }

    public long getRecordType() {
        return _type;
    }

    public String toString() {
        String out = "TextBytesAtom:\n" +
                HexDump.dump(_text, 0, 0);
        return out;
    }

    public void dispose() {
        _header = null;
        _text = null;
    }
}
