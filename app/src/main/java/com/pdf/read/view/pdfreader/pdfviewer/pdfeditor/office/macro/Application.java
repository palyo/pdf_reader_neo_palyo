package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.Vector;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.FileKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.MainControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.IPageListViewListener;

public class Application {
    public static final byte APPLICATION_TYPE_WP = KeyKt.APPLICATION_TYPE_WP;
    public static final byte APPLICATION_TYPE_SS = KeyKt.APPLICATION_TYPE_SS;
    public static final byte APPLICATION_TYPE_PPT = KeyKt.APPLICATION_TYPE_PPT;
    public static final byte APPLICATION_TYPE_PDF = KeyKt.APPLICATION_TYPE_PDF;
    public static final byte APPLICATION_TYPE_TXT = KeyKt.APPLICATION_TYPE_WP;

    public static final int STANDARD_RATE = KeyKt.STANDARD_RATE;
    public static final int MAXZOOM = KeyKt.MAX_ZOOM;
    public static final int MAXZOOM_THUMBNAIL = KeyKt.MAX_ZOOM_THUMBNAIL;

    public static final int DRAWMODE_NORMAL = KeyKt.DRAW_MODE_NORMAL;
    public static final int DRAWMODE_CALLOUTDRAW = KeyKt.DRAW_MODE_CALL_OUT_DRAW;
    public static final int DRAWMODE_CALLOUTERASE = KeyKt.DRAW_MODE_CALL_OUT_ERASE;

    public static final byte MOVING_HORIZONTAL = IPageListViewListener.Moving_Horizontal;
    public static final byte MOVING_VERTICAL = IPageListViewListener.Moving_Vertical;

    public static final int THUMBNAILSIZE = 1000;
    protected Toast toast;
    private byte applicationType = -1;
    private ViewGroup parent;
    private MacroFrame frame;
    private MainControl mainControl;

    public Application(Activity activity, ViewGroup parent) {
        frame = new MacroFrame(this, activity);
        mainControl = new MainControl(frame);
        this.parent = parent;
    }

    public void setViewBackground(Object bg) {
        if (frame != null && bg != null && (bg instanceof Integer || bg instanceof Drawable)) {
            frame.setViewBackground(bg);
        }
    }

    public void addOfficeToPictureListener(OfficeToPictureListener listener) {
        if (listener != null) {
            mainControl.setOffictToPicture(new MacroOfficeToPicture(listener));
        }
    }

    public void addDialogListener(DialogListener dlgListener) {
        if (dlgListener != null) {
            mainControl.setCustomDialog(new MacroCustomDialog(dlgListener));
        }
    }

    public void addSlideShowListener(SlideShowListener listener) {
        if (listener != null) {
            mainControl.setSlideShow(new MacroSlideShow(listener));
        }
    }

    public void addOpenFileFinishListener(OpenFileFinishListener listener) {
        frame.addOpenFileFinishListener(listener);
    }

    public void addTouchEventListener(TouchEventListener listener) {
        frame.addTouchEventListener(listener);
    }

    public void addUpdateStatusListener(UpdateStatusListener listener) {
        frame.addUpdateStatusListener(listener);
    }

    public void addErrorListener(ErrorListener listener) {
        frame.addErrorListener(listener);
    }

    public boolean openFile(String filePath) {

        String file = filePath.toLowerCase();
        if (file.endsWith(KeyKt.FILE_TYPE_DOC)
                || file.endsWith(KeyKt.FILE_TYPE_DOCX)
                || file.endsWith(KeyKt.FILE_TYPE_DOT)
                || file.endsWith(KeyKt.FILE_TYPE_DOTX)
                || file.endsWith(KeyKt.FILE_TYPE_DOTM)) {
            applicationType = KeyKt.APPLICATION_TYPE_WP;
        } else if (file.endsWith(KeyKt.FILE_TYPE_XLS)
                || file.endsWith(KeyKt.FILE_TYPE_XLSX)
                || file.endsWith(KeyKt.FILE_TYPE_XLT)
                || file.endsWith(KeyKt.FILE_TYPE_XLTX)
                || file.endsWith(KeyKt.FILE_TYPE_XLTM)
                || file.endsWith(KeyKt.FILE_TYPE_XLSM)) {
            applicationType = KeyKt.APPLICATION_TYPE_SS;
        } else if (file.endsWith(KeyKt.FILE_TYPE_PPT)
                || file.endsWith(KeyKt.FILE_TYPE_PPTX)
                || file.endsWith(KeyKt.FILE_TYPE_POT)
                || file.endsWith(KeyKt.FILE_TYPE_PPTM)
                || file.endsWith(KeyKt.FILE_TYPE_POTX)
                || file.endsWith(KeyKt.FILE_TYPE_POTM)) {
            applicationType = KeyKt.APPLICATION_TYPE_PPT;
        } else if (file.endsWith(KeyKt.FILE_TYPE_PDF)) {
            applicationType = KeyKt.APPLICATION_TYPE_PDF;
        } else {

            if (frame != null && frame.isThumbnail()) {
                setDefaultViewMode(0);
            } else {
                setDefaultViewMode(1);
            }
            applicationType = KeyKt.APPLICATION_TYPE_WP;
        }
        mainControl.openFile(filePath);

        return true;
    }

