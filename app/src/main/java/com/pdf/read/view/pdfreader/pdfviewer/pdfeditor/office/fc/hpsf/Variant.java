package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Variant {
    public static final int VT_EMPTY = 0;

    public static final int VT_NULL = 1;

    public static final int VT_I2 = 2;

    public static final int VT_I4 = 3;

    public static final int VT_R4 = 4;

    public static final int VT_R8 = 5;

    public static final int VT_CY = 6;

    public static final int VT_DATE = 7;

    public static final int VT_BSTR = 8;

    public static final int VT_DISPATCH = 9;

    public static final int VT_ERROR = 10;

    public static final int VT_BOOL = 11;

    public static final int VT_VARIANT = 12;

    public static final int VT_UNKNOWN = 13;

    public static final int VT_DECIMAL = 14;
    public static final int VT_I1 = 16;

    public static final int VT_UI1 = 17;

    public static final int VT_UI2 = 18;

    public static final int VT_UI4 = 19;

    public static final int VT_I8 = 20;

    public static final int VT_UI8 = 21;

    public static final int VT_INT = 22;

    public static final int VT_UINT = 23;

    public static final int VT_VOID = 24;

    public static final int VT_HRESULT = 25;

    public static final int VT_PTR = 26;

    public static final int VT_SAFEARRAY = 27;

    public static final int VT_CARRAY = 28;

    public static final int VT_USERDEFINED = 29;

    public static final int VT_LPSTR = 30;

    public static final int VT_LPWSTR = 31;

    public static final int VT_FILETIME = 64;

    public static final int VT_BLOB = 65;

    public static final int VT_STREAM = 66;

    public static final int VT_STORAGE = 67;

    public static final int VT_STREAMED_OBJECT = 68;

    public static final int VT_STORED_OBJECT = 69;

    public static final int VT_BLOB_OBJECT = 70;

    public static final int VT_CF = 71;

    public static final int VT_CLSID = 72;

    public static final int VT_VECTOR = 0x1000;

    public static final int VT_ARRAY = 0x2000;

    public static final int VT_BYREF = 0x4000;

    public static final int VT_RESERVED = 0x8000;

    public static final int VT_ILLEGAL = 0xFFFF;

    public static final int VT_ILLEGALMASKED = 0xFFF;

    public static final int VT_TYPEMASK = 0xFFF;

    public static final Integer LENGTH_UNKNOWN = Integer.valueOf(-2);

    public static final Integer LENGTH_VARIABLE = Integer.valueOf(-1);

    public static final Integer LENGTH_0 = Integer.valueOf(0);

    public static final Integer LENGTH_2 = Integer.valueOf(2);

    public static final Integer LENGTH_4 = Integer.valueOf(4);

    public static final Integer LENGTH_8 = Integer.valueOf(8);

    private static final Map numberToName;
    private static final Map numberToLength;

    static {

        Map tm1 = new HashMap();
        tm1.put(Long.valueOf(0), "VT_EMPTY");
        tm1.put(Long.valueOf(1), "VT_NULL");
        tm1.put(Long.valueOf(2), "VT_I2");
        tm1.put(Long.valueOf(3), "VT_I4");
        tm1.put(Long.valueOf(4), "VT_R4");
        tm1.put(Long.valueOf(5), "VT_R8");
        tm1.put(Long.valueOf(6), "VT_CY");
        tm1.put(Long.valueOf(7), "VT_DATE");
        tm1.put(Long.valueOf(8), "VT_BSTR");
        tm1.put(Long.valueOf(9), "VT_DISPATCH");
        tm1.put(Long.valueOf(10), "VT_ERROR");
        tm1.put(Long.valueOf(11), "VT_BOOL");
        tm1.put(Long.valueOf(12), "VT_VARIANT");
        tm1.put(Long.valueOf(13), "VT_UNKNOWN");
        tm1.put(Long.valueOf(14), "VT_DECIMAL");
        tm1.put(Long.valueOf(16), "VT_I1");
        tm1.put(Long.valueOf(17), "VT_UI1");
        tm1.put(Long.valueOf(18), "VT_UI2");
        tm1.put(Long.valueOf(19), "VT_UI4");
        tm1.put(Long.valueOf(20), "VT_I8");
        tm1.put(Long.valueOf(21), "VT_UI8");
        tm1.put(Long.valueOf(22), "VT_INT");
        tm1.put(Long.valueOf(23), "VT_UINT");
        tm1.put(Long.valueOf(24), "VT_VOID");
        tm1.put(Long.valueOf(25), "VT_HRESULT");
        tm1.put(Long.valueOf(26), "VT_PTR");
        tm1.put(Long.valueOf(27), "VT_SAFEARRAY");
        tm1.put(Long.valueOf(28), "VT_CARRAY");
        tm1.put(Long.valueOf(29), "VT_USERDEFINED");
        tm1.put(Long.valueOf(30), "VT_LPSTR");
        tm1.put(Long.valueOf(31), "VT_LPWSTR");
        tm1.put(Long.valueOf(64), "VT_FILETIME");
        tm1.put(Long.valueOf(65), "VT_BLOB");
        tm1.put(Long.valueOf(66), "VT_STREAM");
        tm1.put(Long.valueOf(67), "VT_STORAGE");
        tm1.put(Long.valueOf(68), "VT_STREAMED_OBJECT");
        tm1.put(Long.valueOf(69), "VT_STORED_OBJECT");
        tm1.put(Long.valueOf(70), "VT_BLOB_OBJECT");
        tm1.put(Long.valueOf(71), "VT_CF");
        tm1.put(Long.valueOf(72), "VT_CLSID");
        Map tm2 = new HashMap(tm1.size(), 1.0F);
        tm2.putAll(tm1);
        numberToName = Collections.unmodifiableMap(tm2);

        tm1.clear();
        tm1.put(Long.valueOf(0), LENGTH_0);
        tm1.put(Long.valueOf(1), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(2), LENGTH_2);
        tm1.put(Long.valueOf(3), LENGTH_4);
        tm1.put(Long.valueOf(4), LENGTH_4);
        tm1.put(Long.valueOf(5), LENGTH_8);
        tm1.put(Long.valueOf(6), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(7), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(8), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(9), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(10), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(11), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(12), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(13), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(14), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(16), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(17), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(18), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(19), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(20), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(21), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(22), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(23), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(24), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(25), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(26), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(27), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(28), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(29), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(30), LENGTH_VARIABLE);
        tm1.put(Long.valueOf(31), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(64), LENGTH_8);
        tm1.put(Long.valueOf(65), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(66), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(67), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(68), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(69), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(70), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(71), LENGTH_UNKNOWN);
        tm1.put(Long.valueOf(72), LENGTH_UNKNOWN);
        tm2 = new HashMap(tm1.size(), 1.0F);
        tm2.putAll(tm1);
        numberToLength = Collections.unmodifiableMap(tm2);
    }

    public static String getVariantName(final long variantType) {
        final String name = (String) numberToName.get(Long.valueOf(variantType));
        return name != null ? name : "unknown variant type";
    }

    public static int getVariantLength(final long variantType) {
        final Long key = Long.valueOf((int) variantType);
        final Long length = (Long) numberToLength.get(key);
        if (length == null)
            return -2;
        return length.intValue();
    }

}
