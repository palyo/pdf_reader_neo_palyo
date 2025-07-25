package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidOperationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.OpenXML4JException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal.marshallers.ZipPartMarshaller;

public class ZipPackagePart extends PackagePart {

    private ZipEntry zipEntry;

    public ZipPackagePart(ZipPackage container, PackagePartName partName, String contentType)
            throws InvalidFormatException {
        super(container, partName, contentType);
    }

    public ZipPackagePart(ZipPackage container, ZipEntry zipEntry, PackagePartName partName,
                          String contentType) throws InvalidFormatException {
        super(container, partName, contentType);
        this.zipEntry = zipEntry;
    }

    public ZipEntry getZipArchive() {
        return zipEntry;
    }

    @Override
    protected InputStream getInputStreamImpl() throws IOException {

        return _container.getZipArchive().getInputStream(zipEntry);
    }

    @Override
    protected OutputStream getOutputStreamImpl() {
        return null;
    }

    @Override
    public boolean save(OutputStream os) throws OpenXML4JException {
        return new ZipPartMarshaller().marshall(this, os);
    }

    @Override
    public boolean load(InputStream ios) {
        throw new InvalidOperationException("Method not implemented !");
    }

    @Override
    public void close() {
        throw new InvalidOperationException("Method not implemented !");
    }

    @Override
    public void flush() {
        throw new InvalidOperationException("Method not implemented !");
    }
}
