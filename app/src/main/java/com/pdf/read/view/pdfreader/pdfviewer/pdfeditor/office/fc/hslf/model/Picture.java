package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherBSERecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherComplexProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherOptRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSimpleProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSpRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.PictureData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.usermodel.SlideShow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public class Picture extends SimpleShape {
    public static final int EMF = 2;

    public static final int WMF = 3;

    public static final int PICT = 4;

    public static final int JPEG = 5;

    public static final int PNG = 6;

    public static final byte DIB = 7;

    public Picture(int idx) {
        this(idx, null);
    }

    public Picture(int idx, Shape parent) {
        super(null, parent);
        _escherContainer = createSpContainer(idx, parent instanceof ShapeGroup);
    }

    protected Picture(EscherContainerRecord escherRecord, Shape parent) {
        super(escherRecord, parent);
    }

    public int getPictureIndex() {
        EscherOptRecord opt = (EscherOptRecord) ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty) ShapeKit.getEscherProperty(opt,
                EscherProperties.BLIP__BLIPTODISPLAY);
        return prop == null ? 0 : prop.getPropertyValue();
    }

    protected EscherContainerRecord createSpContainer(int idx, boolean isChild) {
        _escherContainer = super.createSpContainer(isChild);
        _escherContainer.setOptions((short) 15);

        EscherSpRecord spRecord = _escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        spRecord.setOptions((short) ((ShapeTypes.PictureFrame << 4) | 0x2));

        EscherOptRecord opt = (EscherOptRecord) ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);
        setEscherProperty(opt, EscherProperties.PROTECTION__LOCKAGAINSTGROUPING, 0x800080);

        setEscherProperty(opt, (short) (EscherProperties.BLIP__BLIPTODISPLAY + 0x4000), idx);

        return _escherContainer;
    }

    public void setDefaultSize() {

    }

    public PictureData getPictureData() {
        SlideShow ppt = getSheet().getSlideShow();
        PictureData[] pict = ppt.getPictureData();

        EscherBSERecord bse = getEscherBSERecord();
        if (bse == null) {

        } else {
            for (int i = 0; i < pict.length; i++) {
                if (pict[i].getOffset() == bse.getOffset()) {
                    return pict[i];
                }
            }

        }
        return null;
    }

    public EscherOptRecord getEscherOptRecord() {
        return (EscherOptRecord) ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);
    }

    protected EscherBSERecord getEscherBSERecord() {
        SlideShow ppt = getSheet().getSlideShow();
        Document doc = ppt.getDocumentRecord();
        EscherContainerRecord dggContainer = doc.getPPDrawingGroup().getDggContainer();
        EscherContainerRecord bstore = (EscherContainerRecord) ShapeKit.getEscherChild(dggContainer,
                EscherContainerRecord.BSTORE_CONTAINER);
        if (bstore == null) {

            return null;
        }
        List lst = bstore.getChildRecords();
        int idx = getPictureIndex();
        if (idx == 0) {

            return null;
        }
        return (EscherBSERecord) lst.get(idx - 1);
    }

    public String getPictureName() {
        EscherOptRecord opt = (EscherOptRecord) ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);
        EscherComplexProperty prop = (EscherComplexProperty) ShapeKit.getEscherProperty(opt,
                EscherProperties.BLIP__BLIPFILENAME);
        String name = null;
        if (prop != null) {
            name = new String(prop.getComplexData(), StandardCharsets.UTF_16LE);
            int idx = name.indexOf('\u0000');
            return idx == -1 ? name : name.substring(0, idx);
        }
        return name;
    }

    public void setPictureName(String name) {
        EscherOptRecord opt = (EscherOptRecord) ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);
        byte[] data = (name + '\u0000').getBytes(StandardCharsets.UTF_16LE);
        EscherComplexProperty prop = new EscherComplexProperty(
                EscherProperties.BLIP__BLIPFILENAME, false, data);
        opt.addEscherProperty(prop);
    }

    protected void afterInsert(Sheet sh) {
        super.afterInsert(sh);

        EscherBSERecord bse = getEscherBSERecord();
        bse.setRef(bse.getRef() + 1);

        Rectangle anchor = getAnchor();
        if (anchor.equals(new Rectangle())) {
            setDefaultSize();
        }
    }
}
