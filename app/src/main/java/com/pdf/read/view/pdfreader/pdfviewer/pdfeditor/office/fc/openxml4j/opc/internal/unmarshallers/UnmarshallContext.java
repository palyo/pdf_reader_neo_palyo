package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.internal.unmarshallers;

import java.util.zip.ZipEntry;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePartName;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.ZipPackage;

public final class UnmarshallContext {

    private ZipPackage _package;

    private PackagePartName partName;

    private ZipEntry zipEntry;

    public UnmarshallContext(ZipPackage targetPackage, PackagePartName partName) {
        this._package = targetPackage;
        this.partName = partName;
    }

    public ZipPackage getPackage() {
        return _package;
    }

    public void setPackage(ZipPackage container) {
        this._package = container;
    }

    PackagePartName getPartName() {
        return partName;
    }

    public void setPartName(PackagePartName partName) {
        this.partName = partName;
    }

    ZipEntry getZipEntry() {
        return zipEntry;
    }

    public void setZipEntry(ZipEntry zipEntry) {
        this.zipEntry = zipEntry;
    }
}
