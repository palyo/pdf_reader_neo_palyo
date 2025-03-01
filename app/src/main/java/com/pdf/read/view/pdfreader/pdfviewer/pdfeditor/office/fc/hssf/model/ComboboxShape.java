package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.ShapeTypes;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherBoolProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherClientDataRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherOptRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSimpleProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSpRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.CommonObjectDataSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.EndSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.FtCblsSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.LbsDataSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.ObjRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFClientAnchor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFSimpleShape;

public class ComboboxShape
        extends AbstractShape {
    private final EscherContainerRecord spContainer;
    private final ObjRecord objRecord;

    ComboboxShape(HSSFSimpleShape hssfShape, int shapeId) {
        spContainer = createSpContainer(hssfShape, shapeId);
        objRecord = createObjRecord(hssfShape, shapeId);
    }

    private ObjRecord createObjRecord(HSSFSimpleShape shape, int shapeId) {
        ObjRecord obj = new ObjRecord();
        CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
        c.setObjectType(HSSFSimpleShape.OBJECT_TYPE_COMBO_BOX);
        c.setObjectId(getCmoObjectId(shapeId));
        c.setLocked(true);
        c.setPrintable(false);
        c.setAutofill(true);
        c.setAutoline(false);

        FtCblsSubRecord f = new FtCblsSubRecord();

        LbsDataSubRecord l = LbsDataSubRecord.newAutoFilterInstance();

        EndSubRecord e = new EndSubRecord();

        obj.addSubRecord(c);
        obj.addSubRecord(f);
        obj.addSubRecord(l);
        obj.addSubRecord(e);

        return obj;
    }

    private EscherContainerRecord createSpContainer(HSSFSimpleShape shape, int shapeId) {
        EscherContainerRecord spContainer = new EscherContainerRecord();
        EscherSpRecord sp = new EscherSpRecord();
        EscherOptRecord opt = new EscherOptRecord();
        EscherClientDataRecord clientData = new EscherClientDataRecord();

        spContainer.setRecordId(EscherContainerRecord.SP_CONTAINER);
        spContainer.setOptions((short) 0x000F);
        sp.setRecordId(EscherSpRecord.RECORD_ID);
        sp.setOptions((short) ((ShapeTypes.HostControl << 4) | 0x2));

        sp.setShapeId(shapeId);
        sp.setFlags(EscherSpRecord.FLAG_HAVEANCHOR | EscherSpRecord.FLAG_HASSHAPETYPE);
        opt.setRecordId(EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherBoolProperty(EscherProperties.PROTECTION__LOCKAGAINSTGROUPING, 17039620));
        opt.addEscherProperty(new EscherBoolProperty(EscherProperties.TEXT__SIZE_TEXT_TO_FIT_SHAPE, 0x00080008));
        opt.addEscherProperty(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 0x00080000));
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GROUPSHAPE__PRINT, 0x00020000));

        HSSFClientAnchor userAnchor = (HSSFClientAnchor) shape.getAnchor();
        userAnchor.setAnchorType(1);
        EscherRecord anchor = createAnchor(userAnchor);
        clientData.setRecordId(EscherClientDataRecord.RECORD_ID);
        clientData.setOptions((short) 0x0000);

        spContainer.addChildRecord(sp);
        spContainer.addChildRecord(opt);
        spContainer.addChildRecord(anchor);
        spContainer.addChildRecord(clientData);

        return spContainer;
    }

    public EscherContainerRecord getSpContainer() {
        return spContainer;
    }

    public ObjRecord getObjRecord() {
        return objRecord;
    }

}
