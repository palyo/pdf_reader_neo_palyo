package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class PropertyModifier implements Cloneable {

    private static final BitField _fComplex = new BitField(0x0001);

    private static final BitField _figrpprl = new BitField(0xfffe);

    private static final BitField _fisprm = new BitField(0x00fe);

    private static final BitField _fval = new BitField(0xff00);

    private final short value;

    public PropertyModifier(short value) {
        this.value = value;
    }

    @Override
    protected PropertyModifier clone() throws CloneNotSupportedException {
        return new PropertyModifier(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyModifier other = (PropertyModifier) obj;
        return value == other.value;
    }

    public short getIgrpprl() {
        if (!isComplex())
            throw new IllegalStateException("Not complex");

        return _figrpprl.getShortValue(value);
    }

    public short getIsprm() {
        if (isComplex())
            throw new IllegalStateException("Not simple");

        return _fisprm.getShortValue(value);
    }

    public short getVal() {
        if (isComplex())
            throw new IllegalStateException("Not simple");

        return _fval.getShortValue(value);
    }

    public short getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    public boolean isComplex() {
        return _fComplex.isSet(value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[PRM] (complex: ");
        stringBuilder.append(isComplex());
        stringBuilder.append("; ");
        if (isComplex()) {
            stringBuilder.append("igrpprl: ");
            stringBuilder.append(getIgrpprl());
            stringBuilder.append("; ");
        } else {
            stringBuilder.append("isprm: ");
            stringBuilder.append(getIsprm());
            stringBuilder.append("; ");
            stringBuilder.append("val: ");
            stringBuilder.append(getVal());
            stringBuilder.append("; ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
