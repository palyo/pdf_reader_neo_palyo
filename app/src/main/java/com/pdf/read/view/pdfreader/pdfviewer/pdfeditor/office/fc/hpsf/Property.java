package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;

public class Property {
    protected long id;
    protected long type;
    protected Object value;

    public Property(final long id, final long type, final Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public Property(final long id, final byte[] src, final long offset,
                    final int length, final int codepage)
            throws UnsupportedEncodingException {
        this.id = id;

        if (id == 0) {
            value = readDictionary(src, offset, length, codepage);
            return;
        }

        int o = (int) offset;
        type = LittleEndian.getUInt(src, o);
        o += LittleEndian.INT_SIZE;

        try {
            value = VariantSupport.read(src, o, length, (int) type, codepage);
        } catch (UnsupportedVariantTypeException ex) {
            VariantSupport.writeUnsupportedTypeMessage(ex);
            value = ex.getValue();
        }
    }

    protected Property() {
    }

    public long getID() {
        return id;
    }

    public long getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    protected Map readDictionary(final byte[] src, final long offset,
                                 final int length, final int codepage)
            throws UnsupportedEncodingException {

        if (offset < 0 || offset > src.length)
            throw new HPSFRuntimeException
                    ("Illegal offset " + offset + " while HPSF stream contains " +
                            length + " bytes.");
        int o = (int) offset;

        final long nrEntries = LittleEndian.getUInt(src, o);
        o += LittleEndian.INT_SIZE;

        final Map m = new HashMap((int) nrEntries, (float) 1.0);

        try {
            for (int i = 0; i < nrEntries; i++) {

                final Long id = Long.valueOf(LittleEndian.getUInt(src, o));
                o += LittleEndian.INT_SIZE;

                long sLength = LittleEndian.getUInt(src, o);
                o += LittleEndian.INT_SIZE;

                final StringBuffer b = new StringBuffer();
                switch (codepage) {
                    case -1: {

                        b.append(new String(src, o, (int) sLength));
                        break;
                    }
                    case Constants.CP_UNICODE: {

                        final int nrBytes = (int) (sLength * 2);
                        final byte[] h = new byte[nrBytes];
                        for (int i2 = 0; i2 < nrBytes; i2 += 2) {
                            h[i2] = src[o + i2 + 1];
                            h[i2 + 1] = src[o + i2];
                        }
                        b.append(new String(h, 0, nrBytes,
                                VariantSupport.codepageToEncoding(codepage)));
                        break;
                    }
                    default: {

                        b.append(new String(src, o, (int) sLength,
                                VariantSupport.codepageToEncoding(codepage)));
                        break;
                    }
                }

                while (b.length() > 0 && b.charAt(b.length() - 1) == 0x00)
                    b.setLength(b.length() - 1);
                if (codepage == Constants.CP_UNICODE) {
                    if (sLength % 2 == 1)
                        sLength++;
                    o += (sLength + sLength);
                } else
                    o += sLength;
                m.put(id, b.toString());
            }
        } catch (RuntimeException ex) {
            final POILogger l = POILogFactory.getLogger(getClass());
            l.log(POILogger.WARN,
                    "The property set's dictionary contains bogus data. "
                            + "All dictionary entries starting with the one with ID "
                            + id + " will be ignored.", ex);
        }
        return m;
    }

    protected int getSize() throws WritingNotSupportedException {
        int length = VariantSupport.getVariantLength(type);
        if (length >= 0)
            return length;
        if (length == -2)

            throw new WritingNotSupportedException(type, null);

        final int PADDING = 4;
        switch ((int) type) {
            case Variant.VT_LPSTR: {
                int l = ((String) value).length() + 1;
                int r = l % PADDING;
                if (r > 0)
                    l += PADDING - r;
                length += l;
                break;
            }
            case Variant.VT_EMPTY:
                break;
            default:
                throw new WritingNotSupportedException(type, value);
        }
        return length;
    }

    public boolean equals(final Object o) {
        if (!(o instanceof Property)) {
            return false;
        }
        final Property p = (Property) o;
        final Object pValue = p.getValue();
        final long pId = p.getID();
        if (id != pId || (id != 0 && !typesAreEqual(type, p.getType())))
            return false;
        if (value == null && pValue == null)
            return true;
        if (value == null || pValue == null)
            return false;

        final Class<?> valueClass = value.getClass();
        final Class<?> pValueClass = pValue.getClass();
        if (!(valueClass.isAssignableFrom(pValueClass)) &&
                !(pValueClass.isAssignableFrom(valueClass)))
            return false;

        if (value instanceof byte[])
            return Util.equal((byte[]) value, (byte[]) pValue);

        return value.equals(pValue);
    }

    private boolean typesAreEqual(final long t1, final long t2) {
        return t1 == t2 ||
                (t1 == Variant.VT_LPSTR && t2 == Variant.VT_LPWSTR) ||
                (t2 == Variant.VT_LPSTR && t1 == Variant.VT_LPWSTR);
    }

    public int hashCode() {
        long hashCode = 0;
        hashCode += id;
        hashCode += type;
        if (value != null)
            hashCode += value.hashCode();
        final int returnHashCode = (int) (hashCode & 0x0ffffffffL);
        return returnHashCode;

    }

    public String toString() {
        final StringBuffer b = new StringBuffer();
        b.append(getClass().getName());
        b.append('[');
        b.append("id: ");
        b.append(getID());
        b.append(", type: ");
        b.append(getType());
        final Object value = getValue();
        b.append(", value: ");
        b.append(value.toString());
        if (value instanceof String) {
            final String s = (String) value;
            final int l = s.length();
            final byte[] bytes = new byte[l * 2];
            for (int i = 0; i < l; i++) {
                final char c = s.charAt(i);
                final byte high = (byte) ((c & 0x00ff00) >> 8);
                final byte low = (byte) ((c & 0x0000ff) >> 0);
                bytes[i * 2] = high;
                bytes[i * 2 + 1] = low;
            }
            final String hex = HexDump.dump(bytes, 0L, 0);
            b.append(" [");
            b.append(hex);
            b.append("]");
        }
        b.append(']');
        return b.toString();
    }
}
