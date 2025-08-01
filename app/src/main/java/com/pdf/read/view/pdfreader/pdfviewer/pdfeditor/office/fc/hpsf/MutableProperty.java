package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.OutputStream;

public class MutableProperty extends Property {

    public MutableProperty() {
    }

    public MutableProperty(final Property p) {
        setID(p.getID());
        setType(p.getType());
        setValue(p.getValue());
    }

    public void setID(final long id) {
        this.id = id;
    }

    public void setType(final long type) {
        this.type = type;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public int write(final OutputStream out, final int codepage)
            throws IOException, WritingNotSupportedException {
        int length = 0;
        long variantType = getType();

        if (codepage == Constants.CP_UNICODE && variantType == Variant.VT_LPSTR)
            variantType = Variant.VT_LPWSTR;

        length += TypeWriter.writeUIntToStream(out, variantType);
        length += VariantSupport.write(out, variantType, getValue(), codepage);
        return length;
    }
}
