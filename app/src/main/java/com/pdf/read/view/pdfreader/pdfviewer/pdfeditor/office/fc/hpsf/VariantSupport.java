package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianConsts;

public class VariantSupport extends Variant {

    final static public int[] SUPPORTED_TYPES = {Variant.VT_EMPTY,
            Variant.VT_I2, Variant.VT_I4, Variant.VT_I8, Variant.VT_R8,
            Variant.VT_FILETIME, Variant.VT_LPSTR, Variant.VT_LPWSTR,
            Variant.VT_CF, Variant.VT_BOOL};
    protected static List unsupportedMessage;
    private static boolean logUnsupportedTypes = false;

    public static boolean isLogUnsupportedTypes() {
        return logUnsupportedTypes;
    }

    public static void setLogUnsupportedTypes(final boolean logUnsupportedTypes) {
        VariantSupport.logUnsupportedTypes = logUnsupportedTypes;
    }

    protected static void writeUnsupportedTypeMessage
            (final UnsupportedVariantTypeException ex) {
        if (isLogUnsupportedTypes()) {
            if (unsupportedMessage == null)
                unsupportedMessage = new LinkedList();
            Long vt = Long.valueOf(ex.getVariantType());
            if (!unsupportedMessage.contains(vt)) {
                System.err.println(ex.getMessage());
                unsupportedMessage.add(vt);
            }
        }
    }

    public static Object read(final byte[] src, final int offset,
                              final int length, final long type,
                              final int codepage)
            throws ReadingNotSupportedException, UnsupportedEncodingException {
        Object value;
        int o1 = offset;
        int l1 = length - LittleEndian.INT_SIZE;
        long lType = type;

        if (codepage == Constants.CP_UNICODE && type == Variant.VT_LPSTR)
            lType = Variant.VT_LPWSTR;

        switch ((int) lType) {
            case Variant.VT_EMPTY: {
                value = null;
                break;
            }
            case Variant.VT_I2: {

                value = Integer.valueOf(LittleEndian.getShort(src, o1));
                break;
            }
            case Variant.VT_I4: {

                value = Integer.valueOf(LittleEndian.getInt(src, o1));
                break;
            }
            case Variant.VT_I8: {

                value = Long.valueOf(LittleEndian.getLong(src, o1));
                break;
            }
            case Variant.VT_R8: {

                value = new Double(LittleEndian.getDouble(src, o1));
                break;
            }
            case Variant.VT_FILETIME: {

                final long low = LittleEndian.getUInt(src, o1);
                o1 += LittleEndian.INT_SIZE;
                final long high = LittleEndian.getUInt(src, o1);
                value = Util.filetimeToDate((int) high, (int) low);
                break;
            }
            case Variant.VT_LPSTR: {

                final int first = o1 + LittleEndian.INT_SIZE;
                long last = first + LittleEndian.getUInt(src, o1) - 1;
                o1 += LittleEndian.INT_SIZE;
                while (src[(int) last] == 0 && first <= last)
                    last--;
                final int l = (int) (last - first + 1);
                value = codepage != -1 ?
                        new String(src, first, l,
                                codepageToEncoding(codepage)) :
                        new String(src, first, l);
                break;
            }
            case Variant.VT_LPWSTR: {

                final int first = o1 + LittleEndian.INT_SIZE;
                long last = first + LittleEndian.getUInt(src, o1) - 1;
                long l = last - first;
                o1 += LittleEndian.INT_SIZE;
                StringBuffer b = new StringBuffer((int) (last - first));
                for (int i = 0; i <= l; i++) {
                    final int i1 = o1 + (i * 2);
                    final int i2 = i1 + 1;
                    final int high = src[i2] << 8;
                    final int low = src[i1] & 0x00ff;
                    final char c = (char) (high | low);
                    b.append(c);
                }

                while (b.length() > 0 && b.charAt(b.length() - 1) == 0x00)
                    b.setLength(b.length() - 1);
                value = b.toString();
                break;
            }
            case Variant.VT_CF: {
                if (l1 < 0) {

                    l1 = LittleEndian.getInt(src, o1);
                    o1 += LittleEndian.INT_SIZE;
                }
                final byte[] v = new byte[l1];
                System.arraycopy(src, o1, v, 0, v.length);
                value = v;
                break;
            }
            case Variant.VT_BOOL: {

                long bool = LittleEndian.getUInt(src, o1);
                if (bool != 0)
                    value = Boolean.TRUE;
                else
                    value = Boolean.FALSE;
                break;
            }
            default: {
                final byte[] v = new byte[l1];
                System.arraycopy(src, o1, v, 0, l1);
                throw new ReadingNotSupportedException(type, v);
            }
        }
        return value;
    }

