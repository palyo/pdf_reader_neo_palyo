package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.os.Handler;
import android.os.Message;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.doc.DOCReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.doc.DOCXReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.doc.TXTReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.PPTReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.PPTXReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.XLSReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.XLSXReader;

public class FileReaderThread extends Thread {
    private String encoding;
    private String filePath;
    private Handler handler;
    private IControl control;

    public FileReaderThread(IControl control, Handler handler, String filePath, String encoding) {
        this.control = control;
        this.handler = handler;
        this.filePath = filePath;
        this.encoding = encoding;
    }

    public void run() {
        Message msg = new Message();
        msg.what = KeyKt.HANDLER_MESSAGE_SHOW_PROGRESS;
        handler.handleMessage(msg);

        msg = new Message();
        msg.what = KeyKt.HANDLER_MESSAGE_DISMISS_PROGRESS;
        try {
            IReader reader = null;
            String fileName = filePath.toLowerCase();

            if (fileName.endsWith(KeyKt.FILE_TYPE_DOC) || fileName.endsWith(KeyKt.FILE_TYPE_DOT)) {
                reader = new DOCReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_DOCX) || fileName.endsWith(KeyKt.FILE_TYPE_DOTX) || fileName.endsWith(KeyKt.FILE_TYPE_DOTM)) {
                reader = new DOCXReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_TXT)) {
                reader = new TXTReader(control, filePath, encoding);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_XLS)
                    || fileName.endsWith(KeyKt.FILE_TYPE_XLT)) {
                reader = new XLSReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_XLSX)
                    || fileName.endsWith(KeyKt.FILE_TYPE_XLTX)
                    || fileName.endsWith(KeyKt.FILE_TYPE_XLTM)
                    || fileName.endsWith(KeyKt.FILE_TYPE_XLSM)) {
                reader = new XLSXReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_PPT)
                    || fileName.endsWith(KeyKt.FILE_TYPE_POT)) {
                reader = new PPTReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_PPTX)
                    || fileName.endsWith(KeyKt.FILE_TYPE_PPTM)
                    || fileName.endsWith(KeyKt.FILE_TYPE_POTX)
                    || fileName.endsWith(KeyKt.FILE_TYPE_POTM)) {
                reader = new PPTXReader(control, filePath);
            } else if (fileName.endsWith(KeyKt.FILE_TYPE_PDF)) {
                reader = new PDFReader(control, filePath);
            } else {
                reader = new TXTReader(control, filePath, encoding);
            }

            Message mesReader = new Message();
            mesReader.obj = reader;
            mesReader.what = KeyKt.HANDLER_MESSAGE_SEND_READER_INSTANCE;
            handler.handleMessage(mesReader);

            msg.obj = reader.getModel();
            reader.dispose();
            msg.what = KeyKt.HANDLER_MESSAGE_SUCCESS;

        } catch (OutOfMemoryError eee) {
            msg.what = KeyKt.HANDLER_MESSAGE_ERROR;
            msg.obj = eee;
        } catch (Exception ee) {
            msg.what = KeyKt.HANDLER_MESSAGE_ERROR;
            msg.obj = ee;
        } catch (AbortReaderError ee) {
            msg.what = KeyKt.HANDLER_MESSAGE_ERROR;
            msg.obj = ee;
        } finally {
            handler.handleMessage(msg);
            control = null;
            handler = null;
            encoding = null;
            filePath = null;
        }
    }
}
