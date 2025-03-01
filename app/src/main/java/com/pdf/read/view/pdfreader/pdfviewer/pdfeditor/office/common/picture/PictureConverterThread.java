package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture;

public class PictureConverterThread extends Thread {
    private final PictureConverterMgr converterMgr;
    private final String type;
    private final String sourPath;
    private final String destPath;

    public PictureConverterThread(PictureConverterMgr converterMgr, String srcPath, String dstPath, String type) {
        this.converterMgr = converterMgr;
        this.type = type;

        this.sourPath = srcPath;
        this.destPath = dstPath;
    }

    public void run() {
        converterMgr.convertPNG(sourPath, destPath, type, false);
    }
}
