package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class WindowOneRecord extends StandardRecord {
    public final static short sid = 0x3d;
    static final private BitField hidden =
            BitFieldFactory.getInstance(0x01);
    static final private BitField iconic =
            BitFieldFactory.getInstance(0x02);
    static final private BitField reserved = BitFieldFactory.getInstance(0x04);
    static final private BitField hscroll =
            BitFieldFactory.getInstance(0x08);
    static final private BitField vscroll =
            BitFieldFactory.getInstance(0x10);
    static final private BitField tabs =
            BitFieldFactory.getInstance(0x20);

    private short field_1_h_hold;
    private short field_2_v_hold;
    private short field_3_width;
    private short field_4_height;
    private short field_5_options;

    private int field_6_active_sheet;
    private int field_7_first_visible_tab;
    private short field_8_num_selected_tabs;
    private short field_9_tab_width_ratio;

    public WindowOneRecord() {
    }

    public WindowOneRecord(RecordInputStream in) {
        field_1_h_hold = in.readShort();
        field_2_v_hold = in.readShort();
        field_3_width = in.readShort();
        field_4_height = in.readShort();
        field_5_options = in.readShort();
        field_6_active_sheet = in.readShort();
        field_7_first_visible_tab = in.readShort();
        field_8_num_selected_tabs = in.readShort();
        field_9_tab_width_ratio = in.readShort();
    }

    public void setDisplayHorizonalScrollbar(boolean scroll) {
        field_5_options = hscroll.setShortBoolean(field_5_options, scroll);
    }

    public short getHorizontalHold() {
        return field_1_h_hold;
    }

    public void setHorizontalHold(short h) {
        field_1_h_hold = h;
    }

    public short getVerticalHold() {
        return field_2_v_hold;
    }

    public void setVerticalHold(short v) {
        field_2_v_hold = v;
    }

    public short getWidth() {
        return field_3_width;
    }

    public void setWidth(short w) {
        field_3_width = w;
    }

    public short getHeight() {
        return field_4_height;
    }

    public void setHeight(short h) {
        field_4_height = h;
    }

    public short getOptions() {
        return field_5_options;
    }

    public void setOptions(short o) {
        field_5_options = o;
    }

    public boolean getHidden() {
        return hidden.isSet(field_5_options);
    }

    public void setHidden(boolean ishidden) {
        field_5_options = hidden.setShortBoolean(field_5_options, ishidden);
    }

    public boolean getIconic() {
        return iconic.isSet(field_5_options);
    }

    public void setIconic(boolean isiconic) {
        field_5_options = iconic.setShortBoolean(field_5_options, isiconic);
    }

    public boolean getDisplayHorizontalScrollbar() {
        return hscroll.isSet(field_5_options);
    }

    public boolean getDisplayVerticalScrollbar() {
        return vscroll.isSet(field_5_options);
    }

    public void setDisplayVerticalScrollbar(boolean scroll) {
        field_5_options = vscroll.setShortBoolean(field_5_options, scroll);
    }

    public boolean getDisplayTabs() {
        return tabs.isSet(field_5_options);
    }

    public void setDisplayTabs(boolean disptabs) {
        field_5_options = tabs.setShortBoolean(field_5_options, disptabs);
    }

    public int getActiveSheetIndex() {
        return field_6_active_sheet;
    }

    public void setActiveSheetIndex(int index) {
        field_6_active_sheet = index;
    }

    public short getSelectedTab() {
        return (short) getActiveSheetIndex();
    }

    public void setSelectedTab(short s) {
        setActiveSheetIndex(s);
    }

    public int getFirstVisibleTab() {
        return field_7_first_visible_tab;
    }

    public void setFirstVisibleTab(int t) {
        field_7_first_visible_tab = t;
    }

    public short getDisplayedTab() {
        return (short) getFirstVisibleTab();
    }

    public void setDisplayedTab(short t) {
        setFirstVisibleTab(t);
    }

    public short getNumSelectedTabs() {
        return field_8_num_selected_tabs;
    }

    public void setNumSelectedTabs(short n) {
        field_8_num_selected_tabs = n;
    }

    public short getTabWidthRatio() {
        return field_9_tab_width_ratio;
    }

    public void setTabWidthRatio(short r) {
        field_9_tab_width_ratio = r;
    }

    public String toString() {

        String buffer = "[WINDOW1]\n" +
                "    .h_hold          = " +
                Integer.toHexString(getHorizontalHold()) + "\n" +
                "    .v_hold          = " +
                Integer.toHexString(getVerticalHold()) + "\n" +
                "    .width           = " +
                Integer.toHexString(getWidth()) + "\n" +
                "    .height          = " +
                Integer.toHexString(getHeight()) + "\n" +
                "    .options         = " +
                Integer.toHexString(getOptions()) + "\n" +
                "        .hidden      = " + getHidden() +
                "\n" +
                "        .iconic      = " + getIconic() +
                "\n" +
                "        .hscroll     = " +
                getDisplayHorizontalScrollbar() + "\n" +
                "        .vscroll     = " +
                getDisplayVerticalScrollbar() + "\n" +
                "        .tabs        = " + getDisplayTabs() +
                "\n" +
                "    .activeSheet     = " +
                Integer.toHexString(getActiveSheetIndex()) + "\n" +
                "    .firstVisibleTab    = " +
                Integer.toHexString(getFirstVisibleTab()) + "\n" +
                "    .numselectedtabs = " +
                Integer.toHexString(getNumSelectedTabs()) + "\n" +
                "    .tabwidthratio   = " +
                Integer.toHexString(getTabWidthRatio()) + "\n" +
                "[/WINDOW1]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getHorizontalHold());
        out.writeShort(getVerticalHold());
        out.writeShort(getWidth());
        out.writeShort(getHeight());
        out.writeShort(getOptions());
        out.writeShort(getActiveSheetIndex());
        out.writeShort(getFirstVisibleTab());
        out.writeShort(getNumSelectedTabs());
        out.writeShort(getTabWidthRatio());
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return sid;
    }
}
