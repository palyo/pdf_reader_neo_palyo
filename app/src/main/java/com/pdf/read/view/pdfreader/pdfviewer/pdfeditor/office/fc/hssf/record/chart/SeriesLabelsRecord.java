package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.StandardRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class SeriesLabelsRecord extends StandardRecord {
    public final static short sid = 0x100c;

    private static final BitField showActual = BitFieldFactory.getInstance(0x01);
    private static final BitField showPercent = BitFieldFactory.getInstance(0x02);
    private static final BitField labelAsPercentage = BitFieldFactory.getInstance(0x04);
    private static final BitField smoothedLine = BitFieldFactory.getInstance(0x08);
    private static final BitField showLabel = BitFieldFactory.getInstance(0x10);
    private static final BitField showBubbleSizes = BitFieldFactory.getInstance(0x20);

    private short field_1_formatFlags;

    public SeriesLabelsRecord() {

    }

    public SeriesLabelsRecord(RecordInputStream in) {
        field_1_formatFlags = in.readShort();
    }

    public String toString() {

        String buffer = "[ATTACHEDLABEL]\n" +
                "    .formatFlags          = " +
                "0x" + HexDump.toHex(getFormatFlags()) +
                " (" + getFormatFlags() + " )" +
                System.getProperty("line.separator") +
                "         .showActual               = " + isShowActual() + '\n' +
                "         .showPercent              = " + isShowPercent() + '\n' +
                "         .labelAsPercentage        = " + isLabelAsPercentage() + '\n' +
                "         .smoothedLine             = " + isSmoothedLine() + '\n' +
                "         .showLabel                = " + isShowLabel() + '\n' +
                "         .showBubbleSizes          = " + isShowBubbleSizes() + '\n' +
                "[/ATTACHEDLABEL]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_formatFlags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SeriesLabelsRecord rec = new SeriesLabelsRecord();

        rec.field_1_formatFlags = field_1_formatFlags;
        return rec;
    }

    public short getFormatFlags() {
        return field_1_formatFlags;
    }

    public void setFormatFlags(short field_1_formatFlags) {
        this.field_1_formatFlags = field_1_formatFlags;
    }

    public boolean isShowActual() {
        return showActual.isSet(field_1_formatFlags);
    }

    public void setShowActual(boolean value) {
        field_1_formatFlags = showActual.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isShowPercent() {
        return showPercent.isSet(field_1_formatFlags);
    }

    public void setShowPercent(boolean value) {
        field_1_formatFlags = showPercent.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isLabelAsPercentage() {
        return labelAsPercentage.isSet(field_1_formatFlags);
    }

    public void setLabelAsPercentage(boolean value) {
        field_1_formatFlags = labelAsPercentage.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isSmoothedLine() {
        return smoothedLine.isSet(field_1_formatFlags);
    }

    public void setSmoothedLine(boolean value) {
        field_1_formatFlags = smoothedLine.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isShowLabel() {
        return showLabel.isSet(field_1_formatFlags);
    }

    public void setShowLabel(boolean value) {
        field_1_formatFlags = showLabel.setShortBoolean(field_1_formatFlags, value);
    }

    public boolean isShowBubbleSizes() {
        return showBubbleSizes.isSet(field_1_formatFlags);
    }

    public void setShowBubbleSizes(boolean value) {
        field_1_formatFlags = showBubbleSizes.setShortBoolean(field_1_formatFlags, value);
    }
}
