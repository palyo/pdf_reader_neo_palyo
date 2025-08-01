package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util;

import java.math.BigDecimal;
import java.math.BigInteger;

final class NormalisedDecimal {

    private static final int EXPONENT_OFFSET = 14;

    private static final BigDecimal BD_2_POW_24 = new BigDecimal(BigInteger.ONE.shiftLeft(24));

    private static final int LOG_BASE_10_OF_2_TIMES_2_POW_20 = 315653;

    private static final int C_2_POW_19 = 1 << 19;

    private static final int FRAC_HALF = 0x800000;

    private static final long MAX_REP_WHOLE_PART = 0x38D7EA4C68000L;

    private final int _relativeDecimalExponent;

    private final long _wholePart;

    private final int _fractionalPart;

    NormalisedDecimal(long wholePart, int fracPart, int decimalExponent) {
        _wholePart = wholePart;
        _fractionalPart = fracPart;
        _relativeDecimalExponent = decimalExponent;
    }

    public static NormalisedDecimal create(BigInteger frac, int binaryExponent) {

        int pow10;
        if (binaryExponent > 49 || binaryExponent < 46) {

            int x = (29 << 19) - binaryExponent * LOG_BASE_10_OF_2_TIMES_2_POW_20;
            x += C_2_POW_19;
            pow10 = -(x >> 20);
        } else {
            pow10 = 0;
        }
        MutableFPNumber cc = new MutableFPNumber(frac, binaryExponent);
        if (pow10 != 0) {
            cc.multiplyByPowerOfTen(-pow10);
        }

        switch (cc.get64BitNormalisedExponent()) {
            case 46:
                if (cc.isAboveMinRep()) {
                    break;
                }
            case 44:
            case 45:
                cc.multiplyByPowerOfTen(1);
                pow10--;
                break;
            case 47:
            case 48:
                break;
            case 49:
                if (cc.isBelowMaxRep()) {
                    break;
                }
            case 50:
                cc.multiplyByPowerOfTen(-1);
                pow10++;
                break;

            default:
                throw new IllegalStateException("Bad binary exp " + cc.get64BitNormalisedExponent() + ".");
        }
        cc.normalise64bit();

        return cc.createNormalisedDecimal(pow10);
    }

    public NormalisedDecimal roundUnits() {
        long wholePart = _wholePart;
        if (_fractionalPart >= FRAC_HALF) {
            wholePart++;
        }

        int de = _relativeDecimalExponent;

        if (wholePart < MAX_REP_WHOLE_PART) {
            return new NormalisedDecimal(wholePart, 0, de);
        }
        return new NormalisedDecimal(wholePart / 10, 0, de + 1);
    }

    public ExpandedDouble normaliseBaseTwo() {
        MutableFPNumber cc = new MutableFPNumber(composeFrac(), 39);
        cc.multiplyByPowerOfTen(_relativeDecimalExponent);
        cc.normalise64bit();
        return cc.createExpandedDouble();
    }

    BigInteger composeFrac() {
        long wp = _wholePart;
        int fp = _fractionalPart;
        return new BigInteger(new byte[]{
                (byte) (wp >> 56),
                (byte) (wp >> 48),
                (byte) (wp >> 40),
                (byte) (wp >> 32),
                (byte) (wp >> 24),
                (byte) (wp >> 16),
                (byte) (wp >> 8),
                (byte) (wp >> 0),
                (byte) (fp >> 16),
                (byte) (fp >> 8),
                (byte) (fp >> 0),
        });
    }

    public String getSignificantDecimalDigits() {
        return Long.toString(_wholePart);
    }

    public String getSignificantDecimalDigitsLastDigitRounded() {
        long wp = _wholePart + 5;
        StringBuilder sb = new StringBuilder(24);
        sb.append(wp);
        sb.setCharAt(sb.length() - 1, '0');
        return sb.toString();
    }

    public int getDecimalExponent() {
        return _relativeDecimalExponent + EXPONENT_OFFSET;
    }

    public int compareNormalised(NormalisedDecimal other) {
        int cmp = _relativeDecimalExponent - other._relativeDecimalExponent;
        if (cmp != 0) {
            return cmp;
        }
        if (_wholePart > other._wholePart) {
            return 1;
        }
        if (_wholePart < other._wholePart) {
            return -1;
        }
        return _fractionalPart - other._fractionalPart;
    }

    public BigDecimal getFractionalPart() {
        return new BigDecimal(_fractionalPart).divide(BD_2_POW_24);
    }

    private String getFractionalDigits() {
        if (_fractionalPart == 0) {
            return "0";
        }
        return getFractionalPart().toString().substring(2);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" [");
        String ws = String.valueOf(_wholePart);
        sb.append(ws.charAt(0));
        sb.append('.');
        sb.append(ws.substring(1));
        sb.append(' ');
        sb.append(getFractionalDigits());
        sb.append("E");
        sb.append(getDecimalExponent());
        sb.append("]");
        return sb.toString();
    }
}
