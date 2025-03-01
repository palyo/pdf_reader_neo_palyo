package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader;

import java.util.Hashtable;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.hyperlink.Hyperlink;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationship;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class HyperlinkReader {
    private static final HyperlinkReader hyperlink = new HyperlinkReader();
    private Map<String, Integer> link;

    public static HyperlinkReader instance() {
        return hyperlink;
    }

    public void getHyperlinkList(IControl control, PackagePart packagePart) throws Exception {
        link = new Hashtable<String, Integer>();
        PackageRelationshipCollection hyperlinkRelCollection =
                packagePart.getRelationshipsByType(PackageRelationshipTypes.HYPERLINK_PART);
        for (PackageRelationship hyperlinkRel : hyperlinkRelCollection) {
            String id = hyperlinkRel.getId();
            if (getLinkIndex(id) < 0) {
                link.put(id, control.getSysKit().getHyperlinkManage().addHyperlink(hyperlinkRel.getTargetURI().toString(), Hyperlink.LINK_URL));
            }
        }
    }

    public int getLinkIndex(String id) {
        if (link != null && link.size() > 0) {
            Integer index = link.get(id);
            if (index != null) {
                return index;
            }
        }
        return -1;
    }

    public void disposs() {
        if (link != null) {
            link.clear();
            link = null;
        }
    }
}
