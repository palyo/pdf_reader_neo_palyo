package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.util.Iterator;
import java.util.Map;

public class DocumentSummaryInformation extends SpecialPropertySet {

    public static final String DEFAULT_STREAM_NAME =
            "\005DocumentSummaryInformation";

    public DocumentSummaryInformation(final PropertySet ps)
            throws HPSFException {
        super(ps);
        if (!isDocumentSummaryInformation())
            throw new HPSFException
                    ("Not a " + getClass().getName());
    }

    public PropertyIDMap getPropertySetIDMap() {
        return PropertyIDMap.getDocumentSummaryInformationProperties();
    }

    public String getCategory() {
        return (String) getProperty(PropertyIDMap.PID_CATEGORY);
    }

    public void setCategory(final String category) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_CATEGORY, category);
    }

    public void removeCategory() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_CATEGORY);
    }

    public String getPresentationFormat() {
        return (String) getProperty(PropertyIDMap.PID_PRESFORMAT);
    }

    public void setPresentationFormat(final String presentationFormat) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_PRESFORMAT, presentationFormat);
    }

    public void removePresentationFormat() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_PRESFORMAT);
    }

    public int getByteCount() {
        return getPropertyIntValue(PropertyIDMap.PID_BYTECOUNT);
    }

    public void setByteCount(final int byteCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_BYTECOUNT, byteCount);
    }

    public void removeByteCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_BYTECOUNT);
    }

    public int getLineCount() {
        return getPropertyIntValue(PropertyIDMap.PID_LINECOUNT);
    }

    public void setLineCount(final int lineCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_LINECOUNT, lineCount);
    }

    public void removeLineCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_LINECOUNT);
    }

    public int getParCount() {
        return getPropertyIntValue(PropertyIDMap.PID_PARCOUNT);
    }

    public void setParCount(final int parCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_PARCOUNT, parCount);
    }

    public void removeParCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_PARCOUNT);
    }

    public int getSlideCount() {
        return getPropertyIntValue(PropertyIDMap.PID_SLIDECOUNT);
    }

    public void setSlideCount(final int slideCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_SLIDECOUNT, slideCount);
    }

    public void removeSlideCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_SLIDECOUNT);
    }

    public int getNoteCount() {
        return getPropertyIntValue(PropertyIDMap.PID_NOTECOUNT);
    }

    public void setNoteCount(final int noteCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_NOTECOUNT, noteCount);
    }

    public void removeNoteCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_NOTECOUNT);
    }

    public int getHiddenCount() {
        return getPropertyIntValue(PropertyIDMap.PID_HIDDENCOUNT);
    }

    public void setHiddenCount(final int hiddenCount) {
        final MutableSection s = (MutableSection) getSections().get(0);
        s.setProperty(PropertyIDMap.PID_HIDDENCOUNT, hiddenCount);
    }

    public void removeHiddenCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_HIDDENCOUNT);
    }

    public int getMMClipCount() {
        return getPropertyIntValue(PropertyIDMap.PID_MMCLIPCOUNT);
    }

    public void setMMClipCount(final int mmClipCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_MMCLIPCOUNT, mmClipCount);
    }

    public void removeMMClipCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_MMCLIPCOUNT);
    }

    public boolean getScale() {
        return getPropertyBooleanValue(PropertyIDMap.PID_SCALE);
    }

    public void setScale(final boolean scale) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_SCALE, scale);
    }

    public void removeScale() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_SCALE);
    }

    public byte[] getHeadingPair() {
        notYetImplemented("Reading byte arrays ");
        return (byte[]) getProperty(PropertyIDMap.PID_HEADINGPAIR);
    }

    public void setHeadingPair(final byte[] headingPair) {
        notYetImplemented("Writing byte arrays ");
    }

    public void removeHeadingPair() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_HEADINGPAIR);
    }

    public byte[] getDocparts() {
        notYetImplemented("Reading byte arrays");
        return (byte[]) getProperty(PropertyIDMap.PID_DOCPARTS);
    }

    public void setDocparts(final byte[] docparts) {
        notYetImplemented("Writing byte arrays");
    }

    public void removeDocparts() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_DOCPARTS);
    }

    public String getManager() {
        return (String) getProperty(PropertyIDMap.PID_MANAGER);
    }

    public void setManager(final String manager) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_MANAGER, manager);
    }

    public void removeManager() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_MANAGER);
    }

    public String getCompany() {
        return (String) getProperty(PropertyIDMap.PID_COMPANY);
    }

    public void setCompany(final String company) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_COMPANY, company);
    }

    public void removeCompany() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_COMPANY);
    }

    public boolean getLinksDirty() {
        return getPropertyBooleanValue(PropertyIDMap.PID_LINKSDIRTY);
    }

    public void setLinksDirty(final boolean linksDirty) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_LINKSDIRTY, linksDirty);
    }

    public void removeLinksDirty() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_LINKSDIRTY);
    }

    public CustomProperties getCustomProperties() {
        CustomProperties cps = null;
        if (getSectionCount() >= 2) {
            cps = new CustomProperties();
            final Section section = (Section) getSections().get(1);
            final Map<Long, String> dictionary = section.getDictionary();
            final Property[] properties = section.getProperties();
            int propertyCount = 0;
            for (int i = 0; i < properties.length; i++) {
                final Property p = properties[i];
                final long id = p.getID();
                if (id != 0 && id != 1) {
                    propertyCount++;
                    final CustomProperty cp = new CustomProperty(p,
                            dictionary.get(Long.valueOf(id)));
                    cps.put(cp.getName(), cp);
                }
            }
            if (cps.size() != propertyCount)
                cps.setPure(false);
        }
        return cps;
    }

    public void setCustomProperties(final CustomProperties customProperties) {
        ensureSection2();
        final MutableSection section = (MutableSection) getSections().get(1);
        final Map<Long, String> dictionary = customProperties.getDictionary();
        section.clear();

        int cpCodepage = customProperties.getCodepage();
        if (cpCodepage < 0)
            cpCodepage = section.getCodepage();
        if (cpCodepage < 0)
            cpCodepage = Constants.CP_UNICODE;
        customProperties.setCodepage(cpCodepage);
        section.setCodepage(cpCodepage);
        section.setDictionary(dictionary);
        for (final Iterator<CustomProperty> i = customProperties.values().iterator(); i.hasNext(); ) {
            final Property p = i.next();
            section.setProperty(p);
        }
    }

    private void ensureSection2() {
        if (getSectionCount() < 2) {
            MutableSection s2 = new MutableSection();
            s2.setFormatID(SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[1]);
            addSection(s2);
        }
    }

    public void removeCustomProperties() {
        if (getSectionCount() >= 2)
            getSections().remove(1);
        else
            throw new HPSFRuntimeException("Illegal internal format of Document SummaryInformation stream: second section is missing.");
    }

    private void notYetImplemented(final String msg) {
        throw new UnsupportedOperationException(msg + " is not yet implemented.");
    }
}
