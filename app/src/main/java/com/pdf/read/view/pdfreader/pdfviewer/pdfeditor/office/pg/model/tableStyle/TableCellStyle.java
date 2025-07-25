package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.tableStyle;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IAttributeSet;

public class TableCellStyle {
    private TableCellBorders cellBorders;
    private Element bgFill;
    private IAttributeSet fontAttr;

    public TableCellBorders getTableCellBorders() {
        return cellBorders;
    }

    public void setTableCellBorders(TableCellBorders cellBorders) {
        this.cellBorders = cellBorders;
    }

    public Element getTableCellBgFill() {
        return bgFill;
    }

    public void setTableCellBgFill(Element bgFill) {
        this.bgFill = bgFill;
    }

    public IAttributeSet getFontAttributeSet() {
        return fontAttr;
    }

    public void setFontAttributeSet(IAttributeSet fontAttr) {
        this.fontAttr = fontAttr;
    }

    public void dispose() {
        cellBorders = null;
        bgFill = null;
        fontAttr = null;
    }
}
