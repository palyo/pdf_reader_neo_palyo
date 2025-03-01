package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal.marshallers;

import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.OpenXML4JException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePartName;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal.PartMarshaller;

public final class ZipPartMarshaller implements PartMarshaller {

    public static boolean marshallRelationshipPart(PackageRelationshipCollection rels,
                                                   PackagePartName relPartName, ZipOutputStream zos) {

        return true;
    }

    public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
        return true;
    }
}
