package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture;

public class VectorgraphConverterThread extends Thread {
    private final PictureConverterMgr converterMgr;
    private final byte type;
    private final String sourPath;
    private final String destPath;
    private final int picWidth;
    private final int picHeight;

    public VectorgraphConverterThread(PictureConverterMgr converterMgr, byte type, String srcPath, String dstPath, int width, int height) {
        this.converterMgr = converterMgr;
        this.type = type;

        this.sourPath = srcPath;
        this.destPath = dstPath;
        this.picWidth = width;
        this.picHeight = height;
    }

    public void run() {
        converterMgr.convertWMF_EMF(type, sourPath, destPath, picWidth, picHeight, false);
    }
}
