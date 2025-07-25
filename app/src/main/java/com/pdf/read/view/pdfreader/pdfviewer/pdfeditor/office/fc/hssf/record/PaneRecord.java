package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.HexDump;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class PaneRecord extends StandardRecord {
    public final static short sid = 0x41;
    public final static short ACTIVE_PANE_LOWER_RIGHT = 0;
    public final static short ACTIVE_PANE_UPPER_RIGHT = 1;
    public final static short ACTIVE_PANE_LOWER_LEFT = 2;

    public final static short ACTIVE_PANE_UPER_LEFT = 3;
    public final static short ACTIVE_PANE_UPPER_LEFT = 3;
    private short field_1_x;
    private short field_2_y;
    private short field_3_topRow;

    private short field_4_leftColumn;
    private short field_5_activePane;

    public PaneRecord() {

    }

    public PaneRecord(RecordInputStream in) {
        field_1_x = in.readShort();
        field_2_y = in.readShort();
        field_3_topRow = in.readShort();
        field_4_leftColumn = in.readShort();
        field_5_activePane = in.readShort();
    }

    public String toString() {

        String buffer = "[PANE]\n" +
                "    .x                    = " +
                "0x" + HexDump.toHex(getX()) +
                " (" + getX() + " )" +
                System.getProperty("line.separator") +
                "    .y                    = " +
                "0x" + HexDump.toHex(getY()) +
                " (" + getY() + " )" +
                System.getProperty("line.separator") +
                "    .topRow               = " +
                "0x" + HexDump.toHex(getTopRow()) +
                " (" + getTopRow() + " )" +
                System.getProperty("line.separator") +
                "    .leftColumn           = " +
                "0x" + HexDump.toHex(getLeftColumn()) +
                " (" + getLeftColumn() + " )" +
                System.getProperty("line.separator") +
                "    .activePane           = " +
                "0x" + HexDump.toHex(getActivePane()) +
                " (" + getActivePane() + " )" +
                System.getProperty("line.separator") +
                "[/PANE]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_x);
        out.writeShort(field_2_y);
        out.writeShort(field_3_topRow);
        out.writeShort(field_4_leftColumn);
        out.writeShort(field_5_activePane);
    }

    protected int getDataSize() {
        return 2 + 2 + 2 + 2 + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PaneRecord rec = new PaneRecord();

        rec.field_1_x = field_1_x;
        rec.field_2_y = field_2_y;
        rec.field_3_topRow = field_3_topRow;
        rec.field_4_leftColumn = field_4_leftColumn;
        rec.field_5_activePane = field_5_activePane;
        return rec;
    }

    public short getX() {
        return field_1_x;
    }

    public void setX(short field_1_x) {
        this.field_1_x = field_1_x;
    }

    public short getY() {
        return field_2_y;
    }

    public void setY(short field_2_y) {
        this.field_2_y = field_2_y;
    }

    public short getTopRow() {
        return field_3_topRow;
    }

    public void setTopRow(short field_3_topRow) {
        this.field_3_topRow = field_3_topRow;
    }

    public short getLeftColumn() {
        return field_4_leftColumn;
    }

    public void setLeftColumn(short field_4_leftColumn) {
        this.field_4_leftColumn = field_4_leftColumn;
    }

    public short getActivePane() {
        return field_5_activePane;
    }

    public void setActivePane(short field_5_activePane) {
        this.field_5_activePane = field_5_activePane;
    }
}
