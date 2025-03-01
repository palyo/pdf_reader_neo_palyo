package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.io.IOException;
import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public final class ParagraphHeight {
    private final BitField fSpare = BitFieldFactory.getInstance(0x0001);
    private final BitField fUnk = BitFieldFactory.getInstance(0x0002);
    private final BitField fDiffLines = BitFieldFactory.getInstance(0x0004);
    private final BitField clMac = BitFieldFactory.getInstance(0xff00);
    private short infoField;
    private short reserved;
    private int dxaCol;
    private int dymLineOrHeight;

    public ParagraphHeight(byte[] buf, int offset) {
        infoField = LittleEndian.getShort(buf, offset);
        offset += LittleEndian.SHORT_SIZE;
        reserved = LittleEndian.getShort(buf, offset);
        offset += LittleEndian.SHORT_SIZE;
        dxaCol = LittleEndian.getInt(buf, offset);
        offset += LittleEndian.INT_SIZE;
        dymLineOrHeight = LittleEndian.getInt(buf, offset);
    }

    public ParagraphHeight() {

    }

    public void write(OutputStream out) throws IOException {
        out.write(toByteArray());
    }

    byte[] toByteArray() {
        byte[] buf = new byte[12];
        int offset = 0;
        LittleEndian.putShort(buf, offset, infoField);
        offset += LittleEndian.SHORT_SIZE;
        LittleEndian.putShort(buf, offset, reserved);
        offset += LittleEndian.SHORT_SIZE;
        LittleEndian.putInt(buf, offset, dxaCol);
        offset += LittleEndian.INT_SIZE;
        LittleEndian.putInt(buf, offset, dymLineOrHeight);

        return buf;
    }

    public boolean equals(Object o) {
        ParagraphHeight ph = (ParagraphHeight) o;

        return infoField == ph.infoField && reserved == ph.reserved && dxaCol == ph.dxaCol
                && dymLineOrHeight == ph.dymLineOrHeight;
    }
}
