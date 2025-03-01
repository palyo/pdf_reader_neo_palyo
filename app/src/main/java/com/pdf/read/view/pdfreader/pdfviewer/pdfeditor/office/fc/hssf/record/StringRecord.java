package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.cont.ContinuableRecordOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class StringRecord extends ContinuableRecord {

    public final static short sid = 0x0207;

    private boolean _is16bitUnicode;
    private String _text;

    public StringRecord() {
    }

    public StringRecord(RecordInputStream in) {
        int field_1_string_length = in.readUShort();
        _is16bitUnicode = in.readByte() != 0x00;

        if (_is16bitUnicode) {
            _text = in.readUnicodeLEString(field_1_string_length);
        } else {
            _text = in.readCompressedUnicode(field_1_string_length);
        }
    }

    protected void serialize(ContinuableRecordOutput out) {
        out.writeShort(_text.length());
        out.writeStringData(_text);
    }

    public short getSid() {
        return sid;
    }

    public String getString() {
        return _text;
    }

    public void setString(String string) {
        _text = string;
        _is16bitUnicode = StringUtil.hasMultibyte(string);
    }

    public String toString() {

        String buffer = "[STRING]\n" +
                "    .string            = " +
                _text + "\n" +
                "[/STRING]\n";
        return buffer;
    }

    public Object clone() {
        StringRecord rec = new StringRecord();
        rec._is16bitUnicode = _is16bitUnicode;
        rec._text = _text;
        return rec;
    }
}
