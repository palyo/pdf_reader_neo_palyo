package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidOperationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.OpenXML4JException;

public interface RelationshipSource {

    PackageRelationship addRelationship(PackagePartName targetPartName,
                                        TargetMode targetMode, String relationshipType);

    PackageRelationship addRelationship(PackagePartName targetPartName,
                                        TargetMode targetMode, String relationshipType, String id);

    PackageRelationship addExternalRelationship(String target, String relationshipType);

    PackageRelationship addExternalRelationship(String target, String relationshipType,
                                                String id);

    void clearRelationships();

    void removeRelationship(String id);

    PackageRelationshipCollection getRelationships() throws
            OpenXML4JException;

    PackageRelationship getRelationship(String id);

    PackageRelationshipCollection getRelationshipsByType(String relationshipType)
            throws IllegalArgumentException, OpenXML4JException;

    boolean hasRelationships();

    boolean isRelationshipExists(PackageRelationship rel);

}
