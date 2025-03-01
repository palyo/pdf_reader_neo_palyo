package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.pivottable;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordFormatException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class ExtendedPivotTableViewFieldsRecord extends StandardRecord {
    public static final short sid = 0x0100;

    private static final int STRING_NOT_PRESENT_LEN = 0xFFFF;

    private final int _grbit1;
    private final int _grbit2;
    private final int _citmShow;
    private final int _isxdiSort;
    private final int _isxdiShow;
    private final int _reserved1;
    private final int _reserved2;
    private String _subtotalName;

    public ExtendedPivotTableViewFieldsRecord(RecordInputStream in) {

        _grbit1 = in.readInt();
        _grbit2 = in.readUByte();
        _citmShow = in.readUByte();
        _isxdiSort = in.readUShort();
        _isxdiShow = in.readUShort();

        switch (in.remaining()) {
            case 0:

                _reserved1 = 0;
                _reserved2 = 0;
                _subtotalName = null;
                return;
            case 10:

                break;
            default:
                throw new RecordFormatException("Unexpected remaining size (" + in.remaining() + ")");
        }
        int cchSubName = in.readUShort();
        _reserved1 = in.readInt();
        _reserved2 = in.readInt();
        if (cchSubName != STRING_NOT_PRESENT_LEN) {
            _subtotalName = in.readUnicodeLEString(cchSubName);
        }
    }

    @Override
    protected void serialize(LittleEndianOutput out) {

        out.writeInt(_grbit1);
        out.writeByte(_grbit2);
        out.writeByte(_citmShow);
        out.writeShort(_isxdiSort);
        out.writeShort(_isxdiShow);

        if (_subtotalName == null) {
            out.writeShort(STRING_NOT_PRESENT_LEN);
        } else {
            out.writeShort(_subtotalName.length());
        }

        out.writeInt(_reserved1);
        out.writeInt(_reserved2);
        if (_subtotalName != null) {
            StringUtil.putUnicodeLE(_subtotalName, out);
        }
    }

    @Override
    protected int getDataSize() {

        return 4 + 1 + 1 + 2 + 2 + 2 + 4 + 4 +
                (_subtotalName == null ? 0 : (2 * _subtotalName.length()));
    }

    @Override
    public short getSid() {
        return sid;
    }

    @Override
    public String toString() {

        String buffer = "[SXVDEX]\n" +
                "    .grbit1 =" + String.valueOf(HexDump.intToHex(_grbit1)) + "\n" +
                "    .grbit2 =" + String.valueOf(HexDump.byteToHex(_grbit2)) + "\n" +
                "    .citmShow =" + String.valueOf(HexDump.byteToHex(_citmShow)) + "\n" +
                "    .isxdiSort =" + String.valueOf(HexDump.shortToHex(_isxdiSort)) + "\n" +
                "    .isxdiShow =" + String.valueOf(HexDump.shortToHex(_isxdiShow)) + "\n" +
                "    .subtotalName =" + _subtotalName + "\n" +
                "[/SXVDEX]\n";
        return buffer;
    }
}
