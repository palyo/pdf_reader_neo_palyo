package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public class Tbkd {

    private final short itxbxs;

    private final short dcpDepend;

    private final boolean fMarkDelete;

    private final boolean fUnk;

    private final boolean fTextOverflow;

    public Tbkd(byte[] data, int offset) {
        itxbxs = LittleEndian.getShort(data, offset);
        dcpDepend = LittleEndian.getShort(data, offset + 2);

        int t = LittleEndian.getShort(data, offset + 4);
        fMarkDelete = ((t & 0x20) != 0);
        fUnk = ((t & 0x10) != 0);
        fTextOverflow = ((t & 0x8) != 0);
    }

    public static int getSize() {
        return 6;
    }

    public int getTxbxIndex() {
        return itxbxs;
    }
}
