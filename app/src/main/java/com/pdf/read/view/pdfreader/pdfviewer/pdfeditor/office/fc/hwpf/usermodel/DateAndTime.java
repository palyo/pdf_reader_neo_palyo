package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import java.util.Calendar;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class DateAndTime implements Cloneable {
    public static final int SIZE = 4;
    private static final BitField _minutes = BitFieldFactory.getInstance(0x3f);
    private static final BitField _hours = BitFieldFactory.getInstance(0x7c0);
    private static final BitField _dom = BitFieldFactory.getInstance(0xf800);
    private static final BitField _months = BitFieldFactory.getInstance(0xf);
    private static final BitField _years = BitFieldFactory.getInstance(0x1ff0);
    private static final BitField _weekday = BitFieldFactory.getInstance(0xe000);
    private short _info;
    private short _info2;

    public DateAndTime() {
    }

    public DateAndTime(byte[] buf, int offset) {
        _info = LittleEndian.getShort(buf, offset);
        _info2 = LittleEndian.getShort(buf, offset + LittleEndian.SHORT_SIZE);
    }

    public Calendar getDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(_years.getValue(_info2) + 1900, _months.getValue(_info2) - 1, _dom.getValue(_info),
                _hours.getValue(_info), _minutes.getValue(_info), 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public void serialize(byte[] buf, int offset) {
        LittleEndian.putShort(buf, offset, _info);
        LittleEndian.putShort(buf, offset + LittleEndian.SHORT_SIZE, _info2);
    }

    public boolean equals(Object o) {
        DateAndTime dttm = (DateAndTime) o;
        return _info == dttm._info && _info2 == dttm._info2;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isEmpty() {
        return _info == 0 && _info2 == 0;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "[DTTM] EMPTY";

        return "[DTTM] " + getDate();
    }
}
