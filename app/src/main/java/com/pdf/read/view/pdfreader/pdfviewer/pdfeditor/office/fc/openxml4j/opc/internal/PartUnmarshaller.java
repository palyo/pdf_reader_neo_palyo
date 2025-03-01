package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.OpenXML4JException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal.unmarshallers.UnmarshallContext;

public interface PartUnmarshaller {

    PackagePart unmarshall(UnmarshallContext context, InputStream in)
            throws InvalidFormatException, IOException;
}
