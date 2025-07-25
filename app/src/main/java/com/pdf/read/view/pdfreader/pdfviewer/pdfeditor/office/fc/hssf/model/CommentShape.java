package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.model;

import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherOptRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherSimpleProperty;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.CommonObjectDataSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.NoteRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.NoteStructureSubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.ObjRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.SubRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFComment;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel.HSSFShape;

public final class CommentShape extends TextboxShape {

    private final NoteRecord _note;

    public CommentShape(HSSFComment hssfShape, int shapeId) {
        super(hssfShape, shapeId);

        _note = createNoteRecord(hssfShape, shapeId);

        ObjRecord obj = getObjRecord();
        List<SubRecord> records = obj.getSubRecords();
        int cmoIdx = 0;
        for (int i = 0; i < records.size(); i++) {
            Object r = records.get(i);

            if (r instanceof CommonObjectDataSubRecord) {

                CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord) r;
                cmo.setAutofill(false);
                cmoIdx = i;
            }
        }

        NoteStructureSubRecord u = new NoteStructureSubRecord();
        obj.addSubRecord(cmoIdx + 1, u);
    }

    private NoteRecord createNoteRecord(HSSFComment shape, int shapeId) {
        NoteRecord note = new NoteRecord();
        note.setColumn(shape.getColumn());
        note.setRow(shape.getRow());
        note.setFlags(shape.isVisible() ? NoteRecord.NOTE_VISIBLE : NoteRecord.NOTE_HIDDEN);
        note.setShapeId(shapeId);
        note.setAuthor(shape.getAuthor() == null ? "" : shape.getAuthor());
        return note;
    }

    protected int addStandardOptions(HSSFShape shape, EscherOptRecord opt) {
        super.addStandardOptions(shape, opt);

        List<EscherProperty> props = opt.getEscherProperties();
        for (Iterator<EscherProperty> iterator = props.iterator(); iterator.hasNext(); ) {
            EscherProperty prop = iterator.next();
            switch (prop.getId()) {
                case EscherProperties.TEXT__TEXTLEFT:
                case EscherProperties.TEXT__TEXTRIGHT:
                case EscherProperties.TEXT__TEXTTOP:
                case EscherProperties.TEXT__TEXTBOTTOM:
                case EscherProperties.GROUPSHAPE__PRINT:
                case EscherProperties.FILL__FILLBACKCOLOR:
                case EscherProperties.LINESTYLE__COLOR:
                    iterator.remove();
                    break;
            }
        }

        HSSFComment comment = (HSSFComment) shape;
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GROUPSHAPE__PRINT, comment.isVisible() ? 0x000A0000 : 0x000A0002));
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.SHADOWSTYLE__SHADOWOBSURED, 0x00030003));
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.SHADOWSTYLE__COLOR, 0x00000000));
        opt.sortProperties();
        return opt.getEscherProperties().size();
    }

    public NoteRecord getNoteRecord() {
        return _note;
    }

    @Override
    int getCmoObjectId(int shapeId) {
        return shapeId;
    }

}
