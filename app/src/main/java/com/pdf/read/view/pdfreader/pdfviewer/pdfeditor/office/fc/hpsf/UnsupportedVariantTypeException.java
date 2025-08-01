package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;

public abstract class UnsupportedVariantTypeException extends VariantTypeException {

    public UnsupportedVariantTypeException(final long variantType, final Object value) {
        super(variantType, value, "HPSF does not yet support the variant type " + variantType + " (" + Variant.getVariantName(variantType) + ", " + HexDump.toHex(variantType) + "). If you want support for " + "this variant type in one of the next POI releases please " + "submit a request for enhancement (RFE) to " + "<http://issues.apache.org/bugzilla/>! Thank you!");
    }
}