    public static String codepageToEncoding(final int codepage)
            throws UnsupportedEncodingException {
        if (codepage <= 0)
            throw new UnsupportedEncodingException
                    ("Codepage number may not be " + codepage);
        switch (codepage) {
            case Constants.CP_UTF16:
                return "UTF-16";
            case Constants.CP_UTF16_BE:
                return "UTF-16BE";
            case Constants.CP_UTF8:
                return "UTF-8";
            case Constants.CP_037:
                return "cp037";
            case Constants.CP_GBK:
                return "GBK";
            case Constants.CP_MS949:
                return "ms949";
            case Constants.CP_WINDOWS_1250:
                return "windows-1250";
            case Constants.CP_WINDOWS_1251:
                return "windows-1251";
            case Constants.CP_WINDOWS_1252:
                return "windows-1252";
            case Constants.CP_WINDOWS_1253:
                return "windows-1253";
            case Constants.CP_WINDOWS_1254:
                return "windows-1254";
            case Constants.CP_WINDOWS_1255:
                return "windows-1255";
            case Constants.CP_WINDOWS_1256:
                return "windows-1256";
            case Constants.CP_WINDOWS_1257:
                return "windows-1257";
            case Constants.CP_WINDOWS_1258:
                return "windows-1258";
            case Constants.CP_JOHAB:
                return "johab";
            case Constants.CP_MAC_ROMAN:
                return "MacRoman";
            case Constants.CP_MAC_JAPAN:
                return "SJIS";
            case Constants.CP_MAC_CHINESE_TRADITIONAL:
                return "Big5";
            case Constants.CP_MAC_KOREAN:
                return "EUC-KR";
            case Constants.CP_MAC_ARABIC:
                return "MacArabic";
            case Constants.CP_MAC_HEBREW:
                return "MacHebrew";
            case Constants.CP_MAC_GREEK:
                return "MacGreek";
            case Constants.CP_MAC_CYRILLIC:
                return "MacCyrillic";
            case Constants.CP_MAC_CHINESE_SIMPLE:
                return "EUC_CN";
            case Constants.CP_MAC_ROMANIA:
                return "MacRomania";
            case Constants.CP_MAC_UKRAINE:
                return "MacUkraine";
            case Constants.CP_MAC_THAI:
                return "MacThai";
            case Constants.CP_MAC_CENTRAL_EUROPE:
                return "MacCentralEurope";
            case Constants.CP_MAC_ICELAND:
                return "MacIceland";
            case Constants.CP_MAC_TURKISH:
                return "MacTurkish";
            case Constants.CP_MAC_CROATIAN:
                return "MacCroatian";
            case Constants.CP_US_ACSII:
            case Constants.CP_US_ASCII2:
                return "US-ASCII";
            case Constants.CP_KOI8_R:
                return "KOI8-R";
            case Constants.CP_ISO_8859_1:
                return "ISO-8859-1";
            case Constants.CP_ISO_8859_2:
                return "ISO-8859-2";
            case Constants.CP_ISO_8859_3:
                return "ISO-8859-3";
            case Constants.CP_ISO_8859_4:
                return "ISO-8859-4";
            case Constants.CP_ISO_8859_5:
                return "ISO-8859-5";
            case Constants.CP_ISO_8859_6:
                return "ISO-8859-6";
            case Constants.CP_ISO_8859_7:
                return "ISO-8859-7";
            case Constants.CP_ISO_8859_8:
                return "ISO-8859-8";
            case Constants.CP_ISO_8859_9:
                return "ISO-8859-9";
            case Constants.CP_ISO_2022_JP1:
            case Constants.CP_ISO_2022_JP2:
            case Constants.CP_ISO_2022_JP3:
                return "ISO-2022-JP";
            case Constants.CP_ISO_2022_KR:
                return "ISO-2022-KR";
            case Constants.CP_EUC_JP:
                return "EUC-JP";
            case Constants.CP_EUC_KR:
                return "EUC-KR";
            case Constants.CP_GB2312:
                return "GB2312";
            case Constants.CP_GB18030:
                return "GB18030";
            case Constants.CP_SJIS:
                return "SJIS";
            default:
                return "cp" + codepage;
        }
    }

