package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.AutoShapeConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherBoolProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherOptRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherRGBProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSimpleProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSpRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.ObjRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFAnchor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFComment;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFPolygon;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFSimpleShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFTextbox;

public abstract class AbstractShape {
    protected AbstractShape() {
    }

    public static AbstractShape createShape(HSSFShape hssfShape, int shapeId) {
        AbstractShape shape;
        if (hssfShape instanceof HSSFComment) {
            shape = new CommentShape((HSSFComment) hssfShape, shapeId);
        } else if (hssfShape instanceof HSSFTextbox) {
            shape = new TextboxShape((HSSFTextbox) hssfShape, shapeId);
        } else if (hssfShape instanceof HSSFPolygon) {
            shape = new PolygonShape((HSSFPolygon) hssfShape, shapeId);
        } else if (hssfShape instanceof HSSFSimpleShape) {
            HSSFSimpleShape simpleShape = (HSSFSimpleShape) hssfShape;
            switch (simpleShape.getShapeType()) {
                case HSSFSimpleShape.OBJECT_TYPE_PICTURE:
                    shape = new PictureShape(simpleShape, shapeId);
                    break;
                case HSSFSimpleShape.OBJECT_TYPE_LINE:
                    shape = new LineShape(simpleShape, shapeId);
                    break;
                case HSSFSimpleShape.OBJECT_TYPE_OVAL:
                case HSSFSimpleShape.OBJECT_TYPE_RECTANGLE:
                    shape = new SimpleFilledShape(simpleShape, shapeId);
                    break;
                case HSSFSimpleShape.OBJECT_TYPE_COMBO_BOX:
                    shape = new ComboboxShape(simpleShape, shapeId);
                    break;
                default:
                    throw new IllegalArgumentException("Do not know how to handle this type of shape");
            }
        } else {
            throw new IllegalArgumentException("Unknown shape type");
        }
        EscherSpRecord sp = shape.getSpContainer().getChildById(EscherSpRecord.RECORD_ID);
        if (hssfShape.getParent() != null)
            sp.setFlags(sp.getFlags() | EscherSpRecord.FLAG_CHILD);
        return shape;
    }

    public abstract EscherContainerRecord getSpContainer();

    public abstract ObjRecord getObjRecord();

    protected EscherRecord createAnchor(HSSFAnchor userAnchor) {
        return ConvertAnchor.createAnchor(userAnchor);
    }

    protected int addStandardOptions(HSSFShape shape, EscherOptRecord opt) {
        opt.addEscherProperty(new EscherBoolProperty(EscherProperties.TEXT__SIZE_TEXT_TO_FIT_SHAPE, 0x080000));

        if (shape.isNoFill()) {

            opt.addEscherProperty(new EscherBoolProperty(EscherProperties.FILL__NOFILLHITTEST, 0x00110000));
        } else {
            opt.addEscherProperty(new EscherBoolProperty(EscherProperties.FILL__NOFILLHITTEST, 0x00010000));
        }
        opt.addEscherProperty(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, shape.getFillColor()));
        opt.addEscherProperty(new EscherBoolProperty(EscherProperties.GROUPSHAPE__PRINT, 0x080000));
        opt.addEscherProperty(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, shape.getLineStyleColor()));
        int options = 5;
        if (shape.getLineWidth() != AutoShapeConstant.LINEWIDTH_DEFAULT) {
            opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEWIDTH, shape.getLineWidth()));
            options++;
        }
        if (shape.getLineStyle() != AutoShapeConstant.LINESTYLE_SOLID) {
            opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEDASHING, shape.getLineStyle()));
            opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEENDCAPSTYLE, 0));
            if (shape.getLineStyle() == AutoShapeConstant.LINESTYLE_NONE)
                opt.addEscherProperty(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 0x00080000));
            else
                opt.addEscherProperty(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 0x00080008));
            options += 3;
        }
        opt.sortProperties();
        return options;
    }

    int getCmoObjectId(int shapeId) {
        return shapeId - 1024;
    }
}
