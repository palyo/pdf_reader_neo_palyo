package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

const val CURRENT_TAB = "current_tab"
const val IS_SETTINGS = "is_settings"
const val IS_LANGUAGE_ENABLED = "is_language_enabled"
const val IS_ONBOARDING_ENABLED = "is_onboarding_enabled"
const val DOC_FILE = "doc_file"
const val PPT_FILE = "ppt_file"
const val XLS_FILE = "xls_file"
const val TXT_FILE = "txt_file"
const val PDF_FILE = "pdf_file"
const val ALL_FILE = "all_file"

const val FILE_URI = "fileUri"
const val FILE_NAME = "fileName"
const val FILE_PATH = "filePath"

const val APPLICATION_TYPE_WP: Byte = 0
const val APPLICATION_TYPE_SS: Byte = 1
const val APPLICATION_TYPE_PPT: Byte = 2
const val APPLICATION_TYPE_PDF: Byte = 3
const val FILE_TYPE_DOC = "doc"
const val FILE_TYPE_DOCX = "docx"
const val FILE_TYPE_XLS = "xls"
const val FILE_TYPE_XLSX = "xlsx"
const val FILE_TYPE_PPT = "ppt"
const val FILE_TYPE_PPTX = "pptx"
const val FILE_TYPE_TXT = "txt"
const val FILE_TYPE_PDF = "pdf"

const val FILE_TYPE_DOT = "dot"
const val FILE_TYPE_DOTX = "dotx"
const val FILE_TYPE_DOTM = "dotm"
const val FILE_TYPE_XLT = "xlt"
const val FILE_TYPE_XLTX = "xltx"
const val FILE_TYPE_XLSM = "xlsm"
const val FILE_TYPE_XLTM = "xltm"
const val FILE_TYPE_POT = "pot"
const val FILE_TYPE_PPTM = "pptm"
const val FILE_TYPE_POTX = "potx"
const val FILE_TYPE_POTM = "potm"

const val GAP = 5
const val ZOOM_ROUND = 10000000

const val POINT_DPI = 72.0f
const val MM_TO_POINT = 2.835f
const val TWIPS_TO_POINT = 1 / 20.0f
const val POINT_TO_TWIPS = 20.0f
const val DEFAULT_TAB_WIDTH_POINT = 21f
const val EMU_PER_INCH = 914400
const val PIXEL_DPI = 96f
const val POINT_TO_PIXEL = PIXEL_DPI / POINT_DPI
const val TWIPS_TO_PIXEL = TWIPS_TO_POINT * POINT_TO_PIXEL
const val DEFAULT_TAB_WIDTH_PIXEL = DEFAULT_TAB_WIDTH_POINT * POINT_TO_PIXEL
const val PIXEL_TO_POINT = POINT_DPI / PIXEL_DPI
const val PIXEL_TO_TWIPS = PIXEL_TO_POINT * POINT_TO_TWIPS
const val HANDLER_MESSAGE_SUCCESS = 0
const val HANDLER_MESSAGE_ERROR = HANDLER_MESSAGE_SUCCESS + 1
const val HANDLER_MESSAGE_SHOW_PROGRESS = HANDLER_MESSAGE_ERROR + 1
const val HANDLER_MESSAGE_DISMISS_PROGRESS = HANDLER_MESSAGE_SHOW_PROGRESS + 1
const val HANDLER_MESSAGE_DISPOSE = HANDLER_MESSAGE_DISMISS_PROGRESS + 1
const val HANDLER_MESSAGE_SEND_READER_INSTANCE = HANDLER_MESSAGE_DISPOSE
const val STANDARD_RATE = 10000
const val MAX_ZOOM = 30000
const val MAX_ZOOM_THUMBNAIL = 5000

const val DRAW_MODE_NORMAL = 0
const val DRAW_MODE_CALL_OUT_DRAW = 1
const val DRAW_MODE_CALL_OUT_ERASE = 2

const val IS_HORIZONTAL_SWIPE_ENABLED ="is_horizontal_swipe_enabled"
const val IS_DEFAULT_APP_ENABLED ="is_default_app_enabled"
const val NIGHT_MODE ="nightMode"
const val IS_ZOOM_ENABLED ="is_zoom_enabled"