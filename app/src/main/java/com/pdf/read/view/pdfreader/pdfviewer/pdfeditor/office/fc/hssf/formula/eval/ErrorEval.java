package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel.ErrorConstants;

public final class ErrorEval implements ValueEval {

    public static final ErrorEval NULL_INTERSECTION = new ErrorEval(ErrorConstants.ERROR_NULL);
    public static final ErrorEval DIV_ZERO = new ErrorEval(ErrorConstants.ERROR_DIV_0);
    public static final ErrorEval VALUE_INVALID = new ErrorEval(ErrorConstants.ERROR_VALUE);
    public static final ErrorEval REF_INVALID = new ErrorEval(ErrorConstants.ERROR_REF);
    public static final ErrorEval NAME_INVALID = new ErrorEval(ErrorConstants.ERROR_NAME);
    public static final ErrorEval NUM_ERROR = new ErrorEval(ErrorConstants.ERROR_NUM);
    public static final ErrorEval NA = new ErrorEval(ErrorConstants.ERROR_NA);
    private static final ErrorConstants EC = null;
    private static final int CIRCULAR_REF_ERROR_CODE = 0xFFFFFFC4;
    public static final ErrorEval CIRCULAR_REF_ERROR = new ErrorEval(CIRCULAR_REF_ERROR_CODE);
    private static final int FUNCTION_NOT_IMPLEMENTED_CODE = 0xFFFFFFE2;
    private final int _errorCode;

    private ErrorEval(int errorCode) {
        _errorCode = errorCode;
    }

    public static ErrorEval valueOf(int errorCode) {
        switch (errorCode) {
            case ErrorConstants.ERROR_NULL:
                return NULL_INTERSECTION;
            case ErrorConstants.ERROR_DIV_0:
                return DIV_ZERO;
            case ErrorConstants.ERROR_VALUE:
                return VALUE_INVALID;
            case ErrorConstants.ERROR_REF:
                return REF_INVALID;
            case ErrorConstants.ERROR_NAME:
                return NAME_INVALID;
            case ErrorConstants.ERROR_NUM:
                return NUM_ERROR;
            case ErrorConstants.ERROR_NA:
                return NA;

            case CIRCULAR_REF_ERROR_CODE:
                return CIRCULAR_REF_ERROR;
        }
        throw new RuntimeException("Unexpected error code (" + errorCode + ")");
    }

    public static String getText(int errorCode) {
        if (ErrorConstants.isValidCode(errorCode)) {
            return ErrorConstants.getText(errorCode);
        }

        switch (errorCode) {
            case CIRCULAR_REF_ERROR_CODE:
                return "~CIRCULAR~REF~";
            case FUNCTION_NOT_IMPLEMENTED_CODE:
                return "~FUNCTION~NOT~IMPLEMENTED~";
        }
        return "~non~std~err(" + errorCode + ")~";
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public String toString() {
        String sb = getClass().getName() + " [" +
                getText(_errorCode) +
                "]";
        return sb;
    }
}