    protected void openFileFinish() {

        View app = getView();
        if (parent != null) {
            parent.addView(app, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }

    }

    public byte getApplicationType() {
        return applicationType;
    }

    public View getView() {
        if (mainControl == null) {
            return null;
        }
        return mainControl.getView();
    }

    public void pageDown() {
        if (mainControl == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_DOWN_ID, null);
    }

    public void pageUp() {
        if (mainControl == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_UP_ID, null);
    }

    public boolean find(String str) {
        if (mainControl == null
                || str == null
                || str.trim().length() == 0
                || isSlideShowMode()) {
            return false;
        }
        boolean finded = mainControl.getFind().find(str);
        if (!finded && mainControl.getMainFrame().isShowFindDlg()) {
            if (applicationType == APPLICATION_TYPE_PDF) {
                return finded;
            }
            if (toast == null) {
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_NOT_FOUND"));
            toast.show();
        }

        return finded;
    }

    public boolean findBackward() {
        if (mainControl == null || isSlideShowMode()) {
            return false;
        }
        boolean finded = mainControl.getFind().findBackward();
        if (!finded && mainControl.getMainFrame().isShowFindDlg()) {
            if (applicationType == APPLICATION_TYPE_PDF) {
                return finded;
            }
            if (toast == null) {
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN"));
            toast.show();
        }
        return finded;
    }

    public boolean findForward() {
        if (mainControl == null || isSlideShowMode()) {
            return false;
        }
        boolean finded = mainControl.getFind().findForward();
        if (!finded && mainControl.getMainFrame().isShowFindDlg()) {
            if (applicationType == APPLICATION_TYPE_PDF) {
                return finded;
            }
            if (toast == null) {
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_TO_END"));
            toast.show();
        }
        return finded;
    }

    public void setTouchZoom(boolean value) {
        if (frame == null) {
            return;
        }
        frame.setTouchZoom(value);
    }

    public void setDrawPageNumber(boolean value) {
        if (frame == null) {
            return;
        }
        frame.setDrawPageNumber(value);
    }

    public void setShowZoomingMsg(boolean showZoomingMsg) {
        if (frame == null) {
            return;
        }
        frame.setShowZoomingMsg(showZoomingMsg);
    }

    public void setPopUpErrorDlg(boolean popupErrorDlg) {
        if (frame == null) {
            return;
        }
        frame.setPopUpErrorDlg(popupErrorDlg);
    }

    public void setShowPasswordDlg(boolean showPasswordDlg) {
        if (frame != null) {
            frame.setShowPasswordDlg(showPasswordDlg);
        }

    }

    public void setShowFindDlg(boolean b) {
        if (frame != null) {
            frame.setShowFindDlg(b);
        }
    }

    public void setShowProgressBar(boolean showProgressbarDlg) {
        if (frame != null) {
            frame.setShowProgressBar(showProgressbarDlg);
        }

    }

    public void setShowTXTEncodeDlg(boolean showTXTEncodeDlg) {
        if (frame != null) {
            frame.setShowTXTEncodeDlg(showTXTEncodeDlg);
        }
    }

    public void setTXTDefaultEncode(String enode) {
        if (frame != null) {
            frame.setTXTDefaultEncode(enode);
        }
    }

    public void setAppName(String name) {
        if (frame == null) {
            return;
        }
        frame.setAppName(name);
    }

    public void setTopBarHeight(int value) {
        if (frame == null || parent == null) {
            return;
        }
        if (value >= parent.getContext().getResources().getDisplayMetrics().heightPixels / 2) {
            return;
        }
        frame.setTopBarHeight(value);
    }

    public void setBottomBarHeight(int value) {
        if (frame == null || parent == null) {
            return;
        }
        if (value >= parent.getContext().getResources().getDisplayMetrics().heightPixels / 2) {
            return;
        }
        frame.setBottomBarHeight(value);
    }

    public void setChangePage(boolean b) {
        if (frame != null) {
            frame.setChangePage(b);
        }
    }

    public int getZoom() {
        if (mainControl == null) {
            return STANDARD_RATE;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_ZOOM_ID, null);
        return obj == null ? STANDARD_RATE : Math.round(((Float) obj).floatValue() * STANDARD_RATE);
    }

    public int getFitZoom() {
        if (mainControl == null) {
            return STANDARD_RATE;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_FIT_ZOOM_ID, null);
        return obj == null ? STANDARD_RATE : Math.round(((Float) obj).floatValue() * STANDARD_RATE);
    }

    public void setZoom(int value, int pointX, int pointY) {
        if (mainControl == null || getView() == null || value > MAXZOOM
                || value < getFitZoom() || value == getZoom()
                || isSlideShowMode()) {
            return;
        }
        if (pointX < 0 || pointY < 0
                || pointX > getView().getWidth()
                || pointY > getView().getHeight()) {
            pointX = Integer.MIN_VALUE;
            pointY = Integer.MIN_VALUE;
        }

        mainControl.actionEvent(EventConstant.APP_ZOOM_ID, new int[]{value, pointX, pointY});
        getView().postInvalidate();
        if (parent != null) {
            parent.post(new Runnable() {

                @Override
                public void run() {
                    if (mainControl != null) {
                        try {
                            if (applicationType == APPLICATION_TYPE_SS) {
                                mainControl.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                            } else if (applicationType == APPLICATION_TYPE_WP && getViewMode() != 2) {
                                mainControl.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            });
            if (applicationType == APPLICATION_TYPE_WP
                    && frame.isZoomAfterLayoutForWord()) {
                mainControl.actionEvent(EventConstant.WP_LAYOUT_NORMAL_VIEW, null);
            }
        }

    }

    public void setFitSize(int value) {
        if (mainControl == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_SET_FIT_SIZE_ID, value);
    }

    public int getFitSizeState() {
        if (mainControl == null || applicationType == APPLICATION_TYPE_SS
                || (applicationType == APPLICATION_TYPE_WP && getViewMode() != 2)) {
            return 0;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_FIT_SIZE_STATE_ID, null);
        return obj == null ? 3 : (Integer) obj;

    }

    public Bitmap getSnapshot(Bitmap destBitmap) {
        if (mainControl == null || destBitmap == null) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_SNAPSHOT_ID, destBitmap);
        return obj == null ? null : (Bitmap) obj;
    }

    public int getPagesCount() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer) obj;

    }

    public int getCurrentPageNumber() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public Bitmap getPageToImage(int pageNumber) {
        if (mainControl == null || pageNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_PAGE_TO_IMAGE, pageNumber);
        return obj == null ? null : (Bitmap) obj;
    }

    private Bitmap getAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (mainControl == null || pageNumber < 1) {
            return null;
        }

        Object obj = mainControl.getActionValue(EventConstant.APP_PAGEAREA_TO_IMAGE,
                new int[]{pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight});
        return obj == null ? null : (Bitmap) obj;
    }

    public Bitmap getPageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (applicationType != KeyKt.APPLICATION_TYPE_WP) {
            return null;
        }

        return getAreaToImage(pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }

    public Rectangle getPageSize(int pageNumber) {
        if (mainControl == null) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_GET_PAGE_SIZE, pageNumber);
        return obj == null ? null : (Rectangle) obj;
    }

    public int getViewMode() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_GET_VIEW_MODE, null);
        return obj == null ? 0 : (Integer) obj;
    }

    public void setDefaultViewMode(int viewMode) {
        if (frame == null) {
            return;
        }
        if (viewMode < 0 || viewMode > 2) {
            viewMode = 0;
        }
        frame.setWordDefaultView((byte) viewMode);
    }

    public void setZoomAfterLayoutForNormalView(boolean value) {
        if (frame == null) {
            return;
        }
        frame.setZoomAfterLayoutForWord(value);
    }

    public void showPage(int index) {
        if (mainControl == null || index < 0) {
            return;
        }
        mainControl.actionEvent(EventConstant.WP_SHOW_PAGE, index);
    }

    public void switchViewMode(int viewMode) {
        if (mainControl == null) {
            return;
        }
        if (viewMode < 0 || viewMode > 2) {
            viewMode = 0;
        }
        mainControl.actionEvent(EventConstant.WP_SWITCH_VIEW, viewMode);
    }

    public Vector<String> getAllSheetName() {
        if (mainControl == null) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.SS_GET_ALL_SHEET_NAME, null);
        return obj == null ? null : (Vector<String>) obj;
    }

