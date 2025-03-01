package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PaletteRecord extends StandardRecord {
    public final static short sid = 0x0092;
    public final static byte STANDARD_PALETTE_SIZE = (byte) 56;
    public final static short FIRST_COLOR_INDEX = (short) 0x8;

    private final List<PColor> _colors;

    public PaletteRecord() {
        PColor[] defaultPalette = createDefaultPalette();
        _colors = new ArrayList<PColor>(defaultPalette.length);
        Collections.addAll(_colors, defaultPalette);
    }

    public PaletteRecord(RecordInputStream in) {
        int field_1_numcolors = in.readShort();
        _colors = new ArrayList<PColor>(field_1_numcolors);
        for (int k = 0; k < field_1_numcolors; k++) {
            _colors.add(new PColor(in));
        }
    }

    private static PColor[] createDefaultPalette() {
        return new PColor[]{
                pc(0, 0, 0),
                pc(255, 255, 255),
                pc(255, 0, 0),
                pc(0, 255, 0),
                pc(0, 0, 255),
                pc(255, 255, 0),
                pc(255, 0, 255),
                pc(0, 255, 255),
                pc(128, 0, 0),
                pc(0, 128, 0),
                pc(0, 0, 128),
                pc(128, 128, 0),
                pc(128, 0, 128),
                pc(0, 128, 128),
                pc(192, 192, 192),
                pc(128, 128, 128),
                pc(153, 153, 255),
                pc(153, 51, 102),
                pc(255, 255, 204),
                pc(204, 255, 255),
                pc(102, 0, 102),
                pc(255, 128, 128),
                pc(0, 102, 204),
                pc(204, 204, 255),
                pc(0, 0, 128),
                pc(255, 0, 255),
                pc(255, 255, 0),
                pc(0, 255, 255),
                pc(128, 0, 128),
                pc(128, 0, 0),
                pc(0, 128, 128),
                pc(0, 0, 255),
                pc(0, 204, 255),
                pc(204, 255, 255),
                pc(204, 255, 204),
                pc(255, 255, 153),
                pc(153, 204, 255),
                pc(255, 153, 204),
                pc(204, 153, 255),
                pc(255, 204, 153),
                pc(51, 102, 255),
                pc(51, 204, 204),
                pc(153, 204, 0),
                pc(255, 204, 0),
                pc(255, 153, 0),
                pc(255, 102, 0),
                pc(102, 102, 153),
                pc(150, 150, 150),
                pc(0, 51, 102),
                pc(51, 153, 102),
                pc(0, 51, 0),
                pc(51, 51, 0),
                pc(153, 51, 0),
                pc(153, 51, 102),
                pc(51, 51, 153),
                pc(51, 51, 51),
        };
    }

    private static PColor pc(int r, int g, int b) {
        return new PColor(r, g, b);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[PALETTE]\n");
        buffer.append("  numcolors     = ").append(_colors.size()).append('\n');
        for (int i = 0; i < _colors.size(); i++) {
            PColor c = _colors.get(i);
            buffer.append("* colornum      = ").append(i).append('\n');
            buffer.append(c.toString());
            buffer.append("/*colornum      = ").append(i).append('\n');
        }
        buffer.append("[/PALETTE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_colors.size());
        for (int i = 0; i < _colors.size(); i++) {
            _colors.get(i).serialize(out);
        }
    }

    protected int getDataSize() {
        return 2 + _colors.size() * PColor.ENCODED_SIZE;
    }

    public short getSid() {
        return sid;
    }

    public byte[] getColor(int byteIndex) {
        int i = byteIndex - FIRST_COLOR_INDEX;
        if (i < 0 || i >= _colors.size()) {
            return null;
        }
        return _colors.get(i).getTriplet();
    }

    public void setColor(short byteIndex, byte red, byte green, byte blue) {
        int i = byteIndex - FIRST_COLOR_INDEX;
        if (i < 0 || i >= STANDARD_PALETTE_SIZE) {
            return;
        }

        while (_colors.size() <= i) {
            _colors.add(new PColor(0, 0, 0));
        }
        PColor custColor = new PColor(red, green, blue);
        _colors.set(i, custColor);
    }

    private static final class PColor {
        public static final short ENCODED_SIZE = 4;
        private final int _red;
        private final int _green;
        private final int _blue;

        public PColor(int red, int green, int blue) {
            _red = red;
            _green = green;
            _blue = blue;
        }

        public PColor(RecordInputStream in) {
            _red = in.readByte();
            _green = in.readByte();
            _blue = in.readByte();
            in.readByte();
        }

        public byte[] getTriplet() {
            return new byte[]{(byte) _red, (byte) _green, (byte) _blue};
        }

        public void serialize(LittleEndianOutput out) {
            out.writeByte(_red);
            out.writeByte(_green);
            out.writeByte(_blue);
            out.writeByte(0);
        }

        public String toString() {
            String buffer = "  red   = " + (_red & 0xff) + '\n' +
                    "  green = " + (_green & 0xff) + '\n' +
                    "  blue  = " + (_blue & 0xff) + '\n';
            return buffer;
        }
    }
}
