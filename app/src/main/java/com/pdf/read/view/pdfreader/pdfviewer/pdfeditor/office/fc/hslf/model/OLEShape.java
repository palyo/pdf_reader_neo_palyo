package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.ExEmbed;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.ExObjList;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.Record;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.ObjectData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.SlideShow;

public final class OLEShape extends Picture {
    private ExEmbed _exEmbed;

    public OLEShape(int idx) {
        super(idx);
    }

    public OLEShape(int idx, Shape parent) {
        super(idx, parent);
    }

    OLEShape(EscherContainerRecord escherRecord, Shape parent) {
        super(escherRecord, parent);
    }

    public int getObjectID() {
        return ShapeKit.getEscherProperty(getSpContainer(), EscherProperties.BLIP__PICTUREID);
    }

    public ObjectData getObjectData() {
        SlideShow ppt = getSheet().getSlideShow();
        ObjectData[] ole = ppt.getEmbeddedObjects();

        int ref = getExEmbed().getExOleObjAtom().getObjStgDataRef();

        ObjectData data = null;

        for (int i = 0; i < ole.length; i++) {
            if (ole[i].getExOleObjStg().getPersistId() == ref) {
                data = ole[i];
            }
        }

        if (data == null) {
        }

        return data;
    }

    public ExEmbed getExEmbed() {
        if (_exEmbed == null) {
            SlideShow ppt = getSheet().getSlideShow();

            ExObjList lst = ppt.getDocumentRecord().getExObjList();
            if (lst == null) {
                return null;
            }

            int id = getObjectID();
            Record[] ch = lst.getChildRecords();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] instanceof ExEmbed) {
                    ExEmbed embd = (ExEmbed) ch[i];
                    if (embd.getExOleObjAtom().getObjID() == id)
                        _exEmbed = embd;
                }
            }
        }
        return _exEmbed;
    }

    public String getInstanceName() {
        return getExEmbed().getMenuName();
    }

    public String getFullName() {
        return getExEmbed().getClipboardName();
    }

    public String getProgID() {
        return getExEmbed().getProgId();
    }

    public void dispose() {
        super.dispose();
        if (_exEmbed != null) {
            _exEmbed.dispose();
            _exEmbed = null;
        }
    }
}