    public static int write(final OutputStream out, final long type,
                            final Object value, final int codepage)
            throws IOException, WritingNotSupportedException {
        int length = 0;
        switch ((int) type) {
            case Variant.VT_BOOL: {
                int trueOrFalse;
                if (((Boolean) value).booleanValue())
                    trueOrFalse = 1;
                else
                    trueOrFalse = 0;
                length = TypeWriter.writeUIntToStream(out, trueOrFalse);
                break;
            }
            case Variant.VT_LPSTR: {
                final byte[] bytes =
                        (codepage == -1 ?
                                ((String) value).getBytes() :
                                ((String) value).getBytes(codepageToEncoding(codepage)));
                length = TypeWriter.writeUIntToStream(out, bytes.length + 1);
                final byte[] b = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, b, 0, bytes.length);
                b[b.length - 1] = 0x00;
                out.write(b);
                length += b.length;
                break;
            }
            case Variant.VT_LPWSTR: {
                final int nrOfChars = ((String) value).length() + 1;
                length += TypeWriter.writeUIntToStream(out, nrOfChars);
                char[] s = Util.pad4((String) value);
                for (int i = 0; i < s.length; i++) {
                    final int high = ((s[i] & 0x0000ff00) >> 8);
                    final int low = (s[i] & 0x000000ff);
                    final byte highb = (byte) high;
                    final byte lowb = (byte) low;
                    out.write(lowb);
                    out.write(highb);
                    length += 2;
                }
                out.write(0x00);
                out.write(0x00);
                length += 2;
                break;
            }
            case Variant.VT_CF: {
                final byte[] b = (byte[]) value;
                out.write(b);
                length = b.length;
                break;
            }
            case Variant.VT_EMPTY: {
                TypeWriter.writeUIntToStream(out, Variant.VT_EMPTY);
                length = LittleEndianConsts.INT_SIZE;
                break;
            }
            case Variant.VT_I2: {
                TypeWriter.writeToStream(out, ((Integer) value).shortValue());
                length = LittleEndianConsts.SHORT_SIZE;
                break;
            }
            case Variant.VT_I4: {
                if (!(value instanceof Integer)) {
                    throw new ClassCastException("Could not cast an object to "
                            + Integer.class + ": "
                            + value.getClass() + ", "
                            + value);
                }
                length += TypeWriter.writeToStream(out,
                        ((Integer) value).intValue());
                break;
            }
            case Variant.VT_I8: {
                TypeWriter.writeToStream(out, ((Long) value).longValue());
                length = LittleEndianConsts.LONG_SIZE;
                break;
            }
            case Variant.VT_R8: {
                length += TypeWriter.writeToStream(out,
                        ((Double) value).doubleValue());
                break;
            }
            case Variant.VT_FILETIME: {
                long filetime = Util.dateToFileTime((Date) value);
                int high = (int) ((filetime >> 32) & 0x00000000FFFFFFFFL);
                int low = (int) (filetime & 0x00000000FFFFFFFFL);
                length += TypeWriter.writeUIntToStream
                        (out, 0x0000000FFFFFFFFL & low);
                length += TypeWriter.writeUIntToStream
                        (out, 0x0000000FFFFFFFFL & high);
                break;
            }
            default: {

                if (value instanceof byte[]) {
                    final byte[] b = (byte[]) value;
                    out.write(b);
                    length = b.length;
                    writeUnsupportedTypeMessage
                            (new WritingNotSupportedException(type, value));
                } else
                    throw new WritingNotSupportedException(type, value);
                break;
            }
        }

        return length;
    }

    public boolean isSupportedType(final int variantType) {
        for (int i = 0; i < SUPPORTED_TYPES.length; i++)
            if (variantType == SUPPORTED_TYPES[i])
                return true;
        return false;
    }
}
