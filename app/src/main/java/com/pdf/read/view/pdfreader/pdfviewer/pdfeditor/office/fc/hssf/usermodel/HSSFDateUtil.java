package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import java.util.Calendar;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.DateUtil;

public class HSSFDateUtil extends DateUtil {
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}
