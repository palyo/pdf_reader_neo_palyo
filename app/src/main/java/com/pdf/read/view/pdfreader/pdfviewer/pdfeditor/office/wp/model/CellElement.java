package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.model;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AbstractElement;

public class CellElement extends AbstractElement {

    public CellElement() {
        super();
    }

    public short getType() {
        return WPModelConstant.TABLE_CELL_ELEMENT;
    }
}
