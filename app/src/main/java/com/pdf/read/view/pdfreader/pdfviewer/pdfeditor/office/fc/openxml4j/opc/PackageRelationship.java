package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc;

import java.net.URI;
import java.net.URISyntaxException;

public final class PackageRelationship {

    public static final String ID_ATTRIBUTE_NAME = "Id";
    public static final String RELATIONSHIPS_TAG_NAME = "Relationships";

    public static final String RELATIONSHIP_TAG_NAME = "Relationship";
    public static final String TARGET_ATTRIBUTE_NAME = "Target";
    public static final String TARGET_MODE_ATTRIBUTE_NAME = "TargetMode";
    public static final String TYPE_ATTRIBUTE_NAME = "Type";
    private static URI containerRelationshipPart;

    static {
        try {
            containerRelationshipPart = new URI("/_rels/.rels");
        } catch (URISyntaxException e) {

        }
    }

    private final String id;

    private final ZipPackage container;

    private final String relationshipType;

    private final PackagePart source;

    private final TargetMode targetMode;

    private final URI targetUri;

    public PackageRelationship(ZipPackage pkg, PackagePart sourcePart, URI targetUri,
                               TargetMode targetMode, String relationshipType, String id) {
        if (pkg == null) {
            throw new IllegalArgumentException("pkg");
        }
        if (targetUri == null) {
            throw new IllegalArgumentException("targetUri");
        }
        if (relationshipType == null) {
            throw new IllegalArgumentException("relationshipType");
        }
        if (id == null) {
            throw new IllegalArgumentException("id");
        }

        this.container = pkg;
        this.source = sourcePart;
        this.targetUri = targetUri;
        this.targetMode = targetMode;
        this.relationshipType = relationshipType;
        this.id = id;
    }

    public static URI getContainerPartRelationship() {
        return containerRelationshipPart;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PackageRelationship)) {
            return false;
        }
        PackageRelationship rel = (PackageRelationship) obj;
        return (this.id.equals(rel.id)
                && this.relationshipType.equals(rel.relationshipType)
                && (rel.source == null || rel.source.equals(this.source))
                && this.targetMode == rel.targetMode
                && this.targetUri.equals(rel.targetUri));
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + this.relationshipType.hashCode()
                + (this.source == null ? 0 : this.source.hashCode()) + this.targetMode.hashCode()
                + this.targetUri.hashCode();
    }

    public ZipPackage getPackage() {
        return container;
    }

    public String getId() {
        return id;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public PackagePart getSource() {
        return source;
    }

    public URI getSourceURI() {
        if (source == null) {
            return PackagingURIHelper.PACKAGE_ROOT_URI;
        }
        return source._partName.getURI();
    }

    public TargetMode getTargetMode() {
        return targetMode;
    }

    public URI getTargetURI() {

        if (targetMode == TargetMode.EXTERNAL) {
            return targetUri;
        }

        if (!targetUri.toASCIIString().startsWith("/")) {

            return PackagingURIHelper.resolvePartUri(getSourceURI(), targetUri);
        }
        return targetUri;
    }

    @Override
    public String toString() {
        String sb = (id == null ? "id=null" : "id=" + id) +
                (container == null ? " - container=null" : " - container=" + container) +
                (relationshipType == null ? " - relationshipType=null" : " - relationshipType="
                        + relationshipType) +
                (source == null ? " - source=null" : " - source=" + getSourceURI().toASCIIString()) +
                (targetUri == null ? " - target=null" : " - target="
                        + getTargetURI().toASCIIString()) +
                (targetMode == null ? ",targetMode=null" : ",targetMode=" + targetMode);
        return sb;
    }
}
