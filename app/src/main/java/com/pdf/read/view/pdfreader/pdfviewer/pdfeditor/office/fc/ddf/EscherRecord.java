package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public abstract class EscherRecord {
    private short _options;
    private short _recordId;

    public EscherRecord() {

    }

    protected int fillFields(byte[] data, EscherRecordFactory f) {
        return fillFields(data, 0, f);
    }

    public abstract int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory);

    public abstract void dispose();

    public int readHeader(byte[] data, int offset) {
        EscherRecordHeader header = EscherRecordHeader.readHeader(data, offset);
        _options = header.getOptions();
        _recordId = header.getRecordId();
        return header.getRemainingBytes();
    }

    public boolean isContainerRecord() {
        return (_options & (short) 0x000f) == (short) 0x000f;
    }

    public short getOptions() {
        return _options;
    }

    public void setOptions(short options) {
        _options = options;
    }

    public byte[] serialize() {
        byte[] retval = new byte[getRecordSize()];

        serialize(0, retval);
        return retval;
    }

    public int serialize(int offset, byte[] data) {
        return serialize(offset, data, new NullEscherSerializationListener());
    }

    public abstract int serialize(int offset, byte[] data, EscherSerializationListener listener);

    abstract public int getRecordSize();

    public short getRecordId() {
        return _recordId;
    }

    public void setRecordId(short recordId) {
        _recordId = recordId;
    }

    public List<EscherRecord> getChildRecords() {
        return Collections.emptyList();
    }

    public void setChildRecords(List<EscherRecord> childRecords) {
        throw new UnsupportedOperationException("This record does not support child records.");
    }

    public Object clone() {
        throw new RuntimeException("The class " + getClass().getName() + " needs to define a clone method");
    }

    public EscherRecord getChild(int index) {
        return getChildRecords().get(index);
    }

    public void display(PrintWriter w, int indent) {
        for (int i = 0; i < indent * 4; i++) w.print(' ');
        w.println(getRecordName());
    }

    public abstract String getRecordName();

    public short getInstance() {
        return (short) (_options >> 4);
    }

    static class EscherRecordHeader {
        private short options;
        private short recordId;
        private int remainingBytes;

        private EscherRecordHeader() {

        }

        public static EscherRecordHeader readHeader(byte[] data, int offset) {
            EscherRecordHeader header = new EscherRecordHeader();
            header.options = LittleEndian.getShort(data, offset);
            header.recordId = LittleEndian.getShort(data, offset + 2);
            header.remainingBytes = LittleEndian.getInt(data, offset + 4);
            return header;
        }

        public short getOptions() {
            return options;
        }

        public short getRecordId() {
            return recordId;
        }

        public int getRemainingBytes() {
            return remainingBytes;
        }

        public String toString() {
            return "EscherRecordHeader{" +
                    "options=" + options +
                    ", recordId=" + recordId +
                    ", remainingBytes=" + remainingBytes +
                    "}";
        }
    }
}
