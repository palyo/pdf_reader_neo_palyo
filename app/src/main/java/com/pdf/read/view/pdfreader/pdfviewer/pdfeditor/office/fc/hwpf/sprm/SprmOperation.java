package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public final class SprmOperation {
    public static final int TYPE_PAP = 1;
    public static final int TYPE_CHP = 2;
    public static final int TYPE_PIC = 3;
    public static final int TYPE_SEP = 4;
    public static final int TYPE_TAP = 5;
    @Deprecated
    final static public int PAP_TYPE = TYPE_PAP;
    @Deprecated
    final static public int TAP_TYPE = TYPE_TAP;
    private static final BitField BITFIELD_OP = BitFieldFactory.getInstance(0x1ff);
    private static final BitField BITFIELD_SIZECODE = BitFieldFactory.getInstance(0xe000);
    private static final BitField BITFIELD_SPECIAL = BitFieldFactory.getInstance(0x200);
    private static final BitField BITFIELD_TYPE = BitFieldFactory.getInstance(0x1c00);
    final static private short SPRM_LONG_PARAGRAPH = (short) 0xc615;
    final static private short SPRM_LONG_TABLE = (short) 0xd608;
    private final int _offset;
    private final byte[] _grpprl;
    private final int _size;
    private final short _value;
    private int _gOffset;

    public SprmOperation(byte[] grpprl, int offset) {
        _grpprl = grpprl;
        _value = LittleEndian.getShort(grpprl, offset);
        _offset = offset;
        _gOffset = offset + 2;
        _size = initSize(_value);
    }

    public static int getOperationFromOpcode(short opcode) {
        return BITFIELD_OP.getValue(opcode);
    }

    public static int getTypeFromOpcode(short opcode) {
        return BITFIELD_TYPE.getValue(opcode);
    }

    public byte[] toByteArray() {
        byte[] result = new byte[size()];
        System.arraycopy(_grpprl, _offset, result, 0, size());
        return result;
    }

    public byte[] getGrpprl() {
        return _grpprl;
    }

    public int getGrpprlOffset() {
        return _gOffset;
    }

    public int getOperand() {
        switch (getSizeCode()) {
            case 0:
            case 1:
                return _grpprl[_gOffset];
            case 2:
            case 4:
            case 5:
                return LittleEndian.getShort(_grpprl, _gOffset);
            case 3:
                return LittleEndian.getInt(_grpprl, _gOffset);
            case 6:

                byte operandLength = _grpprl[_gOffset + 1];

                byte[] codeBytes = new byte[LittleEndian.INT_SIZE];
                for (int i = 0; i < operandLength; i++)
                    if (_gOffset + i < _grpprl.length)
                        codeBytes[i] = _grpprl[_gOffset + 1 + i];

                return LittleEndian.getInt(codeBytes, 0);
            case 7:
                byte[] threeByteInt = new byte[4];
                threeByteInt[0] = _grpprl[_gOffset];
                threeByteInt[1] = _grpprl[_gOffset + 1];
                threeByteInt[2] = _grpprl[_gOffset + 2];
                threeByteInt[3] = (byte) 0;
                return LittleEndian.getInt(threeByteInt, 0);
            default:
                throw new IllegalArgumentException("SPRM contains an invalid size code");
        }
    }

    public int getOperation() {
        return BITFIELD_OP.getValue(_value);
    }

    public int getSizeCode() {
        return BITFIELD_SIZECODE.getValue(_value);
    }

    public int getType() {
        return BITFIELD_TYPE.getValue(_value);
    }

    private int initSize(short sprm) {
        switch (getSizeCode()) {
            case 0:
            case 1:
                return 3;
            case 2:
            case 4:
            case 5:
                return 4;
            case 3:
                return 6;
            case 6:
                int offset = _gOffset;
                if (sprm == SPRM_LONG_TABLE || sprm == SPRM_LONG_PARAGRAPH) {
                    int retVal = (0x0000ffff & LittleEndian.getShort(_grpprl, offset)) + 3;
                    _gOffset += 2;
                    return retVal;
                }
                return (0x000000ff & _grpprl[_gOffset++]) + 3;
            case 7:
                return 5;
            default:
                throw new IllegalArgumentException("SPRM contains an invalid size code");
        }
    }

    public int size() {
        return _size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[SPRM] (0x");
        stringBuilder.append(Integer.toHexString(_value & 0xffff));
        stringBuilder.append("): ");
        try {
            stringBuilder.append(getOperand());
        } catch (Exception exc) {
            stringBuilder.append("(error)");
        }
        return stringBuilder.toString();
    }
}
