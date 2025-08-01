package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FilePassRecord extends StandardRecord {
    public final static short sid = 0x002F;
    private static final int ENCRYPTION_XOR = 0;
    private static final int ENCRYPTION_OTHER = 1;
    private static final int ENCRYPTION_OTHER_RC4 = 1;
    private static final int ENCRYPTION_OTHER_CAPI_2 = 2;
    private static final int ENCRYPTION_OTHER_CAPI_3 = 3;
    private final int _encryptionType;
    private final int _encryptionInfo;
    private final int _minorVersionNo;
    private byte[] _docId;
    private byte[] _saltData;
    private byte[] _saltHash;

    public FilePassRecord(RecordInputStream in) {
        _encryptionType = in.readUShort();

        switch (_encryptionType) {
            case ENCRYPTION_XOR:
                throw new RecordFormatException("HSSF does not currently support XOR obfuscation");
            case ENCRYPTION_OTHER:

                break;
            default:
                throw new RecordFormatException("Unknown encryption type " + _encryptionType);
        }
        _encryptionInfo = in.readUShort();
        switch (_encryptionInfo) {
            case ENCRYPTION_OTHER_RC4:

                break;
            case ENCRYPTION_OTHER_CAPI_2:
            case ENCRYPTION_OTHER_CAPI_3:
                throw new RecordFormatException(
                        "HSSF does not currently support CryptoAPI encryption");
            default:
                throw new RecordFormatException("Unknown encryption info " + _encryptionInfo);
        }
        _minorVersionNo = in.readUShort();
        if (_minorVersionNo != 1) {
            throw new RecordFormatException("Unexpected VersionInfo number for RC4Header " + _minorVersionNo);
        }
        _docId = read(in, 16);
        _saltData = read(in, 16);
        _saltHash = read(in, 16);
    }

    private static byte[] read(RecordInputStream in, int size) {
        byte[] result = new byte[size];
        in.readFully(result);
        return result;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(_encryptionType);
        out.writeShort(_encryptionInfo);
        out.writeShort(_minorVersionNo);
        out.write(_docId);
        out.write(_saltData);
        out.write(_saltHash);
    }

    protected int getDataSize() {
        return 54;
    }

    public byte[] getDocId() {
        return _docId.clone();
    }

    public void setDocId(byte[] docId) {
        _docId = docId.clone();
    }

    public byte[] getSaltData() {
        return _saltData.clone();
    }

    public void setSaltData(byte[] saltData) {
        _saltData = saltData.clone();
    }

    public byte[] getSaltHash() {
        return _saltHash.clone();
    }

    public void setSaltHash(byte[] saltHash) {
        _saltHash = saltHash.clone();
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {

        return this;
    }

    public String toString() {

        String buffer = "[FILEPASS]\n" +
                "    .type = " + String.valueOf(HexDump.shortToHex(_encryptionType)) + "\n" +
                "    .info = " + String.valueOf(HexDump.shortToHex(_encryptionInfo)) + "\n" +
                "    .ver  = " + String.valueOf(HexDump.shortToHex(_minorVersionNo)) + "\n" +
                "    .docId= " + HexDump.toHex(_docId) + "\n" +
                "    .salt = " + HexDump.toHex(_saltData) + "\n" +
                "    .hash = " + HexDump.toHex(_saltHash) + "\n" +
                "[/FILEPASS]\n";
        return buffer;
    }
}
