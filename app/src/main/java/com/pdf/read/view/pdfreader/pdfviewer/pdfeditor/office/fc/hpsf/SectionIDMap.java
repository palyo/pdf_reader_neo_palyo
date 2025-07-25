package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.util.HashMap;

public class SectionIDMap extends HashMap {

    public static final byte[] SUMMARY_INFORMATION_ID = new byte[]
            {
                    (byte) 0xF2, (byte) 0x9F, (byte) 0x85, (byte) 0xE0,
                    (byte) 0x4F, (byte) 0xF9, (byte) 0x10, (byte) 0x68,
                    (byte) 0xAB, (byte) 0x91, (byte) 0x08, (byte) 0x00,
                    (byte) 0x2B, (byte) 0x27, (byte) 0xB3, (byte) 0xD9
            };

    public static final byte[][] DOCUMENT_SUMMARY_INFORMATION_ID = new byte[][]
            {
                    {
                            (byte) 0xD5, (byte) 0xCD, (byte) 0xD5, (byte) 0x02,
                            (byte) 0x2E, (byte) 0x9C, (byte) 0x10, (byte) 0x1B,
                            (byte) 0x93, (byte) 0x97, (byte) 0x08, (byte) 0x00,
                            (byte) 0x2B, (byte) 0x2C, (byte) 0xF9, (byte) 0xAE
                    },
                    {
                            (byte) 0xD5, (byte) 0xCD, (byte) 0xD5, (byte) 0x05,
                            (byte) 0x2E, (byte) 0x9C, (byte) 0x10, (byte) 0x1B,
                            (byte) 0x93, (byte) 0x97, (byte) 0x08, (byte) 0x00,
                            (byte) 0x2B, (byte) 0x2C, (byte) 0xF9, (byte) 0xAE
                    }
            };

    public static final String UNDEFINED = "[undefined]";

    private static SectionIDMap defaultMap;

    public static SectionIDMap getInstance() {
        if (defaultMap == null) {
            final SectionIDMap m = new SectionIDMap();
            m.put(SUMMARY_INFORMATION_ID,
                    PropertyIDMap.getSummaryInformationProperties());
            m.put(DOCUMENT_SUMMARY_INFORMATION_ID[0],
                    PropertyIDMap.getDocumentSummaryInformationProperties());
            defaultMap = m;
        }
        return defaultMap;
    }

    public static String getPIDString(final byte[] sectionFormatID, final long pid) {
        final PropertyIDMap m = getInstance().get(sectionFormatID);
        if (m == null) {
            return UNDEFINED;
        }
        final String s = (String) m.get(pid);
        if (s == null)
            return UNDEFINED;
        return s;
    }

    public PropertyIDMap get(final byte[] sectionFormatID) {
        return (PropertyIDMap) super.get(new String(sectionFormatID));
    }

    public Object get(final Object sectionFormatID) {
        return get((byte[]) sectionFormatID);
    }

    public Object put(final byte[] sectionFormatID, final PropertyIDMap propertyIDMap) {
        return super.put(new String(sectionFormatID), propertyIDMap);
    }

    public Object put(final Object key, final Object value) {
        return put((byte[]) key, (PropertyIDMap) value);
    }
}
