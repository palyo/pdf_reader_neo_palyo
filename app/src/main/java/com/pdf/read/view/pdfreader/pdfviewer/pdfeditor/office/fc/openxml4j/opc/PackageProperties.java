package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc;

import java.util.Date;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.util.Nullable;

public interface PackageProperties {

    String NAMESPACE_DCTERMS = "http://purl.org/dc/terms/";

    String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";

    Nullable<String> getCategoryProperty();

    void setCategoryProperty(String category);

    Nullable<String> getContentStatusProperty();

    void setContentStatusProperty(String contentStatus);

    Nullable<String> getContentTypeProperty();

    void setContentTypeProperty(String contentType);

    Nullable<Date> getCreatedProperty();

    void setCreatedProperty(String created);

    void setCreatedProperty(Nullable<Date> created);

    Nullable<String> getCreatorProperty();

    void setCreatorProperty(String creator);

    Nullable<String> getDescriptionProperty();

    void setDescriptionProperty(String description);

    Nullable<String> getIdentifierProperty();

    void setIdentifierProperty(String identifier);

    Nullable<String> getKeywordsProperty();

    void setKeywordsProperty(String keywords);

    Nullable<String> getLanguageProperty();

    void setLanguageProperty(String language);

    Nullable<String> getLastModifiedByProperty();

    void setLastModifiedByProperty(String lastModifiedBy);

    Nullable<Date> getLastPrintedProperty();

    void setLastPrintedProperty(String lastPrinted);

    void setLastPrintedProperty(Nullable<Date> lastPrinted);

    Nullable<Date> getModifiedProperty();

    void setModifiedProperty(String modified);

    void setModifiedProperty(Nullable<Date> modified);

    Nullable<String> getRevisionProperty();

    void setRevisionProperty(String revision);

    Nullable<String> getSubjectProperty();

    void setSubjectProperty(String subject);

    Nullable<String> getTitleProperty();

    void setTitleProperty(String title);

    Nullable<String> getVersionProperty();

    void setVersionProperty(String version);
}
