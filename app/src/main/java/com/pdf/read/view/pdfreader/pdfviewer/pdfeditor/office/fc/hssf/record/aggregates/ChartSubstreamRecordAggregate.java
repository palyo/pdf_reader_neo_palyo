package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.model.RecordStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.BOFRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.EOFRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.HeaderFooterRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.Record;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordBase;

public final class ChartSubstreamRecordAggregate extends RecordAggregate {

    private final BOFRecord _bofRec;
    private final List<RecordBase> _recs;
    private PageSettingsBlock _psBlock;

    public ChartSubstreamRecordAggregate(RecordStream rs) {
        _bofRec = (BOFRecord) rs.getNext();
        List<RecordBase> temp = new ArrayList<RecordBase>();
        while (rs.peekNextClass() != EOFRecord.class) {
            if (PageSettingsBlock.isComponentRecord(rs.peekNextSid())) {
                if (_psBlock != null) {
                    if (rs.peekNextSid() == HeaderFooterRecord.sid) {

                        _psBlock.addLateHeaderFooter((HeaderFooterRecord) rs.getNext());
                        continue;
                    }
                    throw new IllegalStateException(
                            "Found more than one PageSettingsBlock in chart sub-stream");
                }
                _psBlock = new PageSettingsBlock(rs);
                temp.add(_psBlock);
                continue;
            }
            temp.add(rs.getNext());
        }
        _recs = temp;
        Record eof = rs.getNext();
        if (!(eof instanceof EOFRecord)) {
            throw new IllegalStateException("Bad chart EOF");
        }
    }

    public void visitContainedRecords(RecordVisitor rv) {
        if (_recs.isEmpty()) {
            return;
        }
        rv.visitRecord(_bofRec);
        for (int i = 0; i < _recs.size(); i++) {
            RecordBase rb = _recs.get(i);
            if (rb instanceof RecordAggregate) {
                ((RecordAggregate) rb).visitContainedRecords(rv);
            } else {
                rv.visitRecord((Record) rb);
            }
        }
        rv.visitRecord(EOFRecord.instance);
    }
}
