package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal;

import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.OpenXML4JException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;

public interface PartMarshaller {

    boolean marshall(PackagePart part, OutputStream out) throws OpenXML4JException;
}
