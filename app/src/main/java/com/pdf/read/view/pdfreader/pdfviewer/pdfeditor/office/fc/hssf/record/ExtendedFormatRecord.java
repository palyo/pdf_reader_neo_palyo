package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class ExtendedFormatRecord
        extends StandardRecord {
    public final static short sid = 0xE0;

    public final static short NULL = (short) 0xfff0;

    public final static short XF_STYLE = 1;
    public final static short XF_CELL = 0;

    public final static short NONE = 0x0;
    public final static short THIN = 0x1;
    public final static short MEDIUM = 0x2;
    public final static short DASHED = 0x3;
    public final static short DOTTED = 0x4;
    public final static short THICK = 0x5;
    public final static short DOUBLE = 0x6;
    public final static short HAIR = 0x7;
    public final static short MEDIUM_DASHED = 0x8;
    public final static short DASH_DOT = 0x9;
    public final static short MEDIUM_DASH_DOT = 0xA;
    public final static short DASH_DOT_DOT = 0xB;
    public final static short MEDIUM_DASH_DOT_DOT = 0xC;
    public final static short SLANTED_DASH_DOT = 0xD;

    public final static short GENERAL = 0x0;
    public final static short LEFT = 0x1;
    public final static short CENTER = 0x2;
    public final static short RIGHT = 0x3;
    public final static short FILL = 0x4;
    public final static short JUSTIFY = 0x5;
    public final static short CENTER_SELECTION = 0x6;

    public final static short VERTICAL_TOP = 0x0;
    public final static short VERTICAL_CENTER = 0x1;
    public final static short VERTICAL_BOTTOM = 0x2;
    public final static short VERTICAL_JUSTIFY = 0x3;

    public final static short NO_FILL = 0;
    public final static short SOLID_FILL = 1;
    public final static short FINE_DOTS = 2;
    public final static short ALT_BARS = 3;
    public final static short SPARSE_DOTS = 4;
    public final static short THICK_HORZ_BANDS = 5;
    public final static short THICK_VERT_BANDS = 6;
    public final static short THICK_BACKWARD_DIAG = 7;
    public final static short THICK_FORWARD_DIAG = 8;
    public final static short BIG_SPOTS = 9;
    public final static short BRICKS = 10;
    public final static short THIN_HORZ_BANDS = 11;
    public final static short THIN_VERT_BANDS = 12;
    public final static short THIN_BACKWARD_DIAG = 13;
    public final static short THIN_FORWARD_DIAG = 14;
    public final static short SQUARES = 15;
    public final static short DIAMONDS = 16;

    static final private BitField _locked = BitFieldFactory.getInstance(0x0001);
    static final private BitField _hidden = BitFieldFactory.getInstance(0x0002);
    static final private BitField _xf_type = BitFieldFactory.getInstance(0x0004);
    static final private BitField _123_prefix = BitFieldFactory.getInstance(0x0008);
    static final private BitField _parent_index = BitFieldFactory.getInstance(0xFFF0);

    static final private BitField _alignment = BitFieldFactory.getInstance(0x0007);
    static final private BitField _wrap_text = BitFieldFactory.getInstance(0x0008);
    static final private BitField _vertical_alignment = BitFieldFactory.getInstance(0x0070);
    static final private BitField _justify_last = BitFieldFactory.getInstance(0x0080);
    static final private BitField _rotation = BitFieldFactory.getInstance(0xFF00);

    static final private BitField _indent =
            BitFieldFactory.getInstance(0x000F);
    static final private BitField _shrink_to_fit =
            BitFieldFactory.getInstance(0x0010);
    static final private BitField _merge_cells =
            BitFieldFactory.getInstance(0x0020);
    static final private BitField _reading_order =
            BitFieldFactory.getInstance(0x00C0);

    static final private BitField _indent_not_parent_format =
            BitFieldFactory.getInstance(0x0400);
    static final private BitField _indent_not_parent_font =
            BitFieldFactory.getInstance(0x0800);
    static final private BitField _indent_not_parent_alignment =
            BitFieldFactory.getInstance(0x1000);
    static final private BitField _indent_not_parent_border =
            BitFieldFactory.getInstance(0x2000);
    static final private BitField _indent_not_parent_pattern =
            BitFieldFactory.getInstance(0x4000);
    static final private BitField _indent_not_parent_cell_options =
            BitFieldFactory.getInstance(0x8000);

    static final private BitField _border_left = BitFieldFactory.getInstance(0x000F);
    static final private BitField _border_right = BitFieldFactory.getInstance(0x00F0);
    static final private BitField _border_top = BitFieldFactory.getInstance(0x0F00);
    static final private BitField _border_bottom = BitFieldFactory.getInstance(0xF000);

    static final private BitField _left_border_palette_idx =
            BitFieldFactory.getInstance(0x007F);
    static final private BitField _right_border_palette_idx =
            BitFieldFactory.getInstance(0x3F80);
    static final private BitField _diag =
            BitFieldFactory.getInstance(0xC000);

    static final private BitField _top_border_palette_idx =
            BitFieldFactory.getInstance(0x0000007F);
    static final private BitField _bottom_border_palette_idx =
            BitFieldFactory.getInstance(0x00003F80);
    static final private BitField _adtl_diag =
            BitFieldFactory.getInstance(0x001fc000);
    static final private BitField _adtl_diag_line_style =
            BitFieldFactory.getInstance(0x01e00000);

    static final private BitField _adtl_fill_pattern =
            BitFieldFactory.getInstance(0xfc000000);

    static final private BitField _fill_foreground = BitFieldFactory.getInstance(0x007F);
    static final private BitField _fill_background = BitFieldFactory.getInstance(0x3f80);

    private short field_1_font_index;
    private short field_2_format_index;
    private short field_3_cell_options;
    private short field_4_alignment_options;
    private short field_5_indention_options;
    private short field_6_border_options;
    private short field_7_palette_options;
    private int field_8_adtl_palette_options;

    private short field_9_fill_palette_options;

    public ExtendedFormatRecord() {
    }

    public ExtendedFormatRecord(RecordInputStream in) {
        field_1_font_index = in.readShort();
        field_2_format_index = in.readShort();
        field_3_cell_options = in.readShort();
        field_4_alignment_options = in.readShort();
        field_5_indention_options = in.readShort();
        field_6_border_options = in.readShort();
        field_7_palette_options = in.readShort();
        field_8_adtl_palette_options = in.readInt();
        field_9_fill_palette_options = in.readShort();
    }

    public short getFontIndex() {
        return field_1_font_index;
    }

    public void setFontIndex(short index) {
        field_1_font_index = index;
    }

    public short getFormatIndex() {
        return field_2_format_index;
    }

    public void setFormatIndex(short index) {
        field_2_format_index = index;
    }

    public short getCellOptions() {
        return field_3_cell_options;
    }

    public void setCellOptions(short options) {
        field_3_cell_options = options;
    }

    public boolean isLocked() {
        return _locked.isSet(field_3_cell_options);
    }

    public void setLocked(boolean locked) {
        field_3_cell_options = _locked.setShortBoolean(field_3_cell_options,
                locked);
    }

    public boolean isHidden() {
        return _hidden.isSet(field_3_cell_options);
    }

    public void setHidden(boolean hidden) {
        field_3_cell_options = _hidden.setShortBoolean(field_3_cell_options,
                hidden);
    }

    public short getXFType() {
        return _xf_type.getShortValue(field_3_cell_options);
    }

    public void setXFType(short type) {
        field_3_cell_options = _xf_type.setShortValue(field_3_cell_options,
                type);
    }

    public boolean get123Prefix() {
        return _123_prefix.isSet(field_3_cell_options);
    }

    public void set123Prefix(boolean prefix) {
        field_3_cell_options =
                _123_prefix.setShortBoolean(field_3_cell_options, prefix);
    }

    public short getParentIndex() {
        return _parent_index.getShortValue(field_3_cell_options);
    }

    public void setParentIndex(short parent) {
        field_3_cell_options =
                _parent_index.setShortValue(field_3_cell_options, parent);
    }

    public short getAlignmentOptions() {
        return field_4_alignment_options;
    }

    public void setAlignmentOptions(short options) {
        field_4_alignment_options = options;
    }

    public short getAlignment() {
        return _alignment.getShortValue(field_4_alignment_options);
    }

    public void setAlignment(short align) {
        field_4_alignment_options =
                _alignment.setShortValue(field_4_alignment_options, align);
    }

    public boolean getWrapText() {
        return _wrap_text.isSet(field_4_alignment_options);
    }

    public void setWrapText(boolean wrapped) {
        field_4_alignment_options =
                _wrap_text.setShortBoolean(field_4_alignment_options, wrapped);
    }

    public short getVerticalAlignment() {
        return _vertical_alignment.getShortValue(field_4_alignment_options);
    }

    public void setVerticalAlignment(short align) {
        field_4_alignment_options =
                _vertical_alignment.setShortValue(field_4_alignment_options,
                        align);
    }

    public short getJustifyLast() {
        return _justify_last.getShortValue(field_4_alignment_options);
    }

    public void setJustifyLast(short justify) {
        field_4_alignment_options =
                _justify_last.setShortValue(field_4_alignment_options, justify);
    }

    public short getRotation() {
        return _rotation.getShortValue(field_4_alignment_options);
    }

    public void setRotation(short rotation) {
        field_4_alignment_options =
                _rotation.setShortValue(field_4_alignment_options, rotation);
    }

    public short getIndentionOptions() {
        return field_5_indention_options;
    }

    public void setIndentionOptions(short options) {
        field_5_indention_options = options;
    }

    public short getIndent() {
        return _indent.getShortValue(field_5_indention_options);
    }

    public void setIndent(short indent) {
        field_5_indention_options =
                _indent.setShortValue(field_5_indention_options, indent);
    }

    public boolean getShrinkToFit() {
        return _shrink_to_fit.isSet(field_5_indention_options);
    }

    public void setShrinkToFit(boolean shrink) {
        field_5_indention_options =
                _shrink_to_fit.setShortBoolean(field_5_indention_options, shrink);
    }

    public boolean getMergeCells() {
        return _merge_cells.isSet(field_5_indention_options);
    }

    public void setMergeCells(boolean merge) {
        field_5_indention_options =
                _merge_cells.setShortBoolean(field_5_indention_options, merge);
    }

    public short getReadingOrder() {
        return _reading_order.getShortValue(field_5_indention_options);
    }

    public void setReadingOrder(short order) {
        field_5_indention_options =
                _reading_order.setShortValue(field_5_indention_options, order);
    }

    public boolean isIndentNotParentFormat() {
        return _indent_not_parent_format.isSet(field_5_indention_options);
    }

    public void setIndentNotParentFormat(boolean parent) {
        field_5_indention_options =
                _indent_not_parent_format
                        .setShortBoolean(field_5_indention_options, parent);
    }

    public boolean isIndentNotParentFont() {
        return _indent_not_parent_font.isSet(field_5_indention_options);
    }

    public void setIndentNotParentFont(boolean font) {
        field_5_indention_options =
                _indent_not_parent_font.setShortBoolean(field_5_indention_options,
                        font);
    }

    public boolean isIndentNotParentAlignment() {
        return _indent_not_parent_alignment.isSet(field_5_indention_options);
    }

    public void setIndentNotParentAlignment(boolean alignment) {
        field_5_indention_options =
                _indent_not_parent_alignment
                        .setShortBoolean(field_5_indention_options, alignment);
    }

    public boolean isIndentNotParentBorder() {
        return _indent_not_parent_border.isSet(field_5_indention_options);
    }

    public void setIndentNotParentBorder(boolean border) {
        field_5_indention_options =
                _indent_not_parent_border
                        .setShortBoolean(field_5_indention_options, border);
    }

    public boolean isIndentNotParentPattern() {
        return _indent_not_parent_pattern.isSet(field_5_indention_options);
    }

    public void setIndentNotParentPattern(boolean pattern) {
        field_5_indention_options =
                _indent_not_parent_pattern
                        .setShortBoolean(field_5_indention_options, pattern);
    }

    public boolean isIndentNotParentCellOptions() {
        return _indent_not_parent_cell_options
                .isSet(field_5_indention_options);
    }

    public void setIndentNotParentCellOptions(boolean options) {
        field_5_indention_options =
                _indent_not_parent_cell_options
                        .setShortBoolean(field_5_indention_options, options);
    }

    public short getBorderOptions() {
        return field_6_border_options;
    }

    public void setBorderOptions(short options) {
        field_6_border_options = options;
    }

    public short getBorderLeft() {
        return _border_left.getShortValue(field_6_border_options);
    }

    public void setBorderLeft(short border) {
        field_6_border_options =
                _border_left.setShortValue(field_6_border_options, border);
    }

    public short getBorderRight() {
        return _border_right.getShortValue(field_6_border_options);
    }

    public void setBorderRight(short border) {
        field_6_border_options =
                _border_right.setShortValue(field_6_border_options, border);
    }

    public short getBorderTop() {
        return _border_top.getShortValue(field_6_border_options);
    }

    public void setBorderTop(short border) {
        field_6_border_options =
                _border_top.setShortValue(field_6_border_options, border);
    }

    public short getBorderBottom() {
        return _border_bottom.getShortValue(field_6_border_options);
    }

    public void setBorderBottom(short border) {
        field_6_border_options =
                _border_bottom.setShortValue(field_6_border_options, border);
    }

    public short getPaletteOptions() {
        return field_7_palette_options;
    }

    public void setPaletteOptions(short options) {
        field_7_palette_options = options;
    }

    public short getLeftBorderPaletteIdx() {
        return _left_border_palette_idx
                .getShortValue(field_7_palette_options);
    }

    public void setLeftBorderPaletteIdx(short border) {
        field_7_palette_options =
                _left_border_palette_idx.setShortValue(field_7_palette_options,
                        border);
    }

    public short getRightBorderPaletteIdx() {
        return _right_border_palette_idx
                .getShortValue(field_7_palette_options);
    }

    public void setRightBorderPaletteIdx(short border) {
        field_7_palette_options =
                _right_border_palette_idx.setShortValue(field_7_palette_options,
                        border);
    }

    public short getDiag() {
        return _diag.getShortValue(field_7_palette_options);
    }

    public void setDiag(short diag) {
        field_7_palette_options = _diag.setShortValue(field_7_palette_options,
                diag);
    }

    public int getAdtlPaletteOptions() {
        return field_8_adtl_palette_options;
    }

    public void setAdtlPaletteOptions(short options) {
        field_8_adtl_palette_options = options;
    }

    public short getTopBorderPaletteIdx() {
        return (short) _top_border_palette_idx
                .getValue(field_8_adtl_palette_options);
    }

    public void setTopBorderPaletteIdx(short border) {
        field_8_adtl_palette_options =
                _top_border_palette_idx.setValue(field_8_adtl_palette_options,
                        border);
    }

    public short getBottomBorderPaletteIdx() {
        return (short) _bottom_border_palette_idx
                .getValue(field_8_adtl_palette_options);
    }

    public void setBottomBorderPaletteIdx(short border) {
        field_8_adtl_palette_options =
                _bottom_border_palette_idx.setValue(field_8_adtl_palette_options,
                        border);
    }

    public short getAdtlDiag() {
        return (short) _adtl_diag.getValue(field_8_adtl_palette_options);
    }

    public void setAdtlDiag(short diag) {
        field_8_adtl_palette_options =
                _adtl_diag.setValue(field_8_adtl_palette_options, diag);
    }

    public short getAdtlDiagLineStyle() {
        return (short) _adtl_diag_line_style
                .getValue(field_8_adtl_palette_options);
    }

    public void setAdtlDiagLineStyle(short diag) {
        field_8_adtl_palette_options =
                _adtl_diag_line_style.setValue(field_8_adtl_palette_options,
                        diag);
    }

    public short getAdtlFillPattern() {
        return (short) _adtl_fill_pattern
                .getValue(field_8_adtl_palette_options);
    }

    public void setAdtlFillPattern(short fill) {
        field_8_adtl_palette_options =
                _adtl_fill_pattern.setValue(field_8_adtl_palette_options, fill);
    }

    public short getFillPaletteOptions() {
        return field_9_fill_palette_options;
    }

    public void setFillPaletteOptions(short options) {
        field_9_fill_palette_options = options;
    }

    public short getFillForeground() {
        return _fill_foreground.getShortValue(field_9_fill_palette_options);
    }

    public void setFillForeground(short color) {
        field_9_fill_palette_options =
                _fill_foreground.setShortValue(field_9_fill_palette_options,
                        color);
    }

    public short getFillBackground() {
        return _fill_background.getShortValue(field_9_fill_palette_options);
    }

    public void setFillBackground(short color) {
        field_9_fill_palette_options =
                _fill_background.setShortValue(field_9_fill_palette_options,
                        color);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[EXTENDEDFORMAT]\n");
        if (getXFType() == XF_STYLE) {
            buffer.append(" STYLE_RECORD_TYPE\n");
        } else if (getXFType() == XF_CELL) {
            buffer.append(" CELL_RECORD_TYPE\n");
        }
        buffer.append("    .fontindex       = ")
                .append(Integer.toHexString(getFontIndex())).append("\n");
        buffer.append("    .formatindex     = ")
                .append(Integer.toHexString(getFormatIndex())).append("\n");
        buffer.append("    .celloptions     = ")
                .append(Integer.toHexString(getCellOptions())).append("\n");
        buffer.append("          .islocked  = ").append(isLocked())
                .append("\n");
        buffer.append("          .ishidden  = ").append(isHidden())
                .append("\n");
        buffer.append("          .recordtype= ")
                .append(Integer.toHexString(getXFType())).append("\n");
        buffer.append("          .parentidx = ")
                .append(Integer.toHexString(getParentIndex())).append("\n");
        buffer.append("    .alignmentoptions= ")
                .append(Integer.toHexString(getAlignmentOptions())).append("\n");
        buffer.append("          .alignment = ").append(getAlignment())
                .append("\n");
        buffer.append("          .wraptext  = ").append(getWrapText())
                .append("\n");
        buffer.append("          .valignment= ")
                .append(Integer.toHexString(getVerticalAlignment())).append("\n");
        buffer.append("          .justlast  = ")
                .append(Integer.toHexString(getJustifyLast())).append("\n");
        buffer.append("          .rotation  = ")
                .append(Integer.toHexString(getRotation())).append("\n");
        buffer.append("    .indentionoptions= ")
                .append(Integer.toHexString(getIndentionOptions())).append("\n");
        buffer.append("          .indent    = ")
                .append(Integer.toHexString(getIndent())).append("\n");
        buffer.append("          .shrinktoft= ").append(getShrinkToFit())
                .append("\n");
        buffer.append("          .mergecells= ").append(getMergeCells())
                .append("\n");
        buffer.append("          .readngordr= ")
                .append(Integer.toHexString(getReadingOrder())).append("\n");
        buffer.append("          .formatflag= ")
                .append(isIndentNotParentFormat()).append("\n");
        buffer.append("          .fontflag  = ")
                .append(isIndentNotParentFont()).append("\n");
        buffer.append("          .prntalgnmt= ")
                .append(isIndentNotParentAlignment()).append("\n");
        buffer.append("          .borderflag= ")
                .append(isIndentNotParentBorder()).append("\n");
        buffer.append("          .paternflag= ")
                .append(isIndentNotParentPattern()).append("\n");
        buffer.append("          .celloption= ")
                .append(isIndentNotParentCellOptions()).append("\n");
        buffer.append("    .borderoptns     = ")
                .append(Integer.toHexString(getBorderOptions())).append("\n");
        buffer.append("          .lftln     = ")
                .append(Integer.toHexString(getBorderLeft())).append("\n");
        buffer.append("          .rgtln     = ")
                .append(Integer.toHexString(getBorderRight())).append("\n");
        buffer.append("          .topln     = ")
                .append(Integer.toHexString(getBorderTop())).append("\n");
        buffer.append("          .btmln     = ")
                .append(Integer.toHexString(getBorderBottom())).append("\n");
        buffer.append("    .paleteoptns     = ")
                .append(Integer.toHexString(getPaletteOptions())).append("\n");
        buffer.append("          .leftborder= ")
                .append(Integer.toHexString(getLeftBorderPaletteIdx()))
                .append("\n");
        buffer.append("          .rghtborder= ")
                .append(Integer.toHexString(getRightBorderPaletteIdx()))
                .append("\n");
        buffer.append("          .diag      = ")
                .append(Integer.toHexString(getDiag())).append("\n");
        buffer.append("    .paleteoptn2     = ")
                .append(Integer.toHexString(getAdtlPaletteOptions()))
                .append("\n");
        buffer.append("          .topborder = ")
                .append(Integer.toHexString(getTopBorderPaletteIdx()))
                .append("\n");
        buffer.append("          .botmborder= ")
                .append(Integer.toHexString(getBottomBorderPaletteIdx()))
                .append("\n");
        buffer.append("          .adtldiag  = ")
                .append(Integer.toHexString(getAdtlDiag())).append("\n");
        buffer.append("          .diaglnstyl= ")
                .append(Integer.toHexString(getAdtlDiagLineStyle())).append("\n");
        buffer.append("          .fillpattrn= ")
                .append(Integer.toHexString(getAdtlFillPattern())).append("\n");
        buffer.append("    .fillpaloptn     = ")
                .append(Integer.toHexString(getFillPaletteOptions()))
                .append("\n");
        buffer.append("          .foreground= ")
                .append(Integer.toHexString(getFillForeground())).append("\n");
        buffer.append("          .background= ")
                .append(Integer.toHexString(getFillBackground())).append("\n");
        buffer.append("[/EXTENDEDFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFontIndex());
        out.writeShort(getFormatIndex());
        out.writeShort(getCellOptions());
        out.writeShort(getAlignmentOptions());
        out.writeShort(getIndentionOptions());
        out.writeShort(getBorderOptions());
        out.writeShort(getPaletteOptions());
        out.writeInt(getAdtlPaletteOptions());
        out.writeShort(getFillPaletteOptions());
    }

    protected int getDataSize() {
        return 20;
    }

    public short getSid() {
        return sid;
    }

    public void cloneStyleFrom(ExtendedFormatRecord source) {
        field_1_font_index = source.field_1_font_index;
        field_2_format_index = source.field_2_format_index;
        field_3_cell_options = source.field_3_cell_options;
        field_4_alignment_options = source.field_4_alignment_options;
        field_5_indention_options = source.field_5_indention_options;
        field_6_border_options = source.field_6_border_options;
        field_7_palette_options = source.field_7_palette_options;
        field_8_adtl_palette_options = source.field_8_adtl_palette_options;
        field_9_fill_palette_options = source.field_9_fill_palette_options;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + field_1_font_index;
        result = prime * result + field_2_format_index;
        result = prime * result + field_3_cell_options;
        result = prime * result + field_4_alignment_options;
        result = prime * result + field_5_indention_options;
        result = prime * result + field_6_border_options;
        result = prime * result + field_7_palette_options;
        result = prime * result + field_8_adtl_palette_options;
        result = prime * result + field_9_fill_palette_options;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof ExtendedFormatRecord) {
            final ExtendedFormatRecord other = (ExtendedFormatRecord) obj;
            if (field_1_font_index != other.field_1_font_index)
                return false;
            if (field_2_format_index != other.field_2_format_index)
                return false;
            if (field_3_cell_options != other.field_3_cell_options)
                return false;
            if (field_4_alignment_options != other.field_4_alignment_options)
                return false;
            if (field_5_indention_options != other.field_5_indention_options)
                return false;
            if (field_6_border_options != other.field_6_border_options)
                return false;
            if (field_7_palette_options != other.field_7_palette_options)
                return false;
            if (field_8_adtl_palette_options != other.field_8_adtl_palette_options)
                return false;
            return field_9_fill_palette_options == other.field_9_fill_palette_options;
        }
        return false;
    }

}
