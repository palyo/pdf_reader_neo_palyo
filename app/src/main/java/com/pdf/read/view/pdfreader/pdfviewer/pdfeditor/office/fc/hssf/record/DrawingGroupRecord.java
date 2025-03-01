package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.NullEscherSerializationListener;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.ArrayUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class DrawingGroupRecord extends AbstractEscherHolderRecord {
    public static final short sid = 0xEB;

    static final int MAX_RECORD_SIZE = 8228;
    private static final int MAX_DATA_SIZE = MAX_RECORD_SIZE - 4;

    public DrawingGroupRecord() {
    }

    public DrawingGroupRecord(RecordInputStream in) {
        super(in);
    }

    static int grossSizeFromDataSize(int dataSize) {
        return dataSize + ((dataSize - 1) / MAX_DATA_SIZE + 1) * 4;
    }

    protected String getRecordName() {
        return "MSODRAWINGGROUP";
    }

    public short getSid() {
        return sid;
    }

    public int serialize(int offset, byte[] data) {
        byte[] rawData = getRawData();
        if (getEscherRecords().size() == 0 && rawData != null) {
            return writeData(offset, data, rawData);
        }
        byte[] buffer = new byte[getRawDataSize()];
        int pos = 0;
        for (Iterator iterator = getEscherRecords().iterator(); iterator.hasNext(); ) {
            EscherRecord r = (EscherRecord) iterator.next();
            pos += r.serialize(pos, buffer, new NullEscherSerializationListener());
        }

        return writeData(offset, data, buffer);
    }

    public void processChildRecords() {
        convertRawBytesToEscherRecords();
    }

    public int getRecordSize() {

        return grossSizeFromDataSize(getRawDataSize());
    }

    private int getRawDataSize() {
        List escherRecords = getEscherRecords();
        byte[] rawData = getRawData();
        if (escherRecords.size() == 0 && rawData != null) {
            return rawData.length;
        }
        int size = 0;
        for (Iterator iterator = escherRecords.iterator(); iterator.hasNext(); ) {
            EscherRecord r = (EscherRecord) iterator.next();
            size += r.getRecordSize();
        }
        return size;
    }

    private int writeData(int offset, byte[] data, byte[] rawData) {
        int writtenActualData = 0;
        int writtenRawData = 0;
        while (writtenRawData < rawData.length) {
            int segmentLength = Math.min(rawData.length - writtenRawData, MAX_DATA_SIZE);
            if (writtenRawData / MAX_DATA_SIZE >= 2)
                writeContinueHeader(data, offset, segmentLength);
            else
                writeHeader(data, offset, segmentLength);
            writtenActualData += 4;
            offset += 4;
            ArrayUtil.arraycopy(rawData, writtenRawData, data, offset, segmentLength);
            offset += segmentLength;
            writtenRawData += segmentLength;
            writtenActualData += segmentLength;
        }
        return writtenActualData;
    }

    private void writeHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, offset, getSid());
        LittleEndian.putShort(data, 2 + offset, (short) sizeExcludingHeader);
    }

    private void writeContinueHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, offset, ContinueRecord.sid);
        LittleEndian.putShort(data, 2 + offset, (short) sizeExcludingHeader);
    }
}
