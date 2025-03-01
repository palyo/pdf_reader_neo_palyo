package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;

public class FileKit {

    private static final FileKit mt = new FileKit();

    private FileKit() {

    }

    public static FileKit instance() {
        return mt;
    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File tempFile : files) {
                    deleteFile(tempFile);
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    public void pasteFile(File fromFile, File toFile) {
        if (fromFile.isDirectory()) {
            copyFolder(fromFile, toFile);
        } else {
            copyFile(fromFile, toFile);
        }
    }

    public void copyFile(File fromFile, File toFile) {
        if (toFile.exists()) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte[] bt = new byte[8192];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFolder(File fromFile, File toFile) {
        if (!toFile.exists()) {
            toFile.mkdir();
        }

        String toPath = toFile.getAbsolutePath();
        File[] files = fromFile.listFiles();
        if (files != null) {
            for (File tempFile : files) {
                if (toPath.endsWith(File.separator)) {
                    pasteFile(tempFile, new File(toPath + tempFile.getName()));
                } else {
                    pasteFile(tempFile, new File(toPath + File.separator + tempFile.getName()));
                }
            }
        }
    }

    public boolean isSupport(String fileName) {

        fileName = fileName.toLowerCase();
        return fileName.indexOf(".") > 0 &&
                (fileName.endsWith(KeyKt.FILE_TYPE_DOC)
                        || fileName.endsWith(KeyKt.FILE_TYPE_DOCX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLS)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLSX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_PPT)
                        || fileName.endsWith(KeyKt.FILE_TYPE_PPTX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_TXT)
                        || fileName.endsWith(KeyKt.FILE_TYPE_DOT)
                        || fileName.endsWith(KeyKt.FILE_TYPE_DOTX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_DOTM)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLT)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLTX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLTM)
                        || fileName.endsWith(KeyKt.FILE_TYPE_XLSM)
                        || fileName.endsWith(KeyKt.FILE_TYPE_POT)
                        || fileName.endsWith(KeyKt.FILE_TYPE_PPTM)
                        || fileName.endsWith(KeyKt.FILE_TYPE_POTX)
                        || fileName.endsWith(KeyKt.FILE_TYPE_POTM)
                        || fileName.endsWith(KeyKt.FILE_TYPE_PDF));
    }

    public boolean isFileMarked(String filePath, List<File> fileList) {
        if (filePath == null || fileList == null || fileList.size() == 0) {
            return false;
        }
        for (File file : fileList) {
            if (filePath.equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }
}
