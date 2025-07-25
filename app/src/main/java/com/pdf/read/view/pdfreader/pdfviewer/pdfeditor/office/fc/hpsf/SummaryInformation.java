package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

import java.util.Date;

public final class SummaryInformation extends SpecialPropertySet {

    public static final String DEFAULT_STREAM_NAME = "\005SummaryInformation";

    public SummaryInformation(final PropertySet ps)
            throws HPSFException {
        super(ps);
        if (!isSummaryInformation())
            throw new HPSFException("Not a "
                    + getClass().getName());
    }

    public PropertyIDMap getPropertySetIDMap() {
        return PropertyIDMap.getSummaryInformationProperties();
    }

    public String getTitle() {
        return (String) getProperty(PropertyIDMap.PID_TITLE);
    }

    public void setTitle(final String title) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_TITLE, title);
    }

    public void removeTitle() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_TITLE);
    }

    public String getSubject() {
        return (String) getProperty(PropertyIDMap.PID_SUBJECT);
    }

    public void setSubject(final String subject) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_SUBJECT, subject);
    }

    public void removeSubject() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_SUBJECT);
    }

    public String getAuthor() {
        return (String) getProperty(PropertyIDMap.PID_AUTHOR);
    }

    public void setAuthor(final String author) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_AUTHOR, author);
    }

    public void removeAuthor() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_AUTHOR);
    }

    public String getKeywords() {
        return (String) getProperty(PropertyIDMap.PID_KEYWORDS);
    }

    public void setKeywords(final String keywords) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_KEYWORDS, keywords);
    }

    public void removeKeywords() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_KEYWORDS);
    }

    public String getComments() {
        return (String) getProperty(PropertyIDMap.PID_COMMENTS);
    }

    public void setComments(final String comments) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_COMMENTS, comments);
    }

    public void removeComments() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_COMMENTS);
    }

    public String getTemplate() {
        return (String) getProperty(PropertyIDMap.PID_TEMPLATE);
    }

    public void setTemplate(final String template) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_TEMPLATE, template);
    }

    public void removeTemplate() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_TEMPLATE);
    }

    public String getLastAuthor() {
        return (String) getProperty(PropertyIDMap.PID_LASTAUTHOR);
    }

    public void setLastAuthor(final String lastAuthor) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_LASTAUTHOR, lastAuthor);
    }

    public void removeLastAuthor() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_LASTAUTHOR);
    }

    public String getRevNumber() {
        return (String) getProperty(PropertyIDMap.PID_REVNUMBER);
    }

    public void setRevNumber(final String revNumber) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_REVNUMBER, revNumber);
    }

    public void removeRevNumber() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_REVNUMBER);
    }

    public long getEditTime() {
        final Date d = (Date) getProperty(PropertyIDMap.PID_EDITTIME);
        if (d == null) {
            return 0;
        }
        return Util.dateToFileTime(d);
    }

    public void setEditTime(final long time) {
        final Date d = Util.filetimeToDate(time);
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_EDITTIME, Variant.VT_FILETIME, d);
    }

    public void removeEditTime() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_EDITTIME);
    }

    public Date getLastPrinted() {
        return (Date) getProperty(PropertyIDMap.PID_LASTPRINTED);
    }

    public void setLastPrinted(final Date lastPrinted) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_LASTPRINTED, Variant.VT_FILETIME,
                lastPrinted);
    }

    public void removeLastPrinted() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_LASTPRINTED);
    }

    public Date getCreateDateTime() {
        return (Date) getProperty(PropertyIDMap.PID_CREATE_DTM);
    }

    public void setCreateDateTime(final Date createDateTime) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_CREATE_DTM, Variant.VT_FILETIME,
                createDateTime);
    }

    public void removeCreateDateTime() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_CREATE_DTM);
    }

    public Date getLastSaveDateTime() {
        return (Date) getProperty(PropertyIDMap.PID_LASTSAVE_DTM);
    }

    public void setLastSaveDateTime(final Date time) {
        final MutableSection s = (MutableSection) getFirstSection();
        s
                .setProperty(PropertyIDMap.PID_LASTSAVE_DTM,
                        Variant.VT_FILETIME, time);
    }

    public void removeLastSaveDateTime() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_LASTSAVE_DTM);
    }

    public int getPageCount() {
        return getPropertyIntValue(PropertyIDMap.PID_PAGECOUNT);
    }

    public void setPageCount(final int pageCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_PAGECOUNT, pageCount);
    }

    public void removePageCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_PAGECOUNT);
    }

    public int getWordCount() {
        return getPropertyIntValue(PropertyIDMap.PID_WORDCOUNT);
    }

    public void setWordCount(final int wordCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_WORDCOUNT, wordCount);
    }

    public void removeWordCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_WORDCOUNT);
    }

    public int getCharCount() {
        return getPropertyIntValue(PropertyIDMap.PID_CHARCOUNT);
    }

    public void setCharCount(final int charCount) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_CHARCOUNT, charCount);
    }

    public void removeCharCount() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_CHARCOUNT);
    }

    public byte[] getThumbnail() {
        return (byte[]) getProperty(PropertyIDMap.PID_THUMBNAIL);
    }

    public void setThumbnail(final byte[] thumbnail) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_THUMBNAIL,
                Variant.VT_LPSTR, thumbnail);
    }

    public void removeThumbnail() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_THUMBNAIL);
    }

    public String getApplicationName() {
        return (String) getProperty(PropertyIDMap.PID_APPNAME);
    }

    public void setApplicationName(final String applicationName) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_APPNAME, applicationName);
    }

    public void removeApplicationName() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_APPNAME);
    }

    public int getSecurity() {
        return getPropertyIntValue(PropertyIDMap.PID_SECURITY);
    }

    public void setSecurity(final int security) {
        final MutableSection s = (MutableSection) getFirstSection();
        s.setProperty(PropertyIDMap.PID_SECURITY, security);
    }

    public void removeSecurity() {
        final MutableSection s = (MutableSection) getFirstSection();
        s.removeProperty(PropertyIDMap.PID_SECURITY);
    }
}
