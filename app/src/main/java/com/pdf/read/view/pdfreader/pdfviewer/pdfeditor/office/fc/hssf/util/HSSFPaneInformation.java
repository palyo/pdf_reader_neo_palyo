package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.util;

public class HSSFPaneInformation {

    public static final byte PANE_LOWER_RIGHT = (byte) 0;

    public static final byte PANE_UPPER_RIGHT = (byte) 1;

    public static final byte PANE_LOWER_LEFT = (byte) 2;

    public static final byte PANE_UPPER_LEFT = (byte) 3;

    private final short x;
    private final short y;
    private final short topRow;
    private final short leftColumn;
    private final byte activePane;
    private boolean frozen = false;

    public HSSFPaneInformation(short x, short y, short top, short left, byte active, boolean frozen) {
        this.x = x;
        this.y = y;
        this.topRow = top;
        this.leftColumn = left;
        this.activePane = active;
        this.frozen = frozen;
    }

    public short getVerticalSplitPosition() {
        return x;
    }

    public short getHorizontalSplitPosition() {
        return y;
    }

    public short getHorizontalSplitTopRow() {
        return topRow;
    }

    public short getVerticalSplitLeftColumn() {
        return leftColumn;
    }

    public byte getActivePane() {
        return activePane;
    }

    public boolean isFreezePane() {
        return frozen;
    }
}
