package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.io.IOException;
import java.io.InputStream;

public class PropertySetFactory {
    public static PropertySet create(final InputStream stream)
            throws HPSFException,
            IOException {
        final PropertySet ps = new PropertySet(stream);
        try {
            if (ps.isSummaryInformation())
                return new SummaryInformation(ps);
            else if (ps.isDocumentSummaryInformation())
                return new DocumentSummaryInformation(ps);
            else
                return ps;
        } catch (HPSFException ex) {

            throw new Error(ex.toString());
        }
    }

    public static SummaryInformation newSummaryInformation() {
        final MutablePropertySet ps = new MutablePropertySet();
        final MutableSection s = (MutableSection) ps.getFirstSection();
        s.setFormatID(SectionIDMap.SUMMARY_INFORMATION_ID);
        try {
            return new SummaryInformation(ps);
        } catch (HPSFException ex) {

            throw new HPSFRuntimeException(ex);
        }
    }

    public static DocumentSummaryInformation newDocumentSummaryInformation() {
        final MutablePropertySet ps = new MutablePropertySet();
        final MutableSection s = (MutableSection) ps.getFirstSection();
        s.setFormatID(SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[0]);
        try {
            return new DocumentSummaryInformation(ps);
        } catch (HPSFException ex) {

            throw new HPSFRuntimeException(ex);
        }
    }
}
