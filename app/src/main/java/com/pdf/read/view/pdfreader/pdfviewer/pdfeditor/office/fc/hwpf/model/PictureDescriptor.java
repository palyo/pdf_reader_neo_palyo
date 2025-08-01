package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.util.Arrays;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public class PictureDescriptor {
    private static final int LCB_OFFSET = 0x00;

    private static final int CBHEADER_OFFSET = 0x04;

    private static final int MFP_MM_OFFSET = 0x06;

    private static final int MFP_XEXT_OFFSET = 0x08;
    private static final int MFP_YEXT_OFFSET = 0x0A;
    private static final int MFP_HMF_OFFSET = 0x0C;
    private static final int DXAGOAL_OFFSET = 0x1C;
    private static final int DYAGOAL_OFFSET = 0x1E;
    private static final int MX_OFFSET = 0x20;
    private static final int MY_OFFSET = 0x22;
    private static final int DXACROPLEFT_OFFSET = 0x24;
    private static final int DYACROPTOP_OFFSET = 0x26;
    private static final int DXACROPRIGHT_OFFSET = 0x28;
    private static final int DYACROPBOTTOM_OFFSET = 0x2A;

    protected int lcb;

    protected int cbHeader;
    protected int mfp_mm;
    protected int mfp_xExt;
    protected int mfp_yExt;

    protected int mfp_hMF;

    protected byte[] offset14 = new byte[14];

    protected short dxaGoal = 0;

    protected short dyaGoal = 0;

    protected short mx;

    protected short my;

    protected float dxaCropLeft = 0;

    protected float dyaCropTop = 0;

    protected float dxaCropRight = 0;

    protected float dyaCropBottom = 0;
    protected float fBright;
    protected float fContrast;
    protected boolean bGrayScl;
    protected float fThreshold;
    protected boolean bSetBright;
    protected boolean bSetContrast;
    protected boolean bSetGrayScl;
    protected boolean bSetThreshold;

    public PictureDescriptor() {
    }

    public PictureDescriptor(byte[] _dataStream, int startOffset) {
        this.lcb = LittleEndian.getInt(_dataStream, startOffset + LCB_OFFSET);
        this.cbHeader = LittleEndian.getUShort(_dataStream, startOffset + CBHEADER_OFFSET);

        this.mfp_mm = LittleEndian.getUShort(_dataStream, startOffset + MFP_MM_OFFSET);
        this.mfp_xExt = LittleEndian.getUShort(_dataStream, startOffset + MFP_XEXT_OFFSET);
        this.mfp_yExt = LittleEndian.getUShort(_dataStream, startOffset + MFP_YEXT_OFFSET);
        this.mfp_hMF = LittleEndian.getUShort(_dataStream, startOffset + MFP_HMF_OFFSET);

        this.offset14 = LittleEndian.getByteArray(_dataStream, startOffset + 0x0E, 14);

        this.dxaGoal = LittleEndian.getShort(_dataStream, startOffset + DXAGOAL_OFFSET);
        this.dyaGoal = LittleEndian.getShort(_dataStream, startOffset + DYAGOAL_OFFSET);

        this.mx = LittleEndian.getShort(_dataStream, startOffset + MX_OFFSET);
        this.my = LittleEndian.getShort(_dataStream, startOffset + MY_OFFSET);

        int pictureOffset = startOffset + 68;
        if (this.mfp_mm == 0x66) {
            int cchPicName = _dataStream[pictureOffset] & 0xFFFF;
            pictureOffset += 1 + cchPicName;
        }

        OfficeArtRecordHeader officeArtRecHeader = readHeader(_dataStream, pictureOffset);
        short recVer = getRecVer(officeArtRecHeader);
        short recInstance = getRecInstance(officeArtRecHeader);
        if (recVer == 0xF && recInstance == 0x0000 && officeArtRecHeader.recType == 0xF004) {
            long len = officeArtRecHeader.recLength;
            pictureOffset += 8;
            officeArtRecHeader = null;
            while (len > 0 && pictureOffset < _dataStream.length) {
                officeArtRecHeader = readHeader(_dataStream, pictureOffset);
                len -= officeArtRecHeader.recLength;
                pictureOffset += 8;

                recVer = getRecVer(officeArtRecHeader);
                recInstance = getRecInstance(officeArtRecHeader);
                if (recVer == 0x3 && officeArtRecHeader.recType == 0xF00B) {
                    int nPropertyNum = getRecInstance(officeArtRecHeader);
                    int nIndex = 0;
                    for (nIndex = 0; nIndex < nPropertyNum; nIndex++) {
                        OfficeArtOpid officeArtOpid = readOfficeArtOpid(_dataStream, pictureOffset);
                        short opid = getOpid(officeArtOpid);
                        boolean bBid = isfBid(officeArtOpid);
                        boolean bComplex = isfComplex(officeArtOpid);
                        byte[] fixedPoint = null;

                        if (opid == 0x0100 && !bBid && !bComplex) {
                            fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
                            this.dyaCropTop = getRealNumFromFixedPoint(fixedPoint);
                        }
                        if (opid == 0x0101 && !bBid && !bComplex) {
                            fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
                            this.dyaCropBottom = getRealNumFromFixedPoint(fixedPoint);
                        }
                        if (opid == 0x0102 && !bBid && !bComplex) {
                            fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
                            this.dxaCropLeft = getRealNumFromFixedPoint(fixedPoint);
                        }
                        if (opid == 0x0103 && !bBid && !bComplex) {
                            fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
                            this.dxaCropRight = getRealNumFromFixedPoint(fixedPoint);
                        }
                        if (opid == 0x0109 && !bBid && !bComplex) {
                            int propValue = LittleEndian.getInt(_dataStream, pictureOffset + 2);
                            this.bSetBright = true;
                            this.fBright = (float) propValue / 32768.0f * 255;
                        }
                        if (opid == 0x0108 && !bBid && !bComplex) {
                            int propValue = LittleEndian.getInt(_dataStream, pictureOffset + 2);
                            this.bSetContrast = true;
                            this.fContrast = Math.min(propValue / 65536f, 10);
                        }
                        if (opid == 0x013F && !bBid && !bComplex) {
                            BlipBooleanProperties blipProperties = readBlipBooleanProperties(_dataStream, pictureOffset + 2);

                            if (isfUsefPictureBiLevel(blipProperties)) {
                                if (isfPictureBiLevel(blipProperties)) {
                                    this.bSetThreshold = true;
                                    this.fThreshold = 128;
                                }
                            } else if (isfUsefPictureGray(blipProperties)) {
                                if (isfPictureGray(blipProperties)) {
                                    this.bSetGrayScl = true;
                                    this.bGrayScl = true;
                                }
                            }
                        }

                        pictureOffset += 6;
                    }

                    break;
                }

                pictureOffset += officeArtRecHeader.recLength;
            }
        }
    }

    public float getBright() {
        return fBright;
    }

    public float getContrast() {
        return fContrast;
    }

    public boolean isGrayScl() {
        return bGrayScl;
    }

    public float getThreshold() {
        return fThreshold;
    }

    public boolean isSetBright() {
        return bSetBright;
    }

    public boolean isSetContrast() {
        return bSetContrast;
    }

    public boolean isSetGrayScl() {
        return bSetGrayScl;
    }

    public boolean isSetThreshold() {
        return bSetThreshold;
    }

    private short getRecVer(OfficeArtRecordHeader officeArtRecHeader) {
        return (short) officeArtRecHeader.recVer.getValue(officeArtRecHeader.recFlag);
    }

    private short getRecInstance(OfficeArtRecordHeader officeArtRecHeader) {
        return (short) officeArtRecHeader.recInstance.getValue(officeArtRecHeader.recFlag);
    }

    private OfficeArtRecordHeader readHeader(byte[] _dataStream, int startoffset) {
        OfficeArtRecordHeader officeArt = new OfficeArtRecordHeader();
        officeArt.recVer = new BitField(0x000F);
        officeArt.recInstance = new BitField(0xFFF0);
        if (startoffset + 4 < _dataStream.length) {
            officeArt.recFlag = LittleEndian.getShort(_dataStream, startoffset);
            officeArt.recType = LittleEndian.getUShort(_dataStream, startoffset + 2);
            officeArt.recLength = LittleEndian.getUInt(_dataStream, startoffset + 4);
        }
        return officeArt;
    }

    private OfficeArtOpid readOfficeArtOpid(byte[] _dataStream, int startoffset) {
        OfficeArtOpid officeArtOpid = new OfficeArtOpid();
        officeArtOpid.opid = new BitField(0x3FFF);
        officeArtOpid.fBid = new BitField(0x4000);
        officeArtOpid.fComplex = new BitField(0x8000);
        officeArtOpid.flag = LittleEndian.getShort(_dataStream, startoffset);
        return officeArtOpid;
    }

    private short getOpid(OfficeArtOpid officeArtOpid) {
        return officeArtOpid.opid.getShortValue(officeArtOpid.flag);
    }

    private boolean isfBid(OfficeArtOpid officeArtOpid) {
        return officeArtOpid.fBid.getShortValue(officeArtOpid.flag) == 1;
    }

    private boolean isfComplex(OfficeArtOpid officeArtOpid) {
        return officeArtOpid.fComplex.getShortValue(officeArtOpid.flag) == 1;
    }

    private float getRealNumFromFixedPoint(byte[] data) {
        short integral = LittleEndian.getShort(data, 2);
        int fractional = LittleEndian.getUShort(data, 0);
        return integral + (fractional / 65536.0f);
    }

    private BlipBooleanProperties readBlipBooleanProperties(byte[] _dataStream, int startoffset) {
        BlipBooleanProperties properties = new BlipBooleanProperties();
        properties.propValue = LittleEndian.getInt(_dataStream, startoffset);
        properties.fPictureBiLevel = new BitField(0x20000);
        properties.fUsefPictureBiLevel = new BitField(0x2);
        properties.fPictureGray = new BitField(0x40000);
        properties.fUsefPictureGray = new BitField(0x4);
        return properties;
    }

    private boolean isfUsefPictureBiLevel(BlipBooleanProperties properties) {
        return properties.fUsefPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfPictureBiLevel(BlipBooleanProperties properties) {
        return properties.fPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfUsefPictureGray(BlipBooleanProperties properties) {
        return properties.fUsefPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfPictureGray(BlipBooleanProperties properties) {
        return properties.fPictureBiLevel.getValue(properties.propValue) == 1;
    }

    public short getZoomX() {
        return this.mx;
    }

    public short getZoomY() {
        return this.my;
    }

    @Override
    public String toString() {

        String stringBuilder = "[PICF]\n" +
                "        lcb           = " + this.lcb + '\n' +
                "        cbHeader      = " + this.cbHeader + '\n' +
                "        mfp.mm        = " + this.mfp_mm + '\n' +
                "        mfp.xExt      = " + this.mfp_xExt + '\n' +
                "        mfp.yExt      = " + this.mfp_yExt + '\n' +
                "        mfp.hMF       = " + this.mfp_hMF + '\n' +
                "        offset14      = " + Arrays.toString(this.offset14) +
                '\n' +
                "        dxaGoal       = " + this.dxaGoal + '\n' +
                "        dyaGoal       = " + this.dyaGoal + '\n' +
                "        dxaCropLeft   = " + this.dxaCropLeft + '\n' +
                "        dyaCropTop    = " + this.dyaCropTop + '\n' +
                "        dxaCropRight  = " + this.dxaCropRight + '\n' +
                "        dyaCropBottom = " + this.dyaCropBottom + '\n' +
                "[/PICF]";
        return stringBuilder;
    }

    public class BlipBooleanProperties {
        public BitField fPictureBiLevel;
        public BitField fPictureGray;
        public BitField fUsefPictureBiLevel;
        public BitField fUsefPictureGray;
        int propValue;
    }

    public class OfficeArtRecordHeader {
        public BitField recVer;
        public BitField recInstance;
        public short recFlag;
        public int recType;
        public long recLength;
    }

    public class OfficeArtOpid {
        public BitField opid;
        public BitField fBid;
        public BitField fComplex;
        public short flag;
    }
}
