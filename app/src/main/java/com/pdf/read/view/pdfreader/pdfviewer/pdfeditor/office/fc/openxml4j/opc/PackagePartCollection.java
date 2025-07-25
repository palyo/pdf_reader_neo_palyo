package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc;

import java.util.TreeMap;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.exceptions.InvalidOperationException;

public final class PackagePartCollection extends TreeMap<PackagePartName, PackagePart> {

    private static final long serialVersionUID = 2515031135957635515L;

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public PackagePart put(PackagePartName partName, PackagePart part) {
        if (this.containsKey(partName)) {
            throw new InvalidOperationException(
                    "You can't add a part with a part name derived from another part ! [M1.11]");
        }

        return super.put(partName, part);
    }

    @Override
    public PackagePart remove(Object key) {

        return super.remove(key);
    }
}
