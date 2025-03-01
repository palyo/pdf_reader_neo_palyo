package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.ErrorUtil;

public interface ErrorListener {

    int INSUFFICIENT_MEMORY = ErrorUtil.INSUFFICIENT_MEMORY;

    int SYSTEM_CRASH = ErrorUtil.SYSTEM_CRASH;

    int BAD_FILE = ErrorUtil.BAD_FILE;

    int OLD_DOCUMENT = ErrorUtil.OLD_DOCUMENT;

    int PARSE_ERROR = ErrorUtil.PARSE_ERROR;

    int RTF_DOCUMENT = ErrorUtil.RTF_DOCUMENT;

    int PASSWORD_DOCUMENT = ErrorUtil.PASSWORD_DOCUMENT;

    int PASSWORD_INCORRECT = ErrorUtil.PASSWORD_INCORRECT;

    int SD_CARD_ERROR = ErrorUtil.SD_CARD_ERROR;

    int SD_CARD_WRITEDENIED = ErrorUtil.SD_CARD_WRITEDENIED;

    int SD_CARD_NOSPACELEFT = ErrorUtil.SD_CARD_NOSPACELEFT;

    void error(int errorCode);

}
