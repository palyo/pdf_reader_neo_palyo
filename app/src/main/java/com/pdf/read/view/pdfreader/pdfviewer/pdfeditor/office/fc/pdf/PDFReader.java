package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.AbstractReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class PDFReader extends AbstractReader {

    private final String filePath;

    private PDFLib lib;

    public PDFReader(IControl control, String filePath) throws Exception {
        this.control = control;
        this.filePath = filePath;
    }

    public Object getModel() throws Exception {
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);

        lib = PDFLib.getPDFLib();
        lib.openFileSync(filePath);

        return lib;
    }

    public void dispose() {
        super.dispose();
        lib = null;
        control = null;
    }

}
