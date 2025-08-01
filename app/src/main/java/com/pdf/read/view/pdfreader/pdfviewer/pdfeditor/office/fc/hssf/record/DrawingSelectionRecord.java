package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianInput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class DrawingSelectionRecord extends StandardRecord {
    public static final short sid = 0x00ED;
    private final OfficeArtRecordHeader _header;
    private final int _cpsp;
    private final int _dgslk;
    private final int _spidFocus;
    private final int[] _shapeIds;

    public DrawingSelectionRecord(RecordInputStream in) {
        _header = new OfficeArtRecordHeader(in);
        _cpsp = in.readInt();
        _dgslk = in.readInt();
        _spidFocus = in.readInt();
        int nShapes = in.available() / 4;
        int[] shapeIds = new int[nShapes];
        for (int i = 0; i < nShapes; i++) {
            shapeIds[i] = in.readInt();
        }
        _shapeIds = shapeIds;
    }

    public short getSid() {
        return sid;
    }

    protected int getDataSize() {
        return OfficeArtRecordHeader.ENCODED_SIZE
                + 12
                + _shapeIds.length * 4;
    }

    public void serialize(LittleEndianOutput out) {
        _header.serialize(out);
        out.writeInt(_cpsp);
        out.writeInt(_dgslk);
        out.writeInt(_spidFocus);
        for (int i = 0; i < _shapeIds.length; i++) {
            out.writeInt(_shapeIds[i]);
        }
    }

    public Object clone() {

        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("[MSODRAWINGSELECTION]\n");
        sb.append("    .rh       =(").append(_header.debugFormatAsString()).append(")\n");
        sb.append("    .cpsp     =").append(HexDump.intToHex(_cpsp)).append('\n');
        sb.append("    .dgslk    =").append(HexDump.intToHex(_dgslk)).append('\n');
        sb.append("    .spidFocus=").append(HexDump.intToHex(_spidFocus)).append('\n');
        sb.append("    .shapeIds =(");
        for (int i = 0; i < _shapeIds.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(HexDump.intToHex(_shapeIds[i]));
        }
        sb.append(")\n");

        sb.append("[/MSODRAWINGSELECTION]\n");
        return sb.toString();
    }

    private static final class OfficeArtRecordHeader {
        public static final int ENCODED_SIZE = 8;

        private final int _verAndInstance;

        private final int _type;
        private final int _length;

        public OfficeArtRecordHeader(LittleEndianInput in) {
            _verAndInstance = in.readUShort();
            _type = in.readUShort();
            _length = in.readInt();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(_verAndInstance);
            out.writeShort(_type);
            out.writeInt(_length);
        }

        public String debugFormatAsString() {
            String sb = "ver+inst=" + String.valueOf(HexDump.shortToHex(_verAndInstance)) +
                    " type=" + String.valueOf(HexDump.shortToHex(_type)) +
                    " len=" + String.valueOf(HexDump.intToHex(_length));
            return sb;
        }
    }
}