    public String getSheetName(int sheetNumber) {
        if (mainControl == null || sheetNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.SS_GET_SHEET_NAME, sheetNumber);
        return obj == null ? null : (String) obj;
    }

    public int getCurrentSheetNumber() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public int getSheetsCount() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public void showSheet(int index) {
        if (mainControl == null || index < 0) {
            return;
        }
        mainControl.actionEvent(EventConstant.SS_SHOW_SHEET, index);
    }

    public void removeDefaultSheetBarForExcel() {
        mainControl.actionEvent(EventConstant.SS_REMOVE_SHEET_BAR, null);
    }

    public void previousSlide() {
        if (mainControl == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_UP_ID, null);
    }

    public void nextSlide() {
        if (mainControl == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_DOWN_ID, null);
    }

    public int getSlidesCount() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public int getLoadSlidesCount() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_REAL_PAGE_COUNT_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public int getCurrentSlideNumber() {
        if (mainControl == null) {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer) obj;
    }

    public String getSlideNote(int slideNumber) {
        if (mainControl == null || slideNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_GET_SLIDE_NOTE, slideNumber);
        return obj == null ? null : (String) obj;
    }

    public Rectangle getSlideSize(int slideNumber) {
        if (mainControl == null) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_GET_SLIDE_SIZE, slideNumber);
        return obj == null ? null : (Rectangle) obj;
    }

    public void showSlide(int index) {
        if (mainControl == null || index < 0) {
            return;
        }
        mainControl.actionEvent(EventConstant.PG_SHOW_SLIDE_ID, index);
    }

    public Bitmap getSlideToImage(int slideNumber) {
        if (mainControl == null || slideNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDE_TO_IMAGE, slideNumber);
        return obj == null ? null : (Bitmap) obj;
    }

    public Bitmap getSlideAreaToImage(int slideNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (applicationType != KeyKt.APPLICATION_TYPE_PPT) {
            return null;
        }
        return getAreaToImage(slideNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }

    public Bitmap getSlideThumbnail(int slideNumber, int zoomValue) {
        if (applicationType != APPLICATION_TYPE_PPT
                || mainControl == null
                || slideNumber < 1
                || zoomValue <= 0
                || zoomValue > MAXZOOM_THUMBNAIL) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{slideNumber, zoomValue});
        return obj == null ? null : (Bitmap) obj;
    }

    public void showPDFPage(int index) {
        if (mainControl == null || index < 0) {
            return;
        }
        mainControl.actionEvent(EventConstant.PDF_SHOW_PAGE, index);
    }

    public Bitmap getPDFPageToImage(int pageNumber) {
        if (mainControl == null || pageNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PDF_PAGE_TO_IMAGE, pageNumber);
        return obj == null ? null : (Bitmap) obj;
    }

    public Bitmap getPDFPageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (applicationType != KeyKt.APPLICATION_TYPE_PDF) {
            return null;
        }

        return getAreaToImage(pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }

    public Bitmap getPDFPageThumbnail(int pageNumber, int zoomValue) {
        if (applicationType != APPLICATION_TYPE_PDF
                || mainControl == null
                || pageNumber < 1
                || zoomValue <= 0
                || zoomValue > MAXZOOM_THUMBNAIL) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{pageNumber, zoomValue});
        return obj == null ? null : (Bitmap) obj;
    }

    public String[] getPDFHyperlinkURL(int pageNumber) {
        if (mainControl == null || pageNumber < 1) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_HYPERLINK_URL_ID, pageNumber);
        return obj == null ? null : (String[]) obj;
    }

    public Rectangle getPDFPageSize(int pageNumber) {
        if (mainControl == null) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PDF_GET_PAGE_SIZE, pageNumber);
        return obj == null ? null : (Rectangle) obj;
    }

    public void addI18NResID(String resName, int resID) {
        frame.addI18NResID(resName, resID);
    }

    public boolean authenticatePassword(String password) {
        if (mainControl == null
                || password == null
                || password.trim().length() == 0) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_AUTHENTICATE_PASSWORD, password);
        return obj != null && (Boolean) obj;
    }

    public void passwordVerified(String password) {
        if (mainControl == null) {
            return;
        }
        if (!authenticatePassword(password)) {
            mainControl.getSysKit().getErrorKit().writerLog(new Throwable("Password is incorrect"));
        } else {
            mainControl.actionEvent(EventConstant.APP_PASSWORD_OK_INIT, password);
        }
    }

    public void txtEncodeDialogFinished(String encode) {
        if (mainControl == null || encode == null) {
            return;
        }
        mainControl.actionEvent(EventConstant.TXT_DIALOG_FINISH_ID, encode);
    }

    public void reopenTXT(String filePath, String encode) {
        if (mainControl == null || filePath == null || encode == null) {
            return;
        }
        if (filePath.toLowerCase().endsWith(KeyKt.FILE_TYPE_TXT)) {
            setDefaultViewMode(1);
            applicationType = KeyKt.APPLICATION_TYPE_WP;
            mainControl.actionEvent(EventConstant.TXT_REOPNE_ID, new String[]{filePath, encode});
        }
    }

    public boolean isSupport(String fileName) {
        return FileKit.instance().isSupport(fileName);
    }

    public void setAnimationDuration(int duration) {
        if (mainControl != null && !isSlideShowMode()) {
            duration = Math.min(1200, Math.max(duration, 100));
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_DURATION, duration);
        }
    }

    public void beginSlideShow(int slideIndex) {
        if (mainControl != null) {
            if (slideIndex < 1 || slideIndex > getSlidesCount()) {
                slideIndex = 1;
            }
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_GEGIN, slideIndex >= 1 ? slideIndex : 1);
        }
    }

    public void exitSlideShow() {
        if (mainControl != null) {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_END, null);
        }
    }

    public boolean hasNextSlide_Slideshow() {
        if (mainControl == null) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_PAGE_DOWN_ID, null);
        return obj != null && (Boolean) obj;
    }

    public boolean hasPreviousSlide_Slideshow() {
        if (mainControl == null) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_PAGE_UP_ID, null);
        return obj != null && (Boolean) obj;
    }

    public boolean hasNextAction_Slideshow() {
        if (mainControl == null) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_HASNEXTACTION, null);
        return obj != null && (Boolean) obj;
    }

    public void nextAction_Slideshow() {
        if (mainControl != null) {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_NEXT, null);
        }
    }

    public boolean hasPreviousAction_Slideshow() {
        if (mainControl == null) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_HASPREVIOUSACTION, null);
        return obj != null && (Boolean) obj;
    }

    public void previousAction_Slideshow() {
        if (mainControl != null) {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_PREVIOUS, null);
        }
    }

    private boolean isSlideShowMode() {
        if (mainControl == null
                || applicationType != APPLICATION_TYPE_PPT) {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW, null);
        return obj != null && (Boolean) obj;
    }

    public boolean isSlideExist(int slideIndex) {
        if (mainControl != null
                && applicationType == APPLICATION_TYPE_PPT
                && slideIndex > 0) {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_SLIDEEXIST, slideIndex);
            return obj != null && (Boolean) obj;
        }

        return false;
    }

    public int getSlideAnimationSteps(int slideIndex) {
        if (isSlideExist(slideIndex)) {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_ANIMATIONSTEPS, slideIndex);
            return obj == null ? -1 : (Integer) obj;
        }

        return -1;
    }

    public Bitmap getSlideshowToImage(int slideIndex, int step) {
        if (!isSlideShowMode()
                && step > 0
                && step <= getSlideAnimationSteps(slideIndex)) {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_SLIDESHOWTOIMAGE, new int[]{slideIndex, step});
            return obj == null ? null : (Bitmap) obj;
        }
        return null;
    }

    public String getTemporaryDirectoryPath() {
        if (mainControl == null) {
            return null;
        }
        return mainControl.getSysKit().getPictureManage().getPicTempPath();
    }

    public void setWriteLog(boolean writeLog) {
        if (frame == null) {
            return;
        }
        frame.setWriteLog(writeLog);
    }

    public void setThumbnail(boolean isThumbnail) {
        if (frame == null) {
            return;
        }
        frame.setThumbnail(isThumbnail);
    }

    public Bitmap getDocThumbnail(int zoomValue) {
        if (applicationType != APPLICATION_TYPE_WP
                || mainControl == null
                || zoomValue <= 0
                || zoomValue > MAXZOOM_THUMBNAIL) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, zoomValue);
        return obj == null ? null : (Bitmap) obj;
    }

    public Bitmap getTxtThumbnail(int zoomValue) {
        if (applicationType != APPLICATION_TYPE_WP
                || mainControl == null
                || zoomValue <= 0
                || zoomValue > MAXZOOM_THUMBNAIL) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, zoomValue);
        return obj == null ? null : (Bitmap) obj;
    }

    public Bitmap getXlsThumbnail(int width, int height, int zoomValue) {
        if (applicationType != APPLICATION_TYPE_SS
                || mainControl == null
                || width < 1 || width > THUMBNAILSIZE
                || height < 1 || height > THUMBNAILSIZE
                || zoomValue <= 0
                || zoomValue > MAXZOOM_THUMBNAIL) {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{width, height, zoomValue});
        return obj == null ? null : (Bitmap) obj;
    }

    public void dispose() {
        if (parent != null) {
            if (getView() != null) {
                parent.removeView(getView());
            }
        }
        if (mainControl.getReader() != null) {
            mainControl.getReader().abortReader();
        }
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
        if (mainControl != null) {
            mainControl.dispose();
            mainControl = null;
        }
        parent = null;
    }

    public int getCalloutLineWidth() {
        if (mainControl == null) {
            return 1;
        }
        return mainControl.getSysKit().getCalloutManager().getWidth();
    }

    public void setCalloutLineWidth(int width) {
        if (width < 1 || width > 10) {
            return;
        }
        if (mainControl != null) {
            mainControl.getSysKit().getCalloutManager().setWidth(width);
        }
    }

    public int getCalloutColor() {
        if (mainControl == null) {
            return Color.RED;
        }
        return mainControl.getSysKit().getCalloutManager().getColor();
    }

    public void setCalloutColor(byte alpha, byte red, byte green, byte blue) {
        if (mainControl != null) {
            int color = (alpha << 24) | (red << 16 & 0xFF0000) | (green << 8 & 0xFF00) | (blue & 0xFF);
            mainControl.getSysKit().getCalloutManager().setColor(color);
        }
    }

    public int getDrawingMode() {
        if (mainControl == null) {
            return KeyKt.DRAW_MODE_NORMAL;
        }

        return mainControl.getSysKit().getCalloutManager().getDrawingMode();
    }

    public void setDrawingMode(int mode) {
        if (mainControl == null || mode < DRAWMODE_NORMAL || mode > DRAWMODE_CALLOUTERASE) {
            return;
        }
        mainControl.getSysKit().getCalloutManager().setDrawingMode(mode);

        if (mode == DRAWMODE_CALLOUTDRAW) {
            parent.post(new Runnable() {

                @Override
                public void run() {
                    mainControl.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
                }
            });
        }
    }

    public boolean hasConvertingVectorgraph(int viewIndex) {
        return mainControl.getSysKit().getPictureManage().hasConvertingVectorgraph(viewIndex);
    }

    public boolean isIgnoreOriginalSize() {
        if (frame != null) {
            return frame.isIgnoreOriginalSize();
        }
        return false;
    }

    public void setIgnoreOriginalSize(boolean ignoreOriginalSize) {
        if (frame != null) {
            frame.setIgnoreOriginalSize(ignoreOriginalSize);
        }
    }

    public byte getPageListViewMovingPosition() {
        if (frame != null) {
            return frame.getPageListViewMovingPosition();
        }

        return MOVING_HORIZONTAL;
    }

    public void setPageListViewMovingPosition(byte position) {
        if (frame != null) {
            frame.setPageListViewMovingPosition(position);
        }
    }

}
